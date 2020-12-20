package com.langer.server.util.paragraphdivider;

import java.util.List;

public class BuildingCharsToSentenceState implements State
{
    private final String        accumulatedCharacters;
    private final List<String>  sentences;

    public BuildingCharsToSentenceState(String accumulatedCharacters, List<String> sentences)
    {
        this.accumulatedCharacters  = accumulatedCharacters;
        this.sentences              = sentences;
    }

    @Override
    public State process(char c)
    {
        if (Character.isAlphabetic(c) || Character.isDigit(c))
            return new BuildingCharsToSentenceState(accumulatedCharacters + c, sentences);

        switch (c)
        {
            case ' ':
            case ',':
            case ':':
            case '¿':
            case '¡':
            case '|':
            case ';':
            case '*':
            case '%':
            case '\\':
            case '/':
            case '²':
            case '(':
            case ')':
            case '[':
            case ']':
            case '«':
            case '»':
            case '"':
            case '“':
            case '”':
            case '’':
            case '́':
            case '´':
            case '\'':
            case '⁃':
            case '-':
            case '–':
            case '—':
                return new BuildingCharsToSentenceState(accumulatedCharacters + c, sentences);

            case '.':
                return new DotState(accumulatedCharacters + c, sentences);

            case '?':
                return new QuestionMarkState(accumulatedCharacters + c, sentences);

            case '!':
                return new ExclamationMarkState(accumulatedCharacters + c, sentences);

            case '…':
                return new MultipleDotsState(accumulatedCharacters + "...", sentences);
        }

        System.out.println("Not handled in building state: " + c + " " + Integer.valueOf(c) + ", " + ((int) accumulatedCharacters.toCharArray()[0]) + " " + accumulatedCharacters);
        return null;
    }

    @Override
    public void processEndOfParagraph() { sentences.add(accumulatedCharacters); }
}