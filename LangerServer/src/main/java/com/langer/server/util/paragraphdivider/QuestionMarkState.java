package com.langer.server.util.paragraphdivider;

import java.util.List;

public class QuestionMarkState implements State
{
    private final String        accumulatedCharacters;
    private final List<String>  sentences;

    public QuestionMarkState(String accumulatedCharacters, List<String> sentences)
    {
        this.accumulatedCharacters  = accumulatedCharacters;
        this.sentences              = sentences;
    }

    @Override
    public State process(char c)
    {
        if (accumulatedCharacters.contains("Эта каша слишком горячая"))
            System.out.println("barak: question mark state, " + accumulatedCharacters + " " + c);
        switch (c)
        {
            case ' ':
            case '”':
                return new QuestionMarkState(accumulatedCharacters + c, sentences);

            case '-':
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
