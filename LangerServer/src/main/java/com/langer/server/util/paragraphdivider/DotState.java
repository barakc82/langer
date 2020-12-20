package com.langer.server.util.paragraphdivider;

import java.util.List;

public class DotState implements State
{
    private final String        accumulatedCharacters;
    private final List<String>  sentences;

    public DotState(String accumulatedCharacters, List<String> sentences)
    {
        this.accumulatedCharacters  = accumulatedCharacters;
        this.sentences              = sentences;
    }

    @Override
    public State process(char c)
    {
        if (c == '¿' || c == 191)
            System.out.println("barak: before question mark " + accumulatedCharacters);
        if (Character.isAlphabetic(c) || Character.isDigit(c))
        {
            sentences.add(accumulatedCharacters + '.');
            return new BuildingCharsToSentenceState(String.valueOf(c), sentences);
        }

        switch (c)
        {
            case ' ':
            case '-':
            case '—':
            case '"':
            case '»':
            case '¿':
                sentences.add(accumulatedCharacters);
                return new BuildingCharsToSentenceState("", sentences);

            case '.':
                return new MultipleDotsState(accumulatedCharacters + c, sentences);

            case '[':
                sentences.add(accumulatedCharacters);
                return new FootnoteState(sentences);
        }

        System.out.println("Not handled in dot state: " + c + " " + Integer.valueOf(c));
        return null;
    }

    @Override
    public void processEndOfParagraph() {}
}