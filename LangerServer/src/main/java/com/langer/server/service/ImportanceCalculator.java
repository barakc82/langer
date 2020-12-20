package com.langer.server.service;

import com.langer.server.api.admin.dto.WordType;
import com.langer.server.util.TextUtils;

import java.util.List;
import java.util.stream.Collectors;

public interface ImportanceCalculator
{
    static boolean isInterestingSentenceStructure(List<SentencePart> sentenceParts)
    {
        List<SentencePart> sentenceWords = sentenceParts.stream().filter(sentencePart ->
                !TextUtils.isAllNotAlphanumeric(sentencePart)).collect(Collectors.toList());

        if (sentenceWords.size() < 2)
            return false;

        if (sentenceWords.size() == 2)
        {
            if (sentenceWords.get(0).getOriginalWordValue().endsWith(","))
            {
                long wordId1 = sentenceWords.get(0).getWordOccurrence().getWord().getId();
                long wordId2 = sentenceWords.get(1).getWordOccurrence().getWord().getId();
                return wordId1 != wordId2;
            }

            if (TextUtils.isNumber(sentenceParts.get(1)))
                return false;
        }

        boolean isAllNames = sentenceWords.stream().allMatch(sentenceWord ->
                        sentenceWord.getWordOccurrence() == null ||
                sentenceWord.getWordOccurrence().getWord().getType() == WordType.Name);

        if (isAllNames)
            return false;

        return true;
    }

    boolean isInterestingSentence(List<SentencePart> sentenceParts);
}