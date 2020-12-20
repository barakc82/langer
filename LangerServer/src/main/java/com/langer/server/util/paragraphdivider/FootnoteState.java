package com.langer.server.util.paragraphdivider;

import java.util.List;

public class FootnoteState implements State
{
    private final List<String> sentences;

    public FootnoteState(List<String> sentences)
    {
        this.sentences = sentences;
    }

    @Override
    public State process(char c)
    {
        if (Character.isDigit(c))
        {
            return new FootnoteState(sentences);
        }

        switch (c)
        {
            case ']':
                return new BuildingCharsToSentenceState("", sentences);
        }

        System.out.println("Not handled in dot state: " + c + " " + Integer.valueOf(c));
        return null;
    }

    @Override
    public void processEndOfParagraph()
    {

    }
}