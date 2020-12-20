package com.langer.server.api.admin;

import com.langer.server.api.admin.dto.*;
import com.langer.server.api.data.dto.UserLanguageProgress;
import com.langer.server.service.CorpusStatisticsService;
import com.langer.server.model.entity.impl.*;
import com.langer.server.model.repository.*;
import com.langer.server.service.TextStorage;
import com.langer.server.service.LangerModelMapper;
import com.langer.server.service.UserProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi
{
    private final LanguageRepository         languageRepository;
    private final ResourceRepository         resourceRepository;
    private final EpisodeRepository          episodeRepository;
    private final ParagraphRepository        paragraphRepository;
    private final SentenceRepository         sentenceRepository;
    private final TextStorage                textStorage;
    private final UserProgressService        userProgressService;
    private final CorpusStatisticsService    corpusStatisticsCalculator;
    private final LangerModelMapper          modeler;

    @Override
    public LanguageDto getLanguage(String name)
    {
        Language language = languageRepository.findByName(name);
        if (language == null)
            return null;
        return modeler.toDto(language);
    }

    @Override
    public long createLanguage(LanguageDto languageDto)
    {
        Assert.isTrue(!languageRepository.existsByName(languageDto.getName()), "The language " + languageDto.getName() + " already exists");
        Language language = new Language();
        language.setName(languageDto.getName());
        languageRepository.save(language);
        return language.getId();
    }

    @Override
    public List<ResourceDto> getResources(long languageId)
    {
        List<Resource> resources = resourceRepository.findAllByLanguageId(languageId);
        return resources.stream().map(resource -> modeler.toDto(resource)).collect(Collectors.toList());
    }

    @Override
    public long createResource(ResourceDto resourceDto)
    {
        Assert.isTrue(!resourceRepository.existsByTitle(resourceDto.getTitle()), "The resource " + resourceDto.getTitle() + " already exists");

        Resource resource = new Resource();
        resource.setTitle(resourceDto.getTitle());
        resource.setLanguageId(resourceDto.getLanguageId());
        resourceRepository.save(resource);
        return resource.getId();
    }

    @Override
    public void deleteResource(long id)
    {
        resourceRepository.deleteById(id);
    }

    @Override
    public FindResourceResponse findResource(FindResourceRequest findResourceRequest)
    {
        Language language = languageRepository.findByName(findResourceRequest.getLanguageName());
        Resource resource = resourceRepository.findByTitleAndLanguageId(findResourceRequest.getResourceTitle(), language.getId());

        if (resource == null)
        {
            throw new ResponseStatusException(NOT_FOUND, "Unknown resource " + findResourceRequest.getResourceTitle() + " for " + language.getName());
        }

        LanguageDto languageDto = modeler.toDto(language);
        ResourceDto resourceDto = modeler.toDto(resource);
        FindResourceResponse findResourceResponse = new FindResourceResponse(languageDto, resourceDto);
        return findResourceResponse;
    }

    @Override
    public List<EpisodeDto> getEpisodes(long resourceId)
    {
        List<Episode> episodes = episodeRepository.findAllByResourceId(resourceId);
        return episodes.stream().map(episode -> modeler.toDto(episode)).collect(Collectors.toList());
    }

    @Override
    public FindEpisodeResponse findEpisode(FindEpisodeRequest findEpisodeRequest)
    {
        Language language = languageRepository.findByName(findEpisodeRequest.getLanguageName());
        Resource resource = resourceRepository.findByTitleAndLanguageId(findEpisodeRequest.getResourceTitle(), language.getId());
        Episode  episode  = episodeRepository.findByTitleAndResourceId(findEpisodeRequest.getEpisodeTitle(), resource.getId());

        if (episode == null)
        {
            throw new ResponseStatusException(NOT_FOUND, "Unknown episode " + findEpisodeRequest.getEpisodeTitle());
        }

        LanguageDto languageDto = modeler.toDto(language);
        ResourceDto resourceDto = modeler.toDto(resource);
        EpisodeDto  episodeDto  = modeler.toDto(episode);
        FindEpisodeResponse findEpisodeResponse = new FindEpisodeResponse(languageDto, resourceDto, episodeDto);
        return findEpisodeResponse;
    }

    @Override
    public long createEpisode(EpisodeDto episodeDto)
    {
        Assert.isTrue(!episodeRepository.existsByTitleAndResourceId(episodeDto.getTitle(), episodeDto.getResourceId()), "The episode " + episodeDto.getTitle() + " already exists");

        Episode episode = new Episode();
        episode.setTitle(episodeDto.getTitle());
        episode.setText(episodeDto.getText());
        episode.setResourceId(episodeDto.getResourceId());
        episodeRepository.save(episode);

        Resource resource = resourceRepository.findById(episodeDto.getResourceId()).get();
        Language language = languageRepository.findById(resource.getLanguageId()).get();

        textStorage.storeEpisodeText(episode.getText(), episode.getId(), language);

        log.info("Episode " + episode.getId() + " created successfully (" + episode.getTitle() + ")");
        return episode.getId();
    }

    @Override
    public void deleteEpisode(long id)
    {
        episodeRepository.deleteById(id);
    }

    @Override
    public FindSentenceResponse findSentence(FindSentenceRequest findSentenceRequest)
    {
        Language                language        = languageRepository.findByName(findSentenceRequest.getLanguageName());
        Resource                resource        = resourceRepository.findByTitleAndLanguageId(findSentenceRequest.getResourceTitle(), language.getId());
        Episode                 episode         = episodeRepository.findByTitleAndResourceId(findSentenceRequest.getEpisodeTitle(), resource.getId());

        return textStorage.findSentence(findSentenceRequest, language, resource, episode);
    }

    @Override
    public void updateSentenceTranslation(long id, String sentenceTranslation)
    {
        textStorage.updateSentenceTranslation(id, sentenceTranslation);
    }

    public void updateSentenceShouldBeIgnored(long id, boolean shouldBeIgnored)
    {
        textStorage.updateSentenceShouldBeIgnored(id, shouldBeIgnored);
    }

    @Override
    @Transactional
    public void updateEpisodeText(long id, String text)
    {
        Episode episode = episodeRepository.findById(id).get();
        episode.setText(text);
        episodeRepository.save(episode);

        paragraphRepository.deleteAllByEpisodeId(id);
        List<Sentence> sentences = sentenceRepository.findAllByEpisodeId(id);
        sentenceRepository.deleteAll(sentences);

        Language language = languageRepository.findByEpisodeId(id);
        textStorage.storeEpisodeText(episode.getText(), id, language);
    }

    @Override
    public WordDto getWord(long id)
    {
        return textStorage.getWord(id);
    }

    @Override
    public void deleteWord(long id)
    {
        textStorage.deleteWord(id);
    }

    @Override
    public void updateWordType(@PathVariable("id") long id, @RequestBody WordType type)
    {
        textStorage.updateWordType(id, type);
    }

    @Override
    public void createWordTranslation(CreateWordTranslationRequest createWordTranslationRequest)
    {
        textStorage.createWordTranslation(createWordTranslationRequest);
    }

    @Override
    public void setWordOccurrenceTranslation(long id, SetWordOccurrenceTranslationRequest setWordOccurrenceTranslationRequest)
    {
        textStorage.setWordOccurrenceTranslation(id, setWordOccurrenceTranslationRequest);
    }

    @Override
    public CorpusStatisticsResponse getCorpusStatistics(long languageId)
    {
        Language language = languageRepository.findById(languageId).get();
        return corpusStatisticsCalculator.calculateCorpusStatistics(language);
    }

    @Override
    public List<Dictionary> getDictionaries()
    {
        Map<Long, Language> idToLanguage = languageRepository.findAll().stream().collect(Collectors.toMap(Language::getId, Function.identity()));
        return textStorage.getDictionaries(idToLanguage);
    }

    @Override
    public void applyDictionaries(List<Dictionary> dictionaries)
    {
        Map<String, Language> nameToLanguage = languageRepository.findAll().stream().collect(Collectors.toMap(Language::getName, Function.identity()));
        for (Dictionary dictionary : dictionaries)
        {
            Language language = nameToLanguage.get(dictionary.getLanguageName());
            textStorage.applyDictionary(dictionary, language.getId());
        }
    }

    @Override
    public boolean isUnderMaintenance()
    {
        return textStorage.isUnderMaintenance();
    }

    @Override
    public List<UserLanguageProgress> getUserProgress(long userId)
    {
        return userProgressService.getUserProgress(userId);
    }

    @Override
    @Transactional
    public void clearUserProgress(long userId)
    {
        userProgressService.clearUserProgress(userId);
    }

    @Override
    @Transactional
    public void restoreFromEpisodeTexts()
    {
        //textStorage.cleanForRestore();

        List<Language> languages = languageRepository.findAll();
        languages.forEach(
                language ->
                {
                    List<Episode> episdoes = episodeRepository.findAllByLanguageId(language.getId());
                    episdoes.forEach(
                            episode ->
                            {
                                textStorage.storeEpisodeText(episode.getText(), episode.getId(), language);
                            }
                    );
                }
        );
    }

    @Override
    public ResponseEntity<String> verifyConsistency()
    {
        try
        {
            textStorage.verifyConsistency();
            return ResponseEntity.status(OK).build();
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> clear(String languageName)
    {
        if (languageName.replaceAll("\"", "").equals("All"))
        {
            textStorage.clearAll();
            episodeRepository.deleteAll();
            resourceRepository.deleteAll();
            languageRepository.deleteAll();
            return ResponseEntity.status(OK).build();
        }

        System.out.println("barak: Language name is " + languageName);
        Language language = languageRepository.findByName(languageName);
        textStorage.clear(language.getId());

        return ResponseEntity.status(OK).build();
    }
}