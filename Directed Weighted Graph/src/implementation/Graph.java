package implementation;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.*;
import java.util.Iterator;

public class Graph implements DirectedWeightedGraph {
    private HashMap<Integer, HashMap<Integer, EdgeData>> Edges;
    private HashMap<Integer, NodeData> nodes;
    private int MC;

    public Graph() {
        this.MC = 0;
        this.nodes = new HashMap<Integer, NodeData>();
        this.Edges = new HashMap<Integer, HashMap<Integer, EdgeData>>();
    }

    @Override
    public NodeData getNode(int key) {
        return this.nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return this.Edges.get(src).get(dest);
    }

    @Override
    public void addNode(NodeData n) {
        if (!this.nodes.containsKey(n.getKey())) {
            MC++;
            this.nodes.put(n.getKey(), n);
            this.Edges.put(n.getKey(), new HashMap<Integer, EdgeData>());
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (this.nodes.containsKey(src) && this.nodes.containsKey(dest) && !this.Edges.get(src).containsKey(dest) && src != dest) {
            EdgeData e = new Edge(src, w, dest);
            this.Edges.get(src).put(dest, e);
            MC++;
        }
    }

    private Iterator MyIter(Iterator iter) {
        Iterator res = new Iterator() {
            private Iterator itr = iter;
            private final int startmc = MC;

            @Override
            public boolean hasNext() {
                if (startmc != MC)
                    throw new RuntimeException("The graph modified.");
                return itr.hasNext();
            }

            @Override
            public Object next() {
                if (startmc != MC)
                    throw new RuntimeException("The graph modified.");
                return itr.next();
            }
        };
        return res;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return MyIter(this.nodes.values().iterator());
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        ArrayList<EdgeData> list = new ArrayList<EdgeData>();
        for (int i : this.Edges.keySet())
            for (int j : this.Edges.get(i).keySet())
                list.add(this.Edges.get(i).get(j));
        return MyIter(list.iterator());
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return MyIter(this.Edges.get(node_id).values().iterator());
    }

    @Override
    public NodeData removeNode(int key) {
        if (!this.nodes.containsKey(key))
            return null;
        MC += this.Edges.get(key).size();
        this.Edges.remove(key);
        for (int i : this.Edges.keySet()) {
            if(this.removeEdge(i, key)!=null)
                MC++;
        }
        MC++;
        return nodes.remove(key);
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (!this.Edges.get(src).containsKey(dest))
            return null;
        if (Edges.get(src).get(dest) != null)
            MC++;
        return this.Edges.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return this.nodes.size();
    }

    @Override
    public int edgeSize() {
        int count = 0;
        for (int i : this.Edges.keySet())
            count += this.Edges.get(i).size();
        return count;
    }

    @Override
    public int getMC() {
        return MC;
    }

    private String printNodes() {
        String str = "";
        for (NodeData i : this.nodes.values()) {
            str += i.toString() + ",";
        }
        return str;
    }
    private String printEdges() {
        String str = "";
        for (int i : this.Edges.keySet())
            for (int j : this.Edges.get(i).keySet())
                str += Edges.get(i).get(j) + ",";
        return str;
    }
    @Override
    public String toString() {
        return "{\n  Edges: ["+printEdges()+"\n],\nNodes: [" + printNodes() + "\n]\n}";
    }
}


