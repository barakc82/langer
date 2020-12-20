package com.langer.server.util.paragraphdivider;

import java.util.List;

public class MultipleDotsState implements State
{
    private final String        accumulatedCharacters;
    private final List<String>  sentences;

    public MultipleDotsState(String accumulatedCharacters, List<String> sentences)
    {
        this.accumulatedCharacters  = accumulatedCharacters;
        this.sentences              = sentences;
    }

    @Override
    public State process(char c)
    {
        if (Character.isAlphabetic(c) || Character.isDigit(c))
        {
            sentences.add(accumulatedCharacters);
            return new BuildingCharsToSentenceState(String.valueOf(c), sentences);
        }

        switch (c)
        {
            case '-':
            case '—':
            case '¿':
                return new BuildingCharsToSentenceState(accumulatedCharacters + c, sentences);

            case ' ':
            case '.':
            case ',':
                return new MultipleDotsState(accumulatedCharacters + c, sentences);

            case '?':
                return new QuestionMarkState(accumulatedCharacters + c, sentences);
        }

        System.out.println("Not handled in multiple dot state: " + c + " " + Integer.valueOf(c));
        return null;
    }

    @Override
    public void processEndOfParagraph() {}
}