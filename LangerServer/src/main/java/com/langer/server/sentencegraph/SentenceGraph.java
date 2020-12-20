package com.langer.server.sentencegraph;

import java.util.*;

public class SentenceGraph
{
    private Map<Long, SentenceNode> idToNode    = new HashMap<>();
    private List<SentenceNode> sourceNodes;

    public void addDirectedEdge(SentenceNode sentenceNode1, SentenceNode sentenceNode2)
    {
        sentenceNode1.getSentenceNeighbors().add(sentenceNode2);
        addNode(sentenceNode1);
        addNode(sentenceNode2);
    }

    public List<SentenceNode> getSourceNodes() { return sourceNodes; }

    public void setSourceNodes(List<SentenceNode> sourceNodes) { this.sourceNodes = sourceNodes; }

    public void addNode(SentenceNode sentenceNode)
    {
        idToNode.put(sentenceNode.getSentenceId(), sentenceNode);
    }

    public Collection<SentenceNode> getSentenceNodes() { return idToNode.values(); }
}