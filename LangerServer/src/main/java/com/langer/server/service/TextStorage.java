package com.langer.server.service;

import com.google.common.collect.*;
import com.langer.server.api.admin.dto.*;
import com.langer.server.api.admin.dto.Dictionary;
import com.langer.server.model.entity.WordTranslationForStorage;
import com.langer.server.model.entity.impl.*;
import com.langer.server.model.repository.*;
import com.langer.server.model.repository.queryitems.SentenceTranslation;
import com.langer.server.model.repository.queryitems.SentenceWithLocation;
import com.langer.server.model.repository.queryitems.WordOccurrenceWithTranslation;
import com.langer.server.sentencegraph.TranslationToSentenceMapping;
import com.langer.server.util.TextUtils;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Service
@RequiredArgsConstructor
public class TextStorage
{
    private static final String             LANGER_STUB_TRANSLATION = "Stub translation";

    private final ParagraphRepository       paragraphRepository;
    private final SentenceRepository        sentenceRepository;
    private final WordRepository            wordRepository;
    private final WordOccurrenceRepository  wordOccurrenceRepository;
    private final WordTranslationRepository wordTranslationRepository;

    private final LangerModelMapper          modeler;
    private final AutomaticTranslatorFactory automaticTranslatorFactory;
    private final AtomicInteger              maintenanceThreadsCount = new AtomicInteger(0);

    @Async
    public void storeEpisodeText(String episodeText, long episodeId, Language language)
    {
        maintenanceThreadsCount.incrementAndGet();
        System.out.println("barak: count is now " + maintenanceThreadsCount.get());

        List<String> paragraphTexts = TextUtils.divideEpisodeTextToParagraphs(episodeText);

        List<Paragraph> paragraphs = IntStream.range(0, paragraphTexts.size())
                .mapToObj(paragraphIndex ->
        {
            String paragraphText = paragraphTexts.get(paragraphIndex);
            boolean isNotAlphabetic = TextUtils.isNotAlphabetic(paragraphText);
            if (isNotAlphabetic)
                return null;

            Paragraph paragraph = new Paragraph();
            paragraph.setEpisodeId(episodeId);
            paragraph.setParagraphIndex(paragraphIndex);
            return paragraph;
        })
                .filter(paragraph -> paragraph != null)
                .collect(Collectors.toList());

        paragraphs = paragraphRepository.saveAll(paragraphs);
        for (Paragraph paragraph : paragraphs)
        {
            String paragraphText = paragraphTexts.get(paragraph.getParagraphIndex());
            storeParagraphText(paragraphText, paragraph.getId(), language);
            double progress = paragraph.getParagraphIndex() * 100 / paragraphs.size();
            log.info("\tDone storing the text of paragraph " + paragraph.getId() + " " + progress + "% (" + language.getName() +")");
        }

        maintenanceThreadsCount.decrementAndGet();
        log.info("Done storing the text of episode " + episodeId);
    }

    private void storeParagraphText(String paragraphText, long paragraphId, Language language)
    {
        List<String> sentenceTexts = TextUtils.divideParagraphTextToSentences(paragraphText);
        Set<String> storedNewWordsOfParagraph = new HashSet<>();
        IntStream.range(0, sentenceTexts.size()).forEach(
                sentenceIndex ->
                {
                    String sentenceText = sentenceTexts.get(sentenceIndex);

                    Sentence sentence = new Sentence();
                    sentence.setParagraphId(paragraphId);
                    sentence.setSentenceIndex(sentenceIndex);
                    sentence.setText(sentenceText);
                    sentence.setShouldBeIgnored(false);
                    sentenceRepository.save(sentence);

                    sentenceText = TextUtils.cleanSentence(sentenceText);
                    storeSentenceText(language, storedNewWordsOfParagraph, sentenceText, sentence);
                }
        );
    }

    private synchronized void storeSentenceText(Language language, Set<String> storedNewWordsOfParagraph, String sentenceText, Sentence sentence)
    {
        List<SentencePart> sentenceParts = calculateSentenceParts(sentence.getId(), sentenceText, language.getId());
        sentenceParts.stream().forEach(sentencePart ->
        {
            if (sentencePart.getWordOccurrence() == null)
                return;

            WordDto word = sentencePart.getWordOccurrence().getWord();
            if (word.getId() == 0 && !storedNewWordsOfParagraph.contains(word.getValue()))
            {
                storeWord(TextUtils.cleanWord(sentencePart.getOriginalWordValue()), language.getId());
                storedNewWordsOfParagraph.add(word.getValue());
            }
        });
        //storeWordOccurrences(sentenceParts, sentence.getId(), language);
    }

