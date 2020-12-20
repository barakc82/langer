package com.langer.server.util.paragraphdivider;

public interface State
{
    State process(char c);

    void processEndOfParagraph();
}