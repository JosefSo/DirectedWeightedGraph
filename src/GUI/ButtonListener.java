package GUI;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import implementation.Graph;
import implementation.GraphAlgorithms;
import implementation.Node;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.Option;

public class ButtonListener implements ActionListener {

    private GraphGUI gui;

    public ButtonListener(GraphGUI gui) {
        this.gui = gui;
    }
    private Boolean isBindingComponent(DirectedWeightedGraph graph,List<NodeData> cities){
        DirectedWeightedGraphAlgorithms algo=new GraphAlgorithms();
        for(NodeData node:cities)
        {
            algo.getGraph().addNode(graph.getNode(node.getKey()));
        }
        for(NodeData node:cities)
        {
            Iterator<EdgeData>eitr=graph.edgeIter(node.getKey());
            while (eitr.hasNext()) {
                EdgeData edge =eitr.next();
                algo.getGraph().connect(edge.getSrc(),edge.getDest(),edge.getWeight());
            }
        }
        return algo.isConnected();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String buttonName = e.getActionCommand();


        if (buttonName.equals("Load")) {
            try{
                JFileChooser chooser= new JFileChooser();
                chooser.setCurrentDirectory(new File("data\\"));
                chooser.setFileFilter(new FileNameExtensionFilter(".json","json"));
                int value = chooser.showOpenDialog(null);
                if(value == JFileChooser.APPROVE_OPTION){
                    File selectedFile = chooser.getSelectedFile();
                    String filename = selectedFile.getAbsolutePath();
                    if (filename!=null&&gui.canvas.getGraphDrawing().load(filename)){
                        this.gui.canvas.numbers=false;
                        this.gui.canvas.wasemtpy=false;
                        this.gui.canvas.wasBigger=false;
                        this.gui.numbersItem.setLabel("show numbers");
                        gui.canvas.paintComponent(gui.canvas.getGraphics());
                        JOptionPane.showMessageDialog(null, filename + " was loaded.");
                    } else JOptionPane.showMessageDialog(null, "load failed.");
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,ex);
            }

        }
        if (buttonName.equals("Save")) {
            String filename = (String)JOptionPane.showInputDialog(
                    this.gui,
                    "Write the name of the file.",
                    "save file",
                    JOptionPane.PLAIN_MESSAGE,null,
                    null,
                    "data\\Unknown"
            );
                int dialogButton = JOptionPane.showConfirmDialog(
                        this.gui,
                        "Are you sure you want to save?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (dialogButton == JOptionPane.YES_OPTION) {
                    if (gui.canvas.getGraphDrawing().save(filename+".json")) {
                        JOptionPane.showMessageDialog(this.gui, "Graph saved.");
                    } else JOptionPane.showMessageDialog(this.gui, "save failed.");
                }
        }
        if (buttonName.equals("Shortest path")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            String srcinput,destinput;
            srcinput = (String)JOptionPane.showInputDialog(
                    this.gui,
                    "Write the key of the starting node.",
                    "input src",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    ""
            );
            int src, dest;
            try {
                if (srcinput.equals("")) throw new NullPointerException();
                else {
                      destinput = (String)JOptionPane.showInputDialog(
                            this.gui,
                            "Write the key of the ending node.",
                            "input dest",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            ""
                    );
                }

                if (destinput.equals("")) throw new NullPointerException();
                src = Integer.parseInt(srcinput);
                dest = Integer.parseInt(destinput);
            } catch (NullPointerException ex) {
                return;
            }
            List<NodeData> list;
            try {
                list = this.gui.canvas.graphDrawing.shortestPath(src, dest);
                if (list==null) throw new NullPointerException();
            }
            catch (NullPointerException exception)
            {
                JOptionPane.showMessageDialog(null, "Path from "+src+" to "+dest+" is not exist.");
                return;
            }



                String path = "";
                int[] arr = new int[list.size()];
                int i = 0;
                for (NodeData n : list) {
                    arr[i] = n.getKey();
                    i++;
                    path += this.gui.canvas.graphDrawing.getGraph().getNode(n.getKey()).getKey() + "->";
                }
                for (i = 0; i < arr.length - 1; i++) {
                    this.gui.canvas.graphDrawing.getGraph().getEdge(arr[i], arr[i + 1]).setTag(2);
                }
                path = path.substring(0, path.length() - 2);

                this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
                JOptionPane.showMessageDialog(null, "The shortest path distance from " + src + " to " + dest + " is " + this.gui.canvas.graphDrawing.shortestPathDist(src, dest) + ", the way is " + path + ".");
            }
        if (buttonName.equals("TSP")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            String path;
            try {
                path = (String) JOptionPane.showInputDialog(
                        this.gui,
                        "Write keys for TSP seperates by ,",
                        "input TSP",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        ""
                );

                    if (path.equals("")) throw new NullPointerException();
                } catch(NullPointerException ex){
                    JOptionPane.showMessageDialog(null, "No input");
                    return;
                }
                String[] keys;
                try {
                    keys = path.split(",");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid path. Write only numbers and ','");
                    return;
                }
                List<NodeData> tspnodes = new LinkedList<NodeData>();
                for (int i = 0; i < keys.length; i++) {
                    try {
                        tspnodes.add(this.gui.canvas.getGraphDrawing().getGraph().getNode(Integer.parseInt(keys[i])));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this.gui, "Invalid path. Write only numbers and ','");
                    }
                }
                if (!isBindingComponent(this.gui.canvas.getGraphDrawing().getGraph(),tspnodes))
                {
                    JOptionPane.showMessageDialog(this.gui, "This path is not a binding component.");return;
                }
                DirectedWeightedGraph graph1 = this.gui.canvas.getGraphDrawing().copy();
                DirectedWeightedGraphAlgorithms algo1 = new GraphAlgorithms(graph1);
                try {
                    tspnodes = algo1.tsp(tspnodes);
                    if (tspnodes == null) throw new NullPointerException();
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "No tsp path with that nodes");

                }