    private void storeWordOccurrences(List<SentencePart> sentenceParts, long sentenceId, Language language)
    {
        AutomaticTranslator automaticTranslator = automaticTranslatorFactory.create(this, language);
        if (automaticTranslator == null)
            return;

        List<WordOccurrenceDto> translatedWordOccurrences = automaticTranslator.translate(sentenceParts);
        translatedWordOccurrences.forEach(translatedWordOccurrence ->
                {
                    WordTranslationDto wordTranslationDto = translatedWordOccurrence.getWordTranslation();
                    WordTranslation wordTranslation = wordTranslationRepository.findByWordIdAndTranslation(translatedWordOccurrence.getWord().getId(), wordTranslationDto.getTranslation());

                    if (wordTranslation == null)
                    {
                        wordTranslation = new WordTranslation();
                        wordTranslation.setWordId(translatedWordOccurrence.getWord().getId());
                        wordTranslation.setTranslation(wordTranslationDto.getTranslation());
                        wordTranslation = wordTranslationRepository.save(wordTranslation);
                    }
                    WordOccurrence wordOccurrence = new WordOccurrence();
                    wordOccurrence.setSentenceId(sentenceId);
                    wordOccurrence.setWordTranslationId(wordTranslation.getId());
                    wordOccurrence.setWordTranslationType(WordTranslationType.AUTOMATIC);
                    wordOccurrenceRepository.save(wordOccurrence);
                }
        );
    }

    public List<SentencePart> calculateSentenceParts(List<SentenceWithLocation> sentences, long languageId)
    {
        List<WordOccurrenceWithTranslation> wordOccurrencesWithTranslations = wordOccurrenceRepository.findAllByLanguageId(languageId);
        final Map<Long, List<WordOccurrenceWithTranslation>> sentenceToWordOccurrences = wordOccurrencesWithTranslations.stream()
                .collect(Collectors.groupingBy(WordOccurrenceWithTranslation::getSentenceId));

        List<WordTranslationForStorage> wordTranslations = wordTranslationRepository.findByLanguageId(languageId);
        Map<String, List<WordTranslationForStorage>> wordValueToTranslations = wordTranslations.stream()
                .collect((Collectors.groupingBy(WordTranslationForStorage::getWordValue)));

        for (SentenceWithLocation sentence : sentences)
        {
            String[] sentenceTextWords = sentence.getSentenceText().split(" ");
            List<SentencePart> sentenceParts = Arrays.stream(sentenceTextWords)
                    .filter(sentenceTextWord -> sentenceTextWord != null && !sentenceTextWord.isEmpty())
                    .map(sentenceTextWord ->
                    {
                        SentencePart sentencePart = new SentencePart();
                        sentencePart.setOriginalWordValue(sentenceTextWord.trim());
                        return sentencePart;
                    }).collect(Collectors.toList());

            List<SentencePart> sentenceWords = sentenceParts.stream()
                    .filter(sentencePart -> !TextUtils.cleanWord(sentencePart.getOriginalWordValue()).isEmpty())
                    .collect(Collectors.toList());

            wordOccurrencesWithTranslations.forEach(wordOccurrenceWithTranslation ->
                    {
                        SentencePart sentencePart = sentenceWords.get(wordOccurrenceWithTranslation.getWordPosition());

                        WordTranslationDto wordTranslation = WordTranslationDto.builder()
                                .id(wordOccurrenceWithTranslation.getWordTranslationId())
                                .translation(wordOccurrenceWithTranslation.getWordTranslation())
                                .build();

                        WordDto word = WordDto.builder()
                                .id(wordOccurrenceWithTranslation.getWordId())
                                .value(wordOccurrenceWithTranslation.getWordValue())
                                .translations(Arrays.asList(wordTranslation))
                                .type(WordType.NA)
                                .build();

                        WordOccurrenceDto wordOccurrence = WordOccurrenceDto.builder()
                                .id(wordOccurrenceWithTranslation.getWordOccurrenceId())
                                .word(word)
                                .build();

                        sentencePart.setWordOccurrence(wordOccurrence);
                    }
            );

            for (SentencePart sentencePart : sentenceWords)
            {
                if (sentencePart.getWordOccurrence() != null)
                    continue;

                String wordValue = TextUtils.cleanWord(sentencePart.getOriginalWordValue());
                List<WordTranslationForStorage> curWordTranslations = wordValueToTranslations.get(wordValue);

                WordTranslationDto wordTranslationDto = WordTranslationDto.builder().translation(LANGER_STUB_TRANSLATION).build();
                WordDto wordDto = WordDto.builder()
                        .value(wordValue)
                        .type(WordType.NA)
                        .translations(Arrays.asList(wordTranslationDto))
                        .build();

                if (curWordTranslations != null)
                {
                    WordTranslationForStorage curWordTranslation = curWordTranslations.get(0);
                    wordDto.setId(curWordTranslation.getWordId());
                    wordDto.setType(curWordTranslation.getWordType());
                    List<WordTranslationDto> wordTranslationDtos = curWordTranslations.stream()
                            .map(translation -> WordTranslationDto.builder()
                                    .id(translation.getWordTranslationId())
                                    .translation(translation.getTranslation())
                                    .build())
                            .collect(Collectors.toList());
                    wordDto.setTranslations(wordTranslationDtos);
                }

                WordOccurrenceDto wordOccurrence = WordOccurrenceDto.builder()
                        .word(wordDto)
                        .build();

                sentencePart.setWordOccurrence(wordOccurrence);
            }
        }

        return null;
    }

