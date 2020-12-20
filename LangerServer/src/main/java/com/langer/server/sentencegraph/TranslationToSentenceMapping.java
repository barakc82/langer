package com.langer.server.sentencegraph;

import com.google.common.collect.ImmutableList;

import java.util.*;


public class TranslationToSentenceMapping
{
    private Map<Long, TranslationNode>  translationNodeMap  = new HashMap<>();
    private Map<Long, SentenceNode>     sentenceNodeMap     = new HashMap<>();

    public void link(long wordTranslationId, long sentenceId)
    {
        TranslationNode translationNode = translationNodeMap.get(wordTranslationId);
        if (translationNode == null)
            translationNode = TranslationNode.builder()
                    .wordTranslationId(wordTranslationId)
                    .sentenceNodes(new HashSet<>())
                    .build();

        translationNodeMap.put(wordTranslationId, translationNode);

        SentenceNode sentenceNode = sentenceNodeMap.get(sentenceId);
        if (sentenceNode == null)
            sentenceNode = SentenceNode.builder()
                    .sentenceId(sentenceId)
                    .wordNodes(new HashSet<>())
                    .sentenceNeighbors(new ArrayList<>())
                    .build();

        sentenceNodeMap.put(sentenceId, sentenceNode);


        translationNode.getSentenceNodes().add(sentenceNode);
        sentenceNode.getWordNodes().add(translationNode);
    }

    public List<SentenceNode> getSentenceNodes()
    {
        return ImmutableList.copyOf(sentenceNodeMap.values());
    }

    public SentenceNode getSentenceNode(long sentenceId) { return sentenceNodeMap.get(sentenceId); }

    public TranslationNode getTranslationNode(long wordTranslationId)
    {
        return translationNodeMap.get(wordTranslationId);
    }
}