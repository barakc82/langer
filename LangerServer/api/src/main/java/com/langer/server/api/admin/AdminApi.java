package com.langer.server.api.admin;


import com.langer.server.api.admin.dto.*;
import com.langer.server.api.data.dto.UserLanguageProgress;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping(path = "/admin")
public interface AdminApi
{
    String ADMIN_API_LANGUAGE =                     "language";
    String ADMIN_API_RESOURCE =                     "resource";
    String ADMIN_API_EPISODE  =                     "episode";
    String ADMIN_API_SENTENCE =                     "sentence";
    String ADMIN_API_WORD_OCCURRENCE =              "word-occurrence";
    String ADMIN_API_WORD     =                     "word";
    String ADMIN_API_WORD_TRANSLATION =             "word-translation";
    String ADMIN_API_USER                           = "user";
    String RESTORE_FROM_EPISODES    =               "restore-from-episodes";
    String VERIFY_CONSISTENCY       =               "verify-consistency";
    String CLEAR_DATA               =               "clear-data";
    String ADMIN_API_DICTIONARIES   =               "dictionaries";
    String ADMIN_API_MAINTENANCE    =               "mainte nance";

    @RequestMapping(method = RequestMethod.GET, path = ADMIN_API_LANGUAGE + "/{name}")
    LanguageDto getLanguage(@PathVariable("name") String name);

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_LANGUAGE, consumes = MediaType.APPLICATION_JSON_VALUE)
    long createLanguage(@RequestBody LanguageDto language);

    @RequestMapping(method = RequestMethod.GET, path = ADMIN_API_RESOURCE)
    List<ResourceDto> getResources(@RequestParam(name = "language-id") long languageId);

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_RESOURCE, consumes = MediaType.APPLICATION_JSON_VALUE)
    long createResource(@RequestBody ResourceDto resourceDto);

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_RESOURCE + "/find")
    FindResourceResponse findResource(@RequestBody FindResourceRequest findResourceRequest);

    @RequestMapping(method = RequestMethod.DELETE, path = ADMIN_API_RESOURCE + "/{id}")
    void deleteResource(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.GET, path = ADMIN_API_EPISODE)
    List<EpisodeDto> getEpisodes(@RequestParam(name = "resource-id") long resourceId);

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_EPISODE + "/find")
    FindEpisodeResponse findEpisode(@RequestBody FindEpisodeRequest findEpisodeRequest);

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_EPISODE, consumes = MediaType.APPLICATION_JSON_VALUE)
    long createEpisode(@RequestBody EpisodeDto episodeDto);

    @RequestMapping(method = RequestMethod.DELETE, path = ADMIN_API_EPISODE + "/{id}")
    void deleteEpisode(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.PUT, path = ADMIN_API_EPISODE + "/{id}/update-text")
    void updateEpisodeText(@PathVariable("id") long id, @RequestBody String text);

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_SENTENCE + "/find")
    FindSentenceResponse findSentence(@RequestBody FindSentenceRequest findSentenceRequest);

    @RequestMapping(method = RequestMethod.PUT, path = ADMIN_API_SENTENCE + "/{id}/update-translation")
    void updateSentenceTranslation(@PathVariable("id") long id, @RequestBody String sentenceTranslation);

    @RequestMapping(method = RequestMethod.PUT, path = ADMIN_API_SENTENCE + "/{id}/update-should-be-ignored")
    void updateSentenceShouldBeIgnored(@PathVariable("id") long id, @RequestBody boolean shouldBeIgnored);

    @RequestMapping(method = RequestMethod.GET, path = ADMIN_API_WORD + "/{id}")
    WordDto getWord(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.DELETE, path = ADMIN_API_WORD + "/{id}")
    void deleteWord(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.PUT, path = ADMIN_API_WORD + "/{id}/update-type", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateWordType(@PathVariable("id") long id, @RequestBody WordType type);

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_WORD_TRANSLATION)
    void createWordTranslation(@RequestBody CreateWordTranslationRequest createWordTranslationRequest);

    @RequestMapping(method = RequestMethod.PUT, path = ADMIN_API_WORD_OCCURRENCE + "/{id}/set-translation")
    void setWordOccurrenceTranslation(@PathVariable("id") long id,
                                      @RequestBody SetWordOccurrenceTranslationRequest setWordOccurrenceTranslationRequest);

    @RequestMapping(method = RequestMethod.GET, path = ADMIN_API_LANGUAGE + "/{id}/corpus-statistics")
    CorpusStatisticsResponse getCorpusStatistics(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.GET, path = ADMIN_API_DICTIONARIES)
    List<Dictionary> getDictionaries();

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_DICTIONARIES, consumes = MediaType.APPLICATION_JSON_VALUE)
    void applyDictionaries(@RequestBody List<Dictionary> dictionaries);

    @RequestMapping(method = RequestMethod.GET, path = ADMIN_API_MAINTENANCE)
    boolean isUnderMaintenance();

    @RequestMapping(method = RequestMethod.GET, path = ADMIN_API_USER + "/{id}/progress")
    List<UserLanguageProgress> getUserProgress(@PathVariable("id") long userId);

    @RequestMapping(method = RequestMethod.POST, path = ADMIN_API_USER + "/{id}/progress")
    void clearUserProgress(@PathVariable("id") long userId);

    @RequestMapping(method = RequestMethod.POST, path = RESTORE_FROM_EPISODES)
    void restoreFromEpisodeTexts();

    @RequestMapping(method = RequestMethod.POST, path = VERIFY_CONSISTENCY)
    ResponseEntity<String> verifyConsistency();

    @RequestMapping(method = RequestMethod.PUT, path = CLEAR_DATA, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> clear(@RequestBody String languageName);
}