    public List<SentencePart> calculateSentenceParts(long sentenceId, String sentenceText, long languageId)
    {
        List<WordOccurrenceWithTranslation> wordOccurrencesWithTranslations = wordOccurrenceRepository.findAllBySentenceId(sentenceId);

        String[] sentenceTextWords = sentenceText.split(" ");
        List<SentencePart> sentenceParts = Arrays.stream(sentenceTextWords)
                .filter(sentenceTextWord -> sentenceTextWord != null && !sentenceTextWord.isEmpty())
                .map(sentenceTextWord ->
        {
            SentencePart sentencePart = new SentencePart();
            sentencePart.setOriginalWordValue(sentenceTextWord.trim());
            return sentencePart;
        }).collect(Collectors.toList());

        List<SentencePart> sentenceWords = sentenceParts.stream()
                .filter(sentencePart -> !TextUtils.cleanWord(sentencePart.getOriginalWordValue()).isEmpty())
                .collect(Collectors.toList());

        wordOccurrencesWithTranslations.forEach(wordOccurrenceWithTranslation ->
                {
                    SentencePart sentencePart = sentenceWords.get(wordOccurrenceWithTranslation.getWordPosition());

                    WordTranslationDto wordTranslation = WordTranslationDto.builder()
                            .id(wordOccurrenceWithTranslation.getWordTranslationId())
                            .translation(wordOccurrenceWithTranslation.getWordTranslation())
                            .build();

                    WordDto word = WordDto.builder()
                            .id(wordOccurrenceWithTranslation.getWordId())
                            .value(wordOccurrenceWithTranslation.getWordValue())
                            .translations(Arrays.asList(wordTranslation))
                            .type(WordType.NA)
                            .build();

                    WordOccurrenceDto wordOccurrence = WordOccurrenceDto.builder()
                            .id(wordOccurrenceWithTranslation.getWordOccurrenceId())
                            .word(word)
                            .build();

                    sentencePart.setWordOccurrence(wordOccurrence);
                }
        );

        sentenceWords.stream().forEach(sentencePart ->
        {
            if (sentencePart.getWordOccurrence() != null)
                return;

            String wordValue = TextUtils.cleanWord(sentencePart.getOriginalWordValue());
            Word word = wordRepository.findByValueAndLanguageId(wordValue, languageId);

            WordTranslationDto wordTranslationDto = WordTranslationDto.builder().translation(LANGER_STUB_TRANSLATION).build();
            WordDto wordDto = WordDto.builder()
                    .value(wordValue)
                    .type(WordType.NA)
                    .translations(Arrays.asList(wordTranslationDto))
                    .build();

            if (word != null)
            {
                wordDto = modeler.toDto(word);
                List<WordTranslation> translations = wordTranslationRepository.findAllByWordId(word.getId());
                List<WordTranslationDto> wordTranslationDtos = translations.stream().map(translation -> modeler.toDto(translation)).collect(Collectors.toList());
                wordDto.setTranslations(wordTranslationDtos);
            }

            WordOccurrenceDto wordOccurrence = WordOccurrenceDto.builder()
                    .word(wordDto)
                    .build();

            sentencePart.setWordOccurrence(wordOccurrence);
        });

        return sentenceParts;
    }

