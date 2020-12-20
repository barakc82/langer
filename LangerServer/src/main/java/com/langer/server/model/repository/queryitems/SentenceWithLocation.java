package com.langer.server.model.repository.queryitems;

public interface SentenceWithLocation
{
    String  getLanguageName();
    String  getResourceTitle();
    String  getEpisodeTitle();
    int     getParagraphIndex();
    long    getSentenceId();
    String  getSentenceText();
    int     getSentenceIndex();
    String  getTranslation();
    boolean getShouldBeIgnored();
}