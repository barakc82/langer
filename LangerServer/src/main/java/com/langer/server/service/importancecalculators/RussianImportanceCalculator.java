package com.langer.server.service.importancecalculators;

import com.langer.server.api.admin.dto.WordOccurrenceDto;
import com.langer.server.service.ImportanceCalculator;
import com.langer.server.service.SentencePart;
import com.langer.server.util.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RussianImportanceCalculator implements ImportanceCalculator
{
    @Override
    public boolean isInterestingSentence(List<SentencePart> sentenceParts)
    {
        if (!ImportanceCalculator.isInterestingSentenceStructure(sentenceParts))
            return false;

        List<SentencePart> sentenceWords = sentenceParts.stream().filter(sentencePart ->
                !TextUtils.isAllNotAlphanumeric(sentencePart)).collect(Collectors.toList());

        if (sentenceWords.size() == 2)
        {
            if (Arrays.asList("а", "и", "да").contains(sentenceWords.get(0).getWordOccurrence().getWord().getValue()))
                return false;

            if (Arrays.asList("да", "вот", "сам").contains(sentenceWords.get(1).getWordOccurrence().getWord().getValue()))
                return false;
        }

        return true;
    }
}