    private Word storeWord(String wordValue, long languageId)
    {
        if (wordValue.equals("внезапным"))
            System.out.println("barak: storing the russian special word");
        Assert.isTrue(!wordValue.isEmpty(), "Attempting to store an empty word value");
        Word word = new Word();
        word.setValue(wordValue);
        word.setLanguageId(languageId);
        word.setType(WordType.NA);
        word = wordRepository.saveAndFlush(word);

        if (wordValue.equals("внезапным"))
            System.out.println("barak: and now storing the translation of the russian special word " + word.getId());

        WordTranslation wordTranslation = new WordTranslation();
        wordTranslation.setWordId(word.getId());
        wordTranslation.setTranslation(LANGER_STUB_TRANSLATION);
        wordTranslationRepository.save(wordTranslation);

        return word;
    }

    public FindSentenceResponse findSentence(FindSentenceRequest findSentenceRequest, Language language, Resource resource, Episode episode)
    {
        Paragraph               paragraph       = paragraphRepository.findByEpisodeIdAndParagraphIndex(episode.getId(), findSentenceRequest.getParagraphIndex());
        Sentence                sentence        = sentenceRepository.findByParagraphIdAndSentenceIndex(paragraph.getId(), findSentenceRequest.getSentenceIndex());

        List<SentencePart> sentenceParts = calculateSentenceParts(sentence.getId(), sentence.getText(), language.getId());

        List<WordOccurrenceDto> wordOccurrenceDtos = sentenceParts.stream()
                .map(sentencePart ->
                {
                    if (sentencePart.getWordOccurrence() == null || sentencePart.getWordOccurrence().getWord().getType() == WordType.Name)
                        return null;

                    if (sentencePart.getWordOccurrence().getWord().getTranslations() == null)
                    {
                        log.warn("Word " + sentencePart.getWordOccurrence().getWord().getId() + " has no available translation");
                        return null;
                    }

            return sentencePart.getWordOccurrence();
        })
                .filter(wordOccurrenceDto -> wordOccurrenceDto != null)
                .collect(Collectors.toList());

        SentenceDto sentenceDto = SentenceDto.builder()
                .id(sentence.getId())
                .text(sentence.getText())
                .wordOccurrences(wordOccurrenceDtos)
                .languageName(findSentenceRequest.getLanguageName())
                .resourceTitle(findSentenceRequest.getResourceTitle())
                .episodeTitle(findSentenceRequest.getEpisodeTitle())
                .translation(sentence.getTranslation())
                .build();

        FindSentenceResponse findSentenceResponse = new FindSentenceResponse();
        findSentenceResponse.setSentence(sentenceDto);
        return findSentenceResponse;
    }

    public WordDto getWord(long id)
    {
        Word word = wordRepository.findById(id).get();
        WordDto wordDto = modeler.toDto(word);
        List<WordTranslation> wordTranslations = wordTranslationRepository.findAllByWordId(id);
        List<WordTranslationDto> translations = wordTranslations.stream().map(wordTranslation ->
                WordTranslationDto.builder()
                        .id(wordTranslation.getId())
                        .translation(wordTranslation.getTranslation())
                        .build()
        ).collect(Collectors.toList());
        wordDto.setTranslations(translations);
        return wordDto;
    }

    public void deleteWord(long id)
    {
        wordRepository.deleteById(id);
    }

