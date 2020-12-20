package com.langer.server.service.importancecalculators;

import com.langer.server.service.ImportanceCalculator;
import com.langer.server.service.SentencePart;
import com.langer.server.util.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PortugueseImportanceCalculator implements ImportanceCalculator
{
    @Override
    public boolean isInterestingSentence(List<SentencePart> sentenceParts)
    {
        if (!ImportanceCalculator.isInterestingSentenceStructure(sentenceParts))
            return false;

        List<SentencePart> sentenceWords = sentenceParts.stream().filter(sentencePart ->
                !TextUtils.isAllNotAlphanumeric(sentencePart)).collect(Collectors.toList());

        if (sentenceParts.get(0).getOriginalWordValue().equals("E") &&
            sentenceParts.get(1).getOriginalWordValue().equals("então?")) {
            System.out.println("barak: " + sentenceWords.size());
            System.out.println("barak: " + sentenceWords.get(0).getWordOccurrence().getWord().getValue());
            System.out.println("barak: " + sentenceWords.get(1).getWordOccurrence().getWord().getValue());
        }

        if (sentenceWords.size() == 2)
        {
            if (Arrays.asList("um", "os", "e").contains(sentenceWords.get(0).getWordOccurrence().getWord().getValue()))
                return false;

            if (Arrays.asList("да", "вот", "сам").contains(sentenceWords.get(1).getWordOccurrence().getWord().getValue()))
                return false;
        }

        return true;
    }
}