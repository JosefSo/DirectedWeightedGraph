package GUI;
import java.awt.*;
import javax.swing.*;

public class GraphGUI extends JFrame{

    protected GraphCanvas canvas;
    private String filename;
    private ButtonListener bl;
    MenuBar menuBar;
    Menu helpMenu,fileMenu,editMenu,graphMenu,pathMenu;
    MenuItem helpMenui,loadItem,saveItem,exitItem,rndItem,isConectedItem,centerItem,clearColorsItem,clearItem,pathItem,tspItem,numbersItem,addNodeItem,removeNodeItem,connectItem,removeEdgeItem ;
    JRadioButton addbutton;


    public GraphGUI(String filename) {
        setTitle("Sali and Yosef Graph GUI");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.jpg")));
        this.filename=filename;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension(d));
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bl = new ButtonListener(this);
        setComponents();
        setVisible(true);
    }

    public String getFileName()
    {
        return this.filename;
    }

    public void setComponents() {
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        canvas = new GraphCanvas(this);
        canvas.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setMenu();
        add(canvas);



    }

    public void setMenu()
    {
        addbutton=new JRadioButton();
        addbutton = new JRadioButton("add node");
        addbutton.addActionListener(bl);
        menuBar = new MenuBar();


        fileMenu = new Menu("File");
        editMenu = new Menu("Edit");
        graphMenu = new Menu("Graph");
        helpMenu = new Menu("Help");
        helpMenui = new MenuItem("Help");
        pathMenu = new Menu("Path");



        loadItem = new MenuItem("Load");
        saveItem = new MenuItem("Save");
        exitItem = new MenuItem("Exit");

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        loadItem.addActionListener(bl);
        saveItem.addActionListener(bl);
        exitItem.addActionListener(bl);

        isConectedItem = new MenuItem("Is Connected");
        centerItem = new MenuItem("Center");
        rndItem = new MenuItem("Random Graph");
        clearColorsItem = new MenuItem("Clear colors");
        clearItem = new MenuItem("Clear graph");

        graphMenu.add(isConectedItem);
        graphMenu.add(centerItem);
        graphMenu.add(rndItem);
        graphMenu.add(clearColorsItem);
        graphMenu.add(clearItem);

        isConectedItem.addActionListener(bl);
        centerItem.addActionListener(bl);
        rndItem.addActionListener(bl);
        clearColorsItem.addActionListener(bl);
        clearItem.addActionListener(bl);

        pathItem = new MenuItem("Shortest path");
        tspItem = new MenuItem("TSP");

        pathMenu.add(pathItem);
        pathMenu.add(tspItem);

        pathItem.addActionListener(bl);
        tspItem.addActionListener(bl);

        if (!this.canvas.numbers)
            numbersItem = new MenuItem("show numbers");
        else
            numbersItem = new MenuItem("hide numbers");
        addNodeItem = new MenuItem("add node");
        removeNodeItem = new MenuItem("remove node");
        connectItem = new MenuItem("connect edge");
        removeEdgeItem = new MenuItem("remove edge");


        editMenu.add(numbersItem);
        editMenu.add(addNodeItem);
        editMenu.add(removeNodeItem);
        editMenu.add(connectItem);
        editMenu.add(removeEdgeItem);

        numbersItem.addActionListener(bl);
        addNodeItem.addActionListener(bl);
        removeNodeItem.addActionListener(bl);
        connectItem.addActionListener(bl);
        helpMenui.addActionListener(bl);
        removeEdgeItem.addActionListener(bl);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(graphMenu);
        menuBar.add(pathMenu);
        helpMenu.add(helpMenui);
        menuBar.add(helpMenu);
        this.setMenuBar(menuBar);
        this.setVisible(true);
    }
}