    public void createWordTranslation(CreateWordTranslationRequest createWordTranslationRequest)
    {
        long    wordId         = createWordTranslationRequest.getWordId();
        String  newTranslation = createWordTranslationRequest.getTranslation();

        List<WordTranslation> translations = wordTranslationRepository.findAllByWordId(wordId);
        if (translations.stream().anyMatch(translation -> translation.getTranslation().equals(newTranslation)))
            throw new IllegalArgumentException("Translation " + newTranslation + " already exists");

        WordTranslation stubTranslation = translations.stream()
                .filter(translation -> translation.getTranslation().equals(LANGER_STUB_TRANSLATION))
                .findAny().get();

        if (stubTranslation != null)
            wordTranslationRepository.delete(stubTranslation);

        WordTranslation wordTranslation = new WordTranslation();
        wordTranslation.setWordId(wordId    );
        wordTranslation.setTranslation(createWordTranslationRequest.getTranslation());
        wordTranslationRepository.save(wordTranslation);
    }

    public WordWithTranslation getWordWithTranslation(long wordTranslationId)
    {
        WordTranslation wordTranslation = wordTranslationRepository.findById(wordTranslationId).get();
        Word            word            = wordRepository.findById(wordTranslation.getWordId()).get();
        return WordWithTranslation.builder()
                .word(modeler.toDto(word))
                .wordTranslation(modeler.toDto(wordTranslation))
                .build();
    }

    public void setWordOccurrenceTranslation(long wordOccurrenceId,
                                             SetWordOccurrenceTranslationRequest setWordOccurrenceTranslationRequest)
    {
        WordOccurrence wordOccurrence = null;
        if (wordOccurrenceId == 0)
        {
            wordOccurrence = new WordOccurrence();
            wordOccurrence.setSentenceId(setWordOccurrenceTranslationRequest.getSentenceId());
            wordOccurrence.setPosition(setWordOccurrenceTranslationRequest.getWordPosition());
        }
        else
            wordOccurrence = wordOccurrenceRepository.findById(wordOccurrenceId).get();

        wordOccurrence.setWordTranslationId(setWordOccurrenceTranslationRequest.getWordTranslationId());
        wordOccurrence.setWordTranslationType(WordTranslationType.MANUAL);
        wordOccurrenceRepository.save(wordOccurrence);
    }

    public List<SentenceWithLocation> findSentences(long languageId)
    {
        return sentenceRepository.findAllByLanguageId(languageId);
    }

    /*
    public WordOccurrenceDto setTranslation(SentencePart sentencePart, String translation, long languageId)
    {
        WordOccurrenceDto wordOccurrenceDto = sentencePart.getWordOccurrence();
        if (wordOccurrenceDto.getWord().getTranslations() == null)
        {
            String wordValue = TextUtils.cleanWord(sentencePart.getOriginalWordValue());
            Word word = wordRepository.findByValueAndLanguageId(wordValue, languageId);
            WordDto wordDto = modeler.toDto(word);

            List<WordTranslation> wordTranslations = wordTranslationRepository.findAllByWordId(word.getId());
            List<WordTranslationDto> wordTranslationsDtos = wordTranslations.stream().map(wordTranslation -> modeler.toDto(wordTranslation)).collect(Collectors.toList());
            wordDto.setTranslations(wordTranslationsDtos);

            OptionalInt optionalSelectedTranslationIndex = IntStream.range(0, wordTranslationsDtos.size())
                    .filter((i -> wordTranslationsDtos.get(i).getTranslation().equals(translation)))
                    .findAny();

            if (!optionalSelectedTranslationIndex.isPresent())
                return null;

            int selectedTranslationIndex = optionalSelectedTranslationIndex.getAsInt();
             
            wordOccurrenceDto = WordOccurrenceDto.builder()
                    .word(wordDto)
                    .selectedTranslationIndex(selectedTranslationIndex)
                    .build();

            sentencePart.setWordOccurrence(wordOccurrenceDto);
        }

        wordOccurrenceDto.setTranslation(translation);
        return wordOccurrenceDto;
    }
*/
    public void clear(long languageId)
    {
        //paragraphRepository.deleteByLanguageId(languageId);
        //sentenceRepository.deleteByLanguageId(languageId);
        wordTranslationRepository.deleteByLanguageId(languageId);
        wordOccurrenceRepository.deleteByLanguageId(languageId);
        wordRepository.deleteByLanguageId(languageId);
    }

    public void clearAll()
    {
        paragraphRepository.deleteAll();
        sentenceRepository.deleteAll();
        wordOccurrenceRepository.deleteAll();
        wordRepository.deleteAll();
        wordTranslationRepository.deleteAll();
    }

