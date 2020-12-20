package com.langer.server.util;

import com.langer.server.service.SentencePart;
import com.langer.server.util.paragraphdivider.BuildingCharsToSentenceState;
import com.langer.server.util.paragraphdivider.State;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TextUtils
{
    public static boolean isNotAlphabetic(String paragraphText)
    {
        return paragraphText.chars().noneMatch(c -> Character.isAlphabetic(c));
    }

    public static String cleanWord(String wordValue)
    {
        String cleanWord = cleanPunctuation(wordValue.trim());

        boolean notAlphabetic = TextUtils.isNotAlphabetic(cleanWord);
        if (notAlphabetic)
            return "";

        return cleanWord;
    }

    private static String cleanPunctuation(String wordValue)
    {
        return wordValue
                .replaceFirst("^—+", "")
                .replaceFirst("^-+", "")
                .replaceAll("—+\\.$", "")
                .replaceAll("-+\\.$", "")
                .replaceAll("\\?", "")
                .replaceAll("\\!", "")
                .replaceAll("\\.", "")
                .replaceAll("\\,", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("\"", "")
                .replaceAll("«", "")
                .replaceAll("»", "")
                .replaceAll(":", "")
                .toLowerCase();
    }

    public static String cleanSentence(String sentenceText)
    {
        if (sentenceText.startsWith("– ") || sentenceText.startsWith("— ") )
            sentenceText = sentenceText.substring(2);
        if (sentenceText.startsWith("«") && !sentenceText.contains("»"))
            sentenceText = sentenceText.substring(1);
        return sentenceText.trim();
    }

    public static boolean isAllNotAlphanumeric(SentencePart sentencePart)
    {
        return sentencePart.getOriginalWordValue().chars().allMatch(c -> !Character.isAlphabetic(c) && !Character.isDigit(c));
    }

    public static boolean isNumber(SentencePart sentencePart)
    {
        String cleanWord = cleanPunctuation(sentencePart.getOriginalWordValue());

        try {
            Integer.parseInt(cleanWord);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static List<String> divideParagraphTextToSentences(String paragraphText)
    {
        List<String> sentences = new ArrayList<>();
        State state = new BuildingCharsToSentenceState("", sentences);
        for (char c : paragraphText.toCharArray())
            state = state.process(c);

        state.processEndOfParagraph();

        return sentences.stream().map(sentence -> sentence.trim()).collect(Collectors.toList());
    }

    public static List<String> divideEpisodeTextToParagraphs(String episodeText)
    {
        String[] paragraphCandidates = episodeText.split("\n");
        List<String> paragraphCandidateList = Collections.arrayToList(paragraphCandidates);
        Iterator<String> it = paragraphCandidateList.iterator();
        String currentParagraph = "";
        String candidate        = "";
        List<String> paragraphs = new ArrayList<>();

        while (it.hasNext())
        {
            candidate = it.next();
            candidate = candidate.replaceAll("\r","");
            if (currentParagraph.endsWith("–")
                    || currentParagraph.endsWith(",")
                    || currentParagraph.endsWith(":")
                    || currentParagraph.isEmpty())
            {
                String old = currentParagraph;
                currentParagraph = String.join(currentParagraph, candidate);
            }
            else
            {
                paragraphs.add(String.valueOf(currentParagraph));
                currentParagraph = candidate;
            }
        }
        if (!candidate.isEmpty())
            paragraphs.add(candidate);

        return paragraphs;
    }
}