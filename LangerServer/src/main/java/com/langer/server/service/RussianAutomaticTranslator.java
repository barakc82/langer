package com.langer.server.service;

import com.langer.server.api.admin.dto.WordOccurrenceDto;
import com.langer.server.util.TextUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RussianAutomaticTranslator implements AutomaticTranslator
{
    private final TextStorage   textStorage;
    private final long          languageId;

    RussianAutomaticTranslator(TextStorage textStorage, long languageId)
    {
        this.textStorage = textStorage;
        this.languageId  = languageId;
    }

    @Override
    public List<WordOccurrenceDto> translate(List<SentencePart> sentenceParts)
    {
/*        return sentenceParts.stream().map(sentencePart ->
        {
            WordOccurrenceDto wordOccurrence = sentencePart.getWordOccurrence();
            String wordValue = wordOccurrence == null ? TextUtils.cleanWord(sentencePart.getOriginalWordValue()) : wordOccurrence.getWord().getValue();

            if (wordValue.equals("а"))
            {
                if (sentencePart.getOriginalWordValue().endsWith(","))
                    return textStorage.setTranslation(sentencePart, "Ah", languageId);
            }

            return null;
        })
        .filter(wordOccurrenceDto -> wordOccurrenceDto != null)
        .collect(Collectors.toList());
    */
        return null;
    }
}