    public void verifyConsistency()
    {
        List<Paragraph> paragraphs = paragraphRepository.findAll();
        Set<Long> paragraphIds = paragraphs.stream().map(Paragraph::getId).collect(Collectors.toSet());

        List<Sentence> sentences = sentenceRepository.findAll();
        for (Sentence sentence : sentences)
        {
            if (!paragraphIds.contains(sentence.getParagraphId()))
            {
                //sentenceRepository.delete(sentence);
                throw new IllegalArgumentException("Sentence not found, ID: " + sentence.getId() + ", " + sentence.getText() + ", paragraph ID: " + sentence.getParagraphId());
            }
        }

        List<Word> words = wordRepository.findAllWordsWithSameValueAndLanguageId();
        if (!words.isEmpty())
            throw new IllegalArgumentException("Duplicate word: " + words.get(0).getValue());

        List<WordTranslation> wordTranslations = wordTranslationRepository.findAllWordTranslationsWithSameWord();
        if (!wordTranslations.isEmpty())
            throw new IllegalArgumentException("Duplicate word translation for word ID " + wordTranslations.get(0).getWordId());
    }

    public void updateSentenceTranslation(long id, String translation)
    {
        Sentence sentence = sentenceRepository.findById(id).get();
        sentence.setTranslation(translation);
        sentenceRepository.save(sentence);
    }

    public void updateSentenceShouldBeIgnored(long id, boolean shouldBeIgnored)
    {
        Sentence sentence = sentenceRepository.findById(id).get();
        sentence.setShouldBeIgnored(shouldBeIgnored);
        sentenceRepository.save(sentence);
    }

    public List<Dictionary> getDictionaries(Map<Long, Language> idToLanguage)
    {
        List<Word> allTypedWords = wordRepository.findByTypeNotLike(WordType.NA);
        ImmutableListMultimap<Long, Word> languageIdToTypedWords = Multimaps.index(allTypedWords, Word::getLanguageId);

        List<WordTranslationForStorage> wordTranslationsForStorage = wordTranslationRepository.findAllTranslations();
        ImmutableListMultimap<Long, WordTranslationForStorage> languageIdToWordTranslations = Multimaps.index(wordTranslationsForStorage, WordTranslationForStorage::getLanguageId);

        List<SentenceTranslation> sentenceTranslations = sentenceRepository.findAllTranslations();
        ImmutableListMultimap<Long, SentenceTranslation> languageIdToSentenceTranslations = Multimaps.index(sentenceTranslations, SentenceTranslation::getLanguageId);

        ImmutableList<Long> languageIds = ImmutableSet.<Long>builder()
                .addAll(languageIdToTypedWords.keySet())
                .addAll(languageIdToWordTranslations.keySet())
                .addAll(languageIdToSentenceTranslations.keySet())
                .build().asList();

        List<Dictionary> dictionaries = languageIds.stream().map(languageId ->
        {
            ImmutableList<Word> typedWords = languageIdToTypedWords.get(languageId);
            Map<String, DictionaryWord> wordValueToDictionaryWord = typedWords.stream().collect(Collectors.toMap(Word::getValue,
                    word -> DictionaryWord.builder().value(word.getValue()).type(word.getType()).build()));

            ImmutableList<WordTranslationForStorage> wordTranslations = languageIdToWordTranslations.get(languageId);
            for (WordTranslationForStorage wordTranslation : wordTranslations)
            {
                DictionaryWord word = wordValueToDictionaryWord.get(wordTranslation.getWordValue());
                if (word == null)
                    word = DictionaryWord.builder().value(wordTranslation.getWordValue()).type(WordType.NA).build();

                word.getTranslations().add(wordTranslation.getTranslation());
                wordValueToDictionaryWord.put(word.getValue(), word);
            }

            List<DictionaryWord> words = wordValueToDictionaryWord.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

            ImmutableList<SentenceTranslation> sentenceTranslationsOfLanguage = languageIdToSentenceTranslations.get(languageId);
            List<DictionarySentence> sentences = sentenceTranslationsOfLanguage.stream()
                    .map(sentenceTranslation ->
                            DictionarySentence.builder()
                                    .text(sentenceTranslation.getText())
                                    .translation(sentenceTranslation.getTranslation())
                                    .build())
                    .collect(Collectors.toList());


            List<Sentence> sentencesToIgnore = sentenceRepository.findByShouldBeIgnoredTrue();
            List<String> sentenceTextsToIgnore = sentencesToIgnore.stream().map(sentence -> sentence.getText()).collect(Collectors.toList());

            return Dictionary.builder()
                    .languageName(idToLanguage.get(languageId).getName())
                    .words(words)
                    .sentences(sentences)
                    .sentencesToIgnore(sentenceTextsToIgnore)
                    .build();

        }).collect(Collectors.toList());

        return dictionaries;
    }

