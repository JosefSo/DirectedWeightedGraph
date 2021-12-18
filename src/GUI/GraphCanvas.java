package GUI;

import api.*;
import implementation.Edge;
import implementation.Geo_Location;
import implementation.GraphAlgorithms;
import implementation.Node;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class GraphCanvas extends JPanel implements MouseListener{

    //Instance Variables of a GraphCanvas
    public GraphGUI frame;
    public static String radioButtonState;
    private static boolean isEnabled;
    protected static DirectedWeightedGraphAlgorithms graphDrawing;
    private static NodeData endpt1,endpt2;
    public boolean numbers=false;
    double minx,miny,maxx,maxy;
    boolean wasemtpy=false;
    boolean wasBigger=false;
    boolean pressed=false,mooved=false;


    /*
     * One Parameter Constructor of a GraphCanvas
     * @param frame the current JFrame the canvas
     * is on
     */
    public GraphCanvas(GraphGUI frame)
    {
        this.frame=frame;
        isEnabled = false;
        radioButtonState="1";
        graphDrawing = new GraphAlgorithms();
        graphDrawing.load(frame.getFileName());
        preload(graphDrawing.getGraph());
        this.addMouseListener(this);
        this.setBackground(Color.GRAY);
        this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
    }
    public void setIsEnabled(boolean e) {
        isEnabled = e;
    }
    public DirectedWeightedGraphAlgorithms getGraphDrawing(){return graphDrawing;}
    public void setRadioButtonState(String s) {
        radioButtonState=s;
    }


    private void preload(DirectedWeightedGraph graph)
    {
        if (graph.nodeSize()==0)
            wasemtpy = true;
        if(graph.nodeSize()<4&&wasemtpy)
        {
            minx=0;
            miny=0;
            maxx=1000;
            maxy=1000;
            return;
        }
        if (!wasemtpy&&!wasBigger) {
            Iterator<NodeData> nitr = graph.nodeIter();
            NodeData node = nitr.next();
            minx = node.getLocation().x();
            miny = node.getLocation().y();
            maxx = node.getLocation().x();
            maxy = node.getLocation().y();

            while (nitr.hasNext()) {
                node = nitr.next();
                if (node.getLocation().x() < minx) minx = node.getLocation().x();
                if (node.getLocation().x() > maxx) maxx = node.getLocation().x();
                if (node.getLocation().y() < miny) miny = node.getLocation().y();
                if (node.getLocation().y() > maxy) maxy = node.getLocation().y() * 1.00030;

            }
        }
    }

    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }
    public void paintComponent(Graphics g){
        preload(this.getGraphDrawing().getGraph());
        super.paintComponent(g);
        double unitX=this.getWidth()/Math.abs(maxx-minx)*0.975;
        double unitY=this.getWidth()/Math.abs(maxy-miny)*0.9;


        DirectedWeightedGraph graph=graphDrawing.getGraph();
        //paint edge
        Iterator<EdgeData>eitr=graph.edgeIter();
        List<EdgeData>colored=new LinkedList<EdgeData>();
        eitr=graph.edgeIter();
        while (eitr.hasNext()) {
           EdgeData edge = eitr.next();
            if (edge.getTag()!=0) {
                colored.add(edge);
            }
            else {
                double xsrc = graph.getNode(edge.getSrc()).getLocation().x();
                xsrc = ((xsrc - minx) * unitX) + 12;

                double ysrc = graph.getNode(edge.getSrc()).getLocation().y();
                ysrc = ((ysrc - miny) * unitY) + 12;

                double xdest = graph.getNode(edge.getDest()).getLocation().x();
                xdest = ((xdest - minx) * unitX) + 12;

                double ydest = graph.getNode(edge.getDest()).getLocation().y();
                ydest = ((ydest - miny) * unitY) + 12;

                g.setColor(Color.darkGray);

                drawArrowLine(g, (int) xsrc, (int) ysrc, (int) xdest, (int) ydest, 15, 3);
                if (edge.getWeight() != 0 && numbers) {
                    g.setFont(new Font("Dialog", Font.BOLD, 10));
                    g.drawString("" + edge.getWeight(), (((int) xsrc + (int) xdest) / 2) + 20, (((int) ysrc + (int) ydest) / 2));
                }
            }

            }
        for (EdgeData edge:colored) {
            double xsrc = graph.getNode(edge.getSrc()).getLocation().x();
            xsrc = ((xsrc - minx) * unitX) + 12;

            double ysrc = graph.getNode(edge.getSrc()).getLocation().y();
            ysrc = ((ysrc - miny) * unitY) + 12;

            double xdest = graph.getNode(edge.getDest()).getLocation().x();
            xdest = ((xdest - minx) * unitX) + 12;

            double ydest = graph.getNode(edge.getDest()).getLocation().y();
            ydest = ((ydest - miny) * unitY) + 12;

            switch (edge.getTag()) {
                case 1:
                    g.setColor(Color.RED);
                    break;
                case 2:
                    g.setColor(Color.GREEN);
                    break;
                case 3:
                    g.setColor(Color.BLUE);

            }
            drawArrowLine(g, (int) xsrc, (int) ysrc, (int) xdest, (int) ydest, 15, 3);
            if (edge.getWeight() != 0 && numbers) {
                g.setFont(new Font("Dialog", Font.BOLD, 10));
                g.drawString("" + edge.getWeight(), (((int) xsrc + (int) xdest) / 2) + 20, (((int) ysrc + (int) ydest) / 2));
            }

        }

        //paint node
        List<NodeData>colorednodes=new LinkedList<NodeData>();
        Iterator<NodeData>nitr=graph.nodeIter();
        while (nitr.hasNext()) {
            NodeData node = nitr.next();
            if (node.getTag() != 0) {
                colorednodes.add(node);
            } else {
                int x = (int) ((node.getLocation().x() - minx) * unitX);
                int y = (int) ((node.getLocation().y() - miny) * unitY);
                g.setColor(Color.darkGray);
                g.fillOval(x+4, y+3, 13, 13);
                if (numbers) {
                    g.setColor(Color.white);
                    g.setFont(new Font("Dialog", Font.BOLD, 12));
                    g.drawString("" + node.getKey(), x + 7, y + 15);
                }
            }
        }
        for (NodeData node:colorednodes) {
            int x = (int) ((node.getLocation().x() - minx) * unitX);
            int y = (int) ((node.getLocation().y() - miny) * unitY);
            switch (node.getTag()) {
                case 1:
                    g.setColor(Color.RED);
                    break;
                case 2:
                    g.setColor(Color.GREEN);
                    break;
                case 3:
                    g.setColor(Color.BLUE);
            }
            g.fillOval(x+6, y+3, 13, 13);
            if (numbers) {
                if (node.getTag()==2)
                   g.setColor(Color.BLACK);
                else
                    g.setColor(Color.WHITE);
                g.setFont(new Font("Dialog", Font.BOLD, 12));
                g.drawString("" + node.getKey(), x + 7, y + 15);
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double unitX = this.getWidth() / Math.abs(maxx - minx) * 0.975;
        double unitY = this.getWidth() / Math.abs(maxy - miny) * 0.9;
        double x = (e.getX() / unitX) + minx;
        double y = (e.getY() / unitY) + miny;

        if (radioButtonState.equals("")) {
            this.paintComponent(this.getGraphics());
        }

        if (!isEnabled) return;

        if (radioButtonState.equals("add node")) {
            int id = 0;
            for (int i = 0; i < graphDrawing.getGraph().nodeSize() + 1; i++) {
                if (graphDrawing.getGraph().getNode(i) == null) {
                    id = i;
                    break;
                }
            }
            String pos = x + "," + y + ",0.0";
            NodeData v = new Node(pos, id);
            graphDrawing.getGraph().addNode(v);
            this.paintComponent(this.getGraphics());

        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }



}