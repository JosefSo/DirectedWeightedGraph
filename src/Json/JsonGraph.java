package Json;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.Iterator;

public class JsonGraph {
    private class JsonEdge{
        private int src;
        private double w;
        private int dest;
        private JsonEdge(int src,double w,int dest)
        {
            this.src=src;
            this.w=w;
            this.dest=dest;

        }
    }
    private class JsonNode{
        private String pos;
        private int id;
        private JsonNode(String pos,int id)
        {
            this.pos=pos;
            this.id=id;

        }
    }
    private JsonEdge[]Edges;
    private JsonNode[]Nodes;
    public JsonGraph(DirectedWeightedGraph graph)
    {
        Edges=new JsonEdge[graph.edgeSize()];
        Nodes=new JsonNode[graph.nodeSize()];
        Iterator<EdgeData>eitr=graph.edgeIter();
        for (int i = 0; i < Edges.length; i++) {
            EdgeData edge=eitr.next();
            Edges[i]=new JsonEdge(edge.getSrc(),edge.getWeight(),edge.getDest());
        }
        Iterator<NodeData>niter=graph.nodeIter();
        for (int i = 0; i < Nodes.length; i++) {
            NodeData node=niter.next();
            Nodes[i]=new JsonNode(node.getLocation().toString(),node.getKey());
        }
    }
}