    public void updateWordType(long wordId, WordType type)
    {
        Word word = wordRepository.findById(wordId).get();
        word.setType(type);
        wordRepository.save(word);
    }

    public void applyDictionary(Dictionary dictionary, long languageId)
    {
        log.info("Applying dictionary for " + dictionary.getLanguageName());
        for (DictionaryWord dictionaryWord : dictionary.getWords())
        {
            Word word = null;
            if (dictionaryWord.getType() != WordType.NA)
            {
                word = wordRepository.findByValueAndLanguageId(dictionaryWord.getValue(), languageId);
                word.setType(dictionaryWord.getType());
                wordRepository.save(word);
            }

            for (String translation : dictionaryWord.getTranslations())
            {
                if (word == null)
                    word = wordRepository.findByValueAndLanguageId(dictionaryWord.getValue(), languageId);

                CreateWordTranslationRequest createWordTranslationRequest = new CreateWordTranslationRequest();
                createWordTranslationRequest.setWordId(word.getId());
                createWordTranslationRequest.setTranslation(translation);
                createWordTranslation(createWordTranslationRequest);
            }
        }

        for (DictionarySentence dictionarySentence : dictionary.getSentences())
        {
            List<Sentence> sentences = sentenceRepository.findByTextAndLanguageId(dictionarySentence.getText(), languageId);
            for (Sentence sentence : sentences)
            {
                sentence.setTranslation(dictionarySentence.getTranslation());
                sentenceRepository.save(sentence);
            }
        }

        for (String sentenceTextToIgnore : dictionary.getSentencesToIgnore())
        {
            List<Sentence> sentences = sentenceRepository.findByTextAndLanguageId(sentenceTextToIgnore, languageId);
            for (Sentence sentence : sentences)
            {
                sentence.setShouldBeIgnored(true);
                sentenceRepository.save(sentence);
            }
        }

        log.info("The Dictionary of " + dictionary.getLanguageName() + " applied successfully");
    }

    public boolean isUnderMaintenance()
    {
        log.info("barak: is maintenance count = " + maintenanceThreadsCount.get());
        return maintenanceThreadsCount.get() > 0;
    }

    public TranslationToSentenceMapping getTranslationToSentenceMapping(long languageId)
    {
        TranslationToSentenceMapping wordSentenceMapping = new TranslationToSentenceMapping();
        List<SentenceWithLocation> sentences = sentenceRepository.findAllByLanguageId(languageId);

        calculateSentenceParts(sentences, languageId);

        for (SentenceWithLocation sentence : sentences)
        {
            if (sentence.getShouldBeIgnored())
                continue;

            List<SentencePart> sentenceParts =
                    calculateSentenceParts(sentence.getSentenceId(), sentence.getSentenceText(), languageId);

            for (SentencePart sentencePart : sentenceParts)
            {
                if (sentencePart.getWordOccurrence() == null)
                    continue;

                WordDto word = sentencePart.getWordOccurrence().getWord();
                if (word.getType() == WordType.Name)
                    continue;

                if (word.getTranslations() == null || word.getTranslations().isEmpty())
                {
                    log.warn("Word " + word.getId() + " has no available translation");
                    continue;
                }

                WordTranslationDto wordTranslation = sentencePart.getWordOccurrence().getWordTranslation();

                long wordTranslationId = wordTranslation.getId();
                if (wordTranslationId == 0)
                    log.warn("barak: wordTranslationId is 0");
                wordSentenceMapping.link(wordTranslationId, sentence.getSentenceId());
            }
        }

        return wordSentenceMapping;
    }
}