                path = "";
                int[] arr = new int[tspnodes.size()];
                int i = 0;
                for (NodeData n : tspnodes) {
                    arr[i] = n.getKey();
                    i++;
                    path += this.gui.canvas.graphDrawing.getGraph().getNode(n.getKey()).getKey() + "->";
                }
                for (i = 0; i < arr.length - 1; i++) {
                    if(this.gui.canvas.graphDrawing.getGraph().getEdge(arr[i], arr[i + 1])!=null)
                        this.gui.canvas.graphDrawing.getGraph().getEdge(arr[i], arr[i + 1]).setTag(2);
                }
                path = path.substring(0, path.length() - 2);

                this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
                JOptionPane.showMessageDialog(null, "The tsp path is: " + path + ".");

        }
        if (buttonName.equals("remove node")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            Iterator<NodeData>nitr=this.gui.canvas.getGraphDrawing().getGraph().nodeIter();
            int id=nitr.next().getKey();
            String input;
            input = (String)JOptionPane.showInputDialog(
                    this.gui,
                    "Write a node key to remove.",
                    "input key",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    ""+id
            );
                try {
                    if (input.equals("")) throw new NullPointerException();
                    id = Integer.parseInt(input);
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "No input");
                    return;
                }
                if (this.gui.canvas.graphDrawing.getGraph().removeNode(id) != null) {
                    this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
                    this.gui.canvas.wasBigger=true;
                    JOptionPane.showMessageDialog(null, "Node " + id + " removed.");
                } else
                    JOptionPane.showMessageDialog(null, "Node " + id + " is not exist.");

        }
        if (buttonName.equals("remove edge")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            if(this.gui.canvas.getGraphDrawing().getGraph().edgeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph has no edges!");
                return;
            }
            int src, dest;
            String srcinput,destinput;
            srcinput = (String)JOptionPane.showInputDialog(
                    this.gui,
                    "Write the key of the source node to remove.",
                    "input src",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    ""
            );
            try {
                if (srcinput.equals("")) throw new NullPointerException();
                else {
                    destinput = (String)JOptionPane.showInputDialog(
                            this.gui,
                            "Write the key of the dest node to remove.",
                            "input dest",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            ""
                    );
                }

                if (destinput.equals("")) throw new NullPointerException();
                src = Integer.parseInt(srcinput);
                dest = Integer.parseInt(destinput);
            } catch (NullPointerException ex) {
                return;
            }
            if(this.gui.canvas.graphDrawing.getGraph().removeEdge(src, dest)!=null){
                this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
                JOptionPane.showMessageDialog(null, "Edge "+src+"->"+dest+" removed.");
            }
            else
                JOptionPane.showMessageDialog(null, "Edge "+src+"->"+dest+" is not exist.");
        }
        if (buttonName.equals("connect edge")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==1){
                JOptionPane.showMessageDialog(null, "The graph should have at least 2 nodes to make connection.");
                return;
            }
            int src, dest;
            String srcinput,destinput,weightinput;
            srcinput = (String)JOptionPane.showInputDialog(
                    this.gui,
                    "Write the key of the source node to connect.",
                    "input src",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    ""
            );

            try {
                if (srcinput.equals("")) throw new NullPointerException();
                else {
                    try {
                        src = Integer.parseInt(srcinput);
                    }
                    catch (NumberFormatException exception)
                    {
                        JOptionPane.showMessageDialog(null, "Use only numbers.");return;
                    }

                    destinput = (String)JOptionPane.showInputDialog(
                            this.gui,
                            "Write the key of the dest node to connect.",
                            "input dest",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            ""
                    );
                }

                if (destinput.equals("")) throw new NullPointerException();

                try {
                    dest = Integer.parseInt(destinput);
                }
                catch (NumberFormatException exception)
                {
                    JOptionPane.showMessageDialog(null, "Use only numbers.");return;
                }

            } catch (NullPointerException ex) {
                return;
            }
            double w;
            weightinput = (String)JOptionPane.showInputDialog(
                    this.gui,
                    "set weight for the connection. (defult weight will be 1).",
                    "input weight",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "1"
            );
            if(weightinput.equals("")){
                w = 1;}
            else{
                w=Double.parseDouble(weightinput);}
            this.gui.canvas.graphDrawing.getGraph().connect(src, dest, w);
            this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
        }
        if (buttonName.equals("Center")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            int center;
            try {
                if (this.gui.canvas.graphDrawing.center() == null) throw new NullPointerException();
                center = this.gui.canvas.graphDrawing.center().getKey();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "The graph is not connected, no center.");
                return;
            }
            this.gui.canvas.graphDrawing.getGraph().getNode(center).setTag(1);
            this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
            JOptionPane.showMessageDialog(null, "The center of the graph is node number " + this.gui.canvas.graphDrawing.center().getKey());

        }
        if (buttonName.equals("Is Connected")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            if (this.gui.canvas.graphDrawing.isConnected())
                JOptionPane.showMessageDialog(null, "The graph is connected!");
            else
                JOptionPane.showMessageDialog(null, "The graph is not connected!");
        }

        if (buttonName.equals("Clear colors")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            DirectedWeightedGraph g1 =this.gui.canvas.getGraphDrawing().getGraph();
            Iterator<NodeData>nitr=g1.nodeIter();
            while (nitr.hasNext())
                nitr.next().setTag(0);
            Iterator<EdgeData>eitr=g1.edgeIter();
            while (eitr.hasNext())
                eitr.next().setTag(0);
            this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
        }

        if (buttonName.equals("Clear graph")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is already empty!");
                return;
            }
            DirectedWeightedGraph g1 =new Graph();
            this.gui.canvas.getGraphDrawing().init(g1);
            this.gui.canvas.numbers=false;
            this.gui.numbersItem.setLabel("show numbers");
            this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());

        }
        if (buttonName.equals("show numbers")) {
            if(this.gui.canvas.getGraphDrawing().getGraph().nodeSize()==0){
                JOptionPane.showMessageDialog(null, "The graph is empty!");
                return;
            }
            this.gui.numbersItem.setLabel("hide numbers");
            this.gui.canvas.numbers=true;
            this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
        }
        if (buttonName.equals("hide numbers")) {
            this.gui.numbersItem.setLabel("show numbers");
            this.gui.canvas.numbers=false;
            this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
        }
        if(buttonName.equals("add node")) {
          //  gui.buttons[0].setAction(gui.buttons[0].setSelected(!gui.buttons[0].isSelected()));
            gui.addbutton.setSelected(!gui.addbutton.isSelected());
            if(gui.addbutton.isSelected()) {
                gui.canvas.setRadioButtonState("add node");
                gui.canvas.setIsEnabled(true);
                gui.addNodeItem.setFont(new Font("Dialog",Font.BOLD,12));
            }

            else {
                gui.addNodeItem.setFont(new Font("Dialog",Font.PLAIN,12));
                gui.canvas.setRadioButtonState("");
                gui.canvas.setIsEnabled(false);
                gui.canvas.repaint();
            }

        }
        if (buttonName.equals("Exit")) {
            this.gui.dispatchEvent(new WindowEvent(this.gui, WindowEvent.WINDOW_CLOSING));
        }
        if (buttonName.equals("Random Graph")) {
            int maxid=0;
            DirectedWeightedGraphAlgorithms rndgraph= new GraphAlgorithms(new Graph());
            String input;
            input = (String)JOptionPane.showInputDialog(
                    this.gui,
                    "Write the number of nodes to build a random graph.",
                    "input num of nodes",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    ""
            );
            try {
                if (input.equals("")) throw new NullPointerException();
                maxid = Integer.parseInt(input);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "No input");
                return;
            }
            for (int i=0;i<maxid;i++)
            {
                double x=Math.random()+35;
                double y=Math.random()+32;
                String pos=x+","+y+",0.0";
                rndgraph.getGraph().addNode(new Node(pos,i));
            }
            for (int i = 0; i <maxid ; i++) {
                        rndgraph.getGraph().connect(i,(int)(Math.random()*maxid),Math.random()+1);
                        rndgraph.getGraph().connect((int)(Math.random()*maxid),i,Math.random()+1);
            }
            this.gui.canvas.getGraphDrawing().init(rndgraph.getGraph());
            this.gui.canvas.numbers=false;
            this.gui.numbersItem.setLabel("show numbers");

            this.gui.canvas.wasemtpy=false;
            this.gui.canvas.wasBigger=false;
            this.gui.canvas.paintComponent(this.gui.canvas.getGraphics());
            JOptionPane.showMessageDialog(null, "Random succeed");



        }


        if (buttonName.equals("Help")) {
            System.out.println("help!!!!");
            File selectedFile = new File("README.html");
            String path = selectedFile.getAbsolutePath();
            File myFile = new File(path);
            try {
                Desktop.getDesktop().browse(new URL("http://github.com/SaliSharfman/Ex2_DirectedWeightedGraph/blob/master/README.md").toURI());
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        }



    }
}

