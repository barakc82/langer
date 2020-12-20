package com.langer.server.util.paragraphdivider;

import java.util.List;

public class ExclamationMarkState implements State
{
    private final String        accumulatedCharacters;
    private final List<String>  sentences;

    public ExclamationMarkState(String accumulatedCharacters, List<String> sentences)
    {
        this.accumulatedCharacters  = accumulatedCharacters;
        this.sentences              = sentences;
    }

    @Override
    public State process(char c)
    {
        switch (c)
        {
            case ' ':
            case '¡':
                return new ExclamationMarkState(accumulatedCharacters + c, sentences);

            case '”':
            case '–':
            case '—':
                return new BuildingCharsToSentenceState(accumulatedCharacters + c, sentences);
        }

        sentences.add(accumulatedCharacters);
        return new BuildingCharsToSentenceState(String.valueOf(c), sentences);
    }

    @Override
    public void processEndOfParagraph() {}
}