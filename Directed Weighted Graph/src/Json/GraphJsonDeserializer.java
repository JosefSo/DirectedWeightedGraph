package Json;

import java.lang.reflect.Type;
import java.util.Iterator;
import api.DirectedWeightedGraph;
import api.NodeData;
import com.google.gson.*;
import implementation.Edge;
import implementation.Graph;
import implementation.Node;


public class GraphJsonDeserializer implements JsonDeserializer<Graph> {

    @Override
    public Graph deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        DirectedWeightedGraph graph = new Graph();

        JsonArray nodesArray = (JsonArray) jsonObject.get("Nodes"); //Gets all Nodes as a json array
        Iterator<JsonElement> nodeIter = nodesArray.iterator();

        while (nodeIter.hasNext()) {
            JsonObject JsonNode = nodeIter.next().getAsJsonObject();
            String nodePos = JsonNode.get("pos").getAsString();
            int nodeId = JsonNode.get("id").getAsInt();
            NodeData node = new Node(nodePos, nodeId);
            graph.addNode(node);
        }

        JsonArray edgesArray = (JsonArray) jsonObject.get("Edges"); //Gets all Nodes as a json array
        Iterator<JsonElement> edgeIter = edgesArray.iterator();

        while (edgeIter.hasNext()) {
            JsonObject JsonNode = edgeIter.next().getAsJsonObject();

            int edgeSrc = JsonNode.get("src").getAsInt();
            double edgeW = JsonNode.get("w").getAsDouble();
            int edgeDest = JsonNode.get("dest").getAsInt();

            Edge edge = new Edge(edgeSrc, edgeW, edgeDest);
            graph.connect(edgeSrc, edgeDest, edgeW);
        }
        return (Graph) graph;
    }
}

