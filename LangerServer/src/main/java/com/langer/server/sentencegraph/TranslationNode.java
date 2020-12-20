package com.langer.server.sentencegraph;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class TranslationNode
{
    long                wordTranslationId;
    Set<SentenceNode>   sentenceNodes;
    int                 ranking;

    @Override
    public boolean equals(Object obj)
    {
        return this == obj;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(wordTranslationId);
    }
}