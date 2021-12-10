package annealing;

import GUI.Ex2;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static GUI.Ex2.getGrapgAlgo;


public class SimulatedAnnealing {

    // Calculate the acceptance probability
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    public static void main(String[] args) {

        DirectedWeightedGraph graph = Ex2.getGrapg("G1.json");
        DirectedWeightedGraphAlgorithms graphAlgo = getGrapgAlgo("G1.json");

        List<NodeData> cities = new LinkedList<>();

        Iterator<NodeData> nodeDataIterator = graph.nodeIter();
        while(nodeDataIterator.hasNext()){
            NodeData node = nodeDataIterator.next();
            cities.add(node);

            // Create and add our cities
            City city = new City(node);
            TourManager.addCity(city);
        }
        //graphAlgo.tsp(cities);


        // Set initial temp
        double temp = 10000;

        // Cooling rate
        double coolingRate = 0.003;

        // Initialize intial solution
        Tour currentSolution = new Tour();
        currentSolution.generateIndividual();

        System.out.println("Initial solution distance: " + currentSolution.getDistance(graphAlgo));

        // Set as current best
        Tour best = new Tour(currentSolution.getTour());

        // Loop until system has cooled
        while (temp > 1) {
            // Create new neighbour tour
            Tour newSolution = new Tour(currentSolution.getTour());

            // Get a random positions in the tour
            int tourPos1 = (int) (newSolution.tourSize() * Math.random());
            int tourPos2 = (int) (newSolution.tourSize() * Math.random());

            // Get the cities at selected positions in the tour
            City citySwap1 = newSolution.getCity(tourPos1);
            City citySwap2 = newSolution.getCity(tourPos2);

            // Swap them
            newSolution.setCity(tourPos2, citySwap1);
            newSolution.setCity(tourPos1, citySwap2);

            // Get energy of solutions
            double currentEnergy = currentSolution.getDistance(graphAlgo);
            double neighbourEnergy = newSolution.getDistance(graphAlgo);

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Tour(newSolution.getTour());
            }

            // Keep track of the best solution found
            if (currentSolution.getDistance(graphAlgo) < best.getDistance(graphAlgo)) {
                best = new Tour(currentSolution.getTour());
            }

            // Cool system
            temp *= 1-coolingRate;
        }

        System.out.println("Final solution distance: " + best.getDistance(graphAlgo));
        System.out.println("Tour: " + best);

        final Tour tour = best;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(tour);
            }
        });
    }

    private static void createAndShowGUI(Tour tour) {

        JFrame f = new JFrame("Annealing simulation for TSP");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel myPanel = new MyPanel(tour);

        f.add(myPanel);
        f.pack();
        f.setVisible(true);
    }
}

//http://docs.oracle.com/javase/tutorial/uiswing/painting/step3.html

class MyPanel extends JPanel {

    private static final long serialVersionUID = 7615629084996272465L;

    // if you want to increase the picture of the route, increase the koef
    float koef = 1.8f;

    // pagging/margin of the route image
    int offset = 50;

    // default area size, our coordinates are within range 0-200
    int areaSize = 200;

    private Tour tour;

    public MyPanel(Tour tour) {

        setBorder(BorderFactory.createLineBorder(Color.black));
        this.tour = tour;

    }

    private void showTour(Graphics g){

        // number of the current city in the route, starts at 1
        // for the first city we don't draw a line to the prev city
        int number = 1;

        double prevX = 0, prevY = 0;

        int max = (int)(offset + areaSize*koef);

        for (City city: tour.getTour()) {

            g.setColor(Color.BLUE);

            g.drawString(
                    String.valueOf(number),
                    offset + (int)((city.getNode().getLocation().x() - 5)*koef),
                    max - (int)((city.getNode().getLocation().y()+ 5 )*koef));

            g.setColor(Color.RED);

            if (number == 1) {
                prevX = city.getNode().getLocation().x();
                prevY = city.getNode().getLocation().y();

            } else {

                g.drawLine((int)(prevX*koef + offset), (int)(max - prevY*koef),
                        (int)(city.getNode().getLocation().x()*koef) + offset, max - (int)(city.getNode().getLocation().y()*koef));
                prevX = city.getNode().getLocation().x();
                prevY = city.getNode().getLocation().y();
            }
            number++;
        }
    }

    public Dimension getPreferredSize() {
        // we define the size of the window dynamically
        return new Dimension((int)(offset*2 + areaSize*koef), (int)(offset*2 + areaSize*koef));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DirectedWeightedGraphAlgorithms graphAlgo = getGrapgAlgo("G3.json");
        g.drawString("Resulting route, length = " + String.valueOf(tour.getDistance(graphAlgo)),10,20);

        g.setColor(Color.BLACK);

        g.drawRect(offset, offset , (int)(areaSize*koef), (int)(areaSize*koef));

        int step = (int)(areaSize*koef/20);

        // creating grid
        for (int i = 1; i < 20 ; i++){
            g.drawLine(offset - 5, i*step + offset, offset + 5 + (int)(areaSize*koef), i*step + offset);
            g.drawLine(offset + i*step, offset - 5, offset + i*step, offset + 5 + (int)(areaSize*koef));
        }

        showTour(g);
    }

}
