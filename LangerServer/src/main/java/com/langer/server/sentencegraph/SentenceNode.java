package com.langer.server.sentencegraph;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class SentenceNode
{
    private long                sentenceId;
    private Set<TranslationNode>       wordNodes;
    private List<SentenceNode>  sentenceNeighbors;
    private int                 ranking;

    @Override
    public boolean equals(Object obj)
    {
        return this == obj;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(sentenceId);
    }

    @Override
    public String toString() { return "Here! (sentence node)"; }
}