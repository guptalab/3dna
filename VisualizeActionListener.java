/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */
import java.awt.*;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class VisualizeActionListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Visualization option has been selected");


        JFrame frame = new JFrame("Stepping Progress");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JProgressBar aJProgressBar = new JProgressBar(JProgressBar.VERTICAL);
        aJProgressBar.setStringPainted(true);
        aJProgressBar.setIndeterminate(true);

        frame.add(aJProgressBar, BorderLayout.NORTH);
        frame.setSize(300, 200);
        frame.setVisible(true);

        visualizeFrame = new JFrame("3DNA Visualization");
        visualizeFrame.setVisible(true);
        visualizeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        visualizeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Initialize the visualizeFrameBasePanel
        visualizeFrameBasePanel = new JPanel();
        visualizeFrameBasePanel.setLayout(new BorderLayout());
        visualizeFrame.getContentPane().add(visualizeFrameBasePanel);

        //Initialize LeftJToolbar
        initializeLeftJToolBar();
        visualizeFrameBasePanel.add(leftJToolBar,BorderLayout.WEST);

        //Initialize LeftJToolbar
        initializeRightJToolBar();
        visualizeFrameBasePanel.add(rightJToolBar,BorderLayout.EAST);

        //Initialize Canvas3D
        initializeCanvas3D();
        visualizeFrameBasePanel.add(visualizeCanvas,BorderLayout.CENTER);
    }

    public static void initializeRightJToolBar(){
        rightJToolBar = new JToolBar("Visualization Status Toolbar", JToolBar.VERTICAL);
        rightJToolBar.setBackground(Color.DARK_GRAY);

        // Add leftJToolBar Images and Buttons
        ImageIcon domain12Icon = new ImageIcon("icons/halfBrick12.png");
        ImageIcon domain34Icon = new ImageIcon("icons/halfBrick34.png");
        ImageIcon domain1234NorthIcon = new ImageIcon("icons/fullBrickNorth.png");
        ImageIcon domain1234WestIcon = new ImageIcon("icons/fullBrickWest.png");
        ImageIcon domain1234SouthIcon = new ImageIcon("icons/fullBrickEast.png");
        ImageIcon domain1234EastIcon = new ImageIcon("icons/fullBrickSouth.png");

        JLabel domain12Label = new JLabel("<html><style>h3{color:white;}</style><h3>Half Brick (Domain 1,2)</h3></html>", domain12Icon, JLabel.HORIZONTAL);
        JLabel domain34Label = new JLabel("<html><style>h3{color:white;}</style><h3>Half Brick (Domain 3,4)</h3></html>", domain34Icon, JLabel.HORIZONTAL);
        JLabel domain1234NorthLabel = new JLabel("<html><style>h3{color:white;}</style><h3>Full Brick (North)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h3></html>", domain1234NorthIcon, JLabel.HORIZONTAL);
        JLabel domain1234WestLabel = new JLabel("<html><style>h3{color:white;}</style><h3>Full Brick (West)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h3></html>", domain1234WestIcon, JLabel.HORIZONTAL);
        JLabel domain1234SouthLabel = new JLabel("<html><style>h3{color:white;}</style><h3>Full Brick (South)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h3></html>", domain1234SouthIcon, JLabel.HORIZONTAL);
        JLabel domain1234EastLabel = new JLabel("<html><style>h3{color:white;}</style><h3>Full Brick (East)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h3></html>", domain1234EastIcon, JLabel.HORIZONTAL);

        rightJToolBar.add(domain12Label);
        rightJToolBar.add(domain34Label);
        rightJToolBar.add(domain1234NorthLabel);
        rightJToolBar.add(domain1234WestLabel);
        rightJToolBar.add(domain1234SouthLabel);
        rightJToolBar.add(domain1234EastLabel);

    }

    public static void initializeLeftJToolBar(){
        leftJToolBar = new JToolBar("Visualization Control Toolbar", JToolBar.VERTICAL);
        leftJToolBar.setBackground(Color.DARK_GRAY);

        // Add leftJToolBar Images and Buttons
        ImageIcon zoominIcon = new ImageIcon("icons/zoomin.png");
        ImageIcon zoomoutIcon = new ImageIcon("icons/zoomout.png");
        ImageIcon upArrowIcon = new ImageIcon("icons/arrow-up.png");
        ImageIcon downArrowIcon = new ImageIcon("icons/arrow-down.png");
        ImageIcon leftArrowIcon = new ImageIcon("icons/arrow-left.png");
        ImageIcon rightArrowIcon = new ImageIcon("icons/arrow-right.png");
        ImageIcon resetIcon = new ImageIcon("icons/reset.png");

        zoomInButton = new JButton(zoominIcon);
        zoomInButton.setBackground(Color.DARK_GRAY);
        zoomOutButton = new JButton(zoomoutIcon);
        zoomOutButton.setBackground(Color.DARK_GRAY);
        upButton = new JButton(upArrowIcon);
        upButton.setBackground(Color.DARK_GRAY);
        downButton = new JButton(downArrowIcon);
        downButton.setBackground(Color.DARK_GRAY);
        leftButton = new JButton(leftArrowIcon);
        leftButton.setBackground(Color.DARK_GRAY);
        rightButton = new JButton(rightArrowIcon);
        rightButton.setBackground(Color.DARK_GRAY);
        resetButton = new JButton(resetIcon);
        resetButton.setBackground(Color.DARK_GRAY);

        zoomInButton.addActionListener(new VisualizationZoomInActionListener());
        zoomOutButton.addActionListener(new VisualizationZoomOutActionListener());
        upButton.addActionListener(new VisualizationUpButtonActionListener());
        downButton.addActionListener(new VisualizationDownButtonActionListener());
        leftButton.addActionListener(new VisualizationLeftButtonActionListener());
        rightButton.addActionListener(new VisualizationRightButtonActionListener());
        resetButton.addActionListener(new VisualizationResetButtonActionListener());

        leftJToolBar.add(zoomInButton);
        leftJToolBar.add(zoomOutButton);
        leftJToolBar.add(upButton);
        leftJToolBar.add(leftButton);
        leftJToolBar.add(rightButton);
        leftJToolBar.add(downButton);
        leftJToolBar.add(resetButton);

    }

    public static void initializeCanvas3D(){
        visualizeCanvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        visualizationSimpleUniverse = new SimpleUniverse(visualizeCanvas);
        //initialize viewing platform
        viewingPlatform = visualizationSimpleUniverse.getViewingPlatform();

        //define background colors for the Canvas
        BranchGroup backgroundBranchGroup = new BranchGroup();
        Background background = new Background(new Color3f(1f,1f,1f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000);
        background.setApplicationBounds(sphere);
        backgroundBranchGroup.addChild(background);
        viewingPlatform.addChild(backgroundBranchGroup);

        //setting the Viewing Platform with the initial values
        canvasX = 0.0f;
        canvasY = 0.0f;
        canvasZ = 20.0f;
        TransformGroup View_TransformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated
        Transform3D View_Transform3D = new Transform3D();    // create a Transform3D for the ViewingPlatform
        View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform
        View_Transform3D.setTranslation(new Vector3f(canvasX,canvasY,canvasZ)); // set 3d to  x=0, y=0, z=20
        View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform

        //initialize visualizeScene
        visualizeScene = createSceneGraph();
        visualizeScene.compile();
        visualizationSimpleUniverse.addBranchGraph(visualizeScene);
    }

    public static BranchGroup createSceneGraph() {

        objRoot = new BranchGroup();
        masterTrans = new TransformGroup();

        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        rotateBehaviour=new MouseRotate();
        rotateBehaviour.setTransformGroup(masterTrans);
        rotateBehaviour.setSchedulingBounds(new BoundingSphere());

        masterTrans.addChild(rotateBehaviour);

        int x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4;
        int canvasDomainXCoordinate, canvasDomainYCoordinate, canvasDomainZCoordinate;
        ArrayList<VoxelToBrick> DNASequenceData = CoordinatesSequenceMap.finalData;
        for (int i = 0; i < DNASequenceData.size(); i++) {
            //identify and print for domains 1 and 2(HalfBrick)
            if (DNASequenceData.get(i).x3 == -1) {
                x1 = DNASequenceData.get(i).x1;
                y1 = DNASequenceData.get(i).y1;
                z1 = DNASequenceData.get(i).z1;

                x2 = DNASequenceData.get(i).x2;
                y2 = DNASequenceData.get(i).y2;
                z2 = DNASequenceData.get(i).z2;

                System.out.println("Printing Domain12HalfBrick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2);

                canvasDomainXCoordinate = x1;
                canvasDomainYCoordinate = y1;
                canvasDomainZCoordinate = z2;

                //Code for Domain12
                getDomain12Block((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate);


            }else if (DNASequenceData.get(i).x1 == -1) {//identify and print for domains 3 and 4(HalfBrick)
                x3 = DNASequenceData.get(i).x3;
                y3 = DNASequenceData.get(i).y3;
                z3 = DNASequenceData.get(i).z3;

                x4 = DNASequenceData.get(i).x4;
                y4 = DNASequenceData.get(i).y4;
                z4 = DNASequenceData.get(i).z4;

                System.out.println("Printing Domain34HalfBrick"+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4);

                canvasDomainXCoordinate = x3;
                canvasDomainYCoordinate = y3;
                canvasDomainZCoordinate = z3;

                //Code for Domain34
                getDomain34Block((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate);


            }else {//identify and print for full bricks(FullBrick)
                x1 = DNASequenceData.get(i).x1;
                y1 = DNASequenceData.get(i).y1;
                z1 = DNASequenceData.get(i).z1;

                x2 = DNASequenceData.get(i).x2;
                y2 = DNASequenceData.get(i).y2;
                z2 = DNASequenceData.get(i).z2;

                x3 = DNASequenceData.get(i).x3;
                y3 = DNASequenceData.get(i).y3;
                z3 = DNASequenceData.get(i).z3;

                x4 = DNASequenceData.get(i).x4;
                y4 = DNASequenceData.get(i).y4;
                z4 = DNASequenceData.get(i).z4;

                System.out.println("Printing Domain1234FullBrick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4);

                //Code for adding Domain1234(FullBrick) based on their orientation

                if(z2%4 == 0){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;

                    getDomain1234NorthBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate);
                }else if(z2%4 == 1){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;

                    getDomain1234WestBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate);
                }else if(z2%4 == 2){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;

                    getDomain1234SouthBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate);
                }else if(z2%4 == 3){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;

                    getDomain1234EastBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate);
                }
            }
        }
        isCompleted=true;
        /**Code for creating 3DNA Domain Blocks
         *The building units of the Visualization class comprises of 3 modular Blocks viz. domain12Block, domain34Block,
         *  domain1234Block
         *  Each block has its own domainXX[XX]tg, domainXX[XX]t3d, domainxx[XX]CoverCylinderTransformGroup,
         *  domainxx[XX]CoverCylinderTransform3D, domainxx[XX]CoverCylinder, lightForDomainxx[XX].
         *  The final parameters which will be used for creating the visualization canvas will include:
         *  domainBlockxx[XX]tg and domainBlockxx[XX]t3d
         */
        objRoot.addChild(masterTrans);
        return objRoot;
    }


    public static void getDomain12Block(float x, float y, float z){
        TransformGroup domain12CoverCylinderTransformGroup = new TransformGroup();

        TransformGroup domain12tg = new TransformGroup();
        Transform3D domain12t3d = new Transform3D();

        Transform3D rotation12 = new Transform3D();
        rotation12.rotX(Math.PI/2);

        try
        {
            Scene scene1 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            String s1 = "HalfBrickDomain12.obj";
            scene1 = f.load (s1);

            domain12tg.addChild (scene1.getSceneGroup ());
            domain12t3d.mul(rotation12);
            domain12t3d.setTranslation(new Vector3f(0.5f,0f,0f));
            domain12tg.setTransform(domain12t3d);
        }

        catch (java.io.FileNotFoundException ex){
        }

        bounds = new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 100.0);

        AmbientLight lightForDomain12 = new AmbientLight();

        domainBlock12tg = new TransformGroup();
        domainBlock12t3d = new Transform3D();

        domainBlock12tg.addChild(domain12tg);

        domainBlock12t3d.mul(rotation12);
        Vector3f domainBlock12Position = new Vector3f(x,y,z);
        domainBlock12t3d.setTranslation(domainBlock12Position);
        domainBlock12tg.setTransform(domainBlock12t3d);

        lightForDomain12.setEnable(true);
        lightForDomain12.setColor(new Color3f(255/255f, 0/255f, 0/255f));
        lightForDomain12.setInfluencingBounds(bounds);
        lightForDomain12.addScope(domain12tg);
        lightForDomain12.addScope(domain12CoverCylinderTransformGroup);

//        masterTrans.addChild (lightForDomain12);
//        masterTrans.addChild(domainBlock12tg);
    }
    public static void getDomain34Block(float x, float y, float z){

        TransformGroup domain34tg = new TransformGroup();
        Transform3D domain34t3d = new Transform3D();

        Transform3D rotation34 = new Transform3D();
        rotation34.rotX(Math.PI/2);

        try
        {
            Scene scene1 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            String s1 = "HalfBrickDomain34.obj";
            scene1 = f.load (s1);

            domain34tg.addChild (scene1.getSceneGroup ());
            domain34t3d.mul(rotation34);
            domain34t3d.setTranslation(new Vector3f(-0.5f,0f,0f));
            domain34tg.setTransform(domain34t3d);

        }

        catch (java.io.FileNotFoundException ex){
        }

        AmbientLight lightForDomain34 = new AmbientLight();

        domainBlock34tg = new TransformGroup();
        domainBlock34t3d = new Transform3D();

        domainBlock34tg.addChild(domain34tg);

        domainBlock34t3d.mul(rotation34);
        Vector3f domainBlock34Position = new Vector3f(x,y,z);
        domainBlock34t3d.setTranslation(domainBlock34Position);
        domainBlock34tg.setTransform(domainBlock34t3d);

        lightForDomain34.setEnable(true);
        lightForDomain34.setColor(new Color3f(0/255f, 255/255f, 0/255f));
        lightForDomain34.setInfluencingBounds(bounds);
        lightForDomain34.addScope(domain34tg);

//        masterTrans.addChild (lightForDomain34);
//        masterTrans.addChild(domainBlock34tg);
    }

    public static void getDomain1234NorthBlock(float x, float y, float z){

        TransformGroup domain1234tg = new TransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234 = new Transform3D();
        rotation1234.rotX(Math.PI/2);

        try
        {
            Scene scene1 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            String s1 = "FullBrick.obj";
            scene1 = f.load (s1);

            domain1234tg.addChild (scene1.getSceneGroup ());
            domain1234t3d.mul(rotation1234);
            domain1234t3d.setTranslation(new Vector3f(-2.0f,0f,0f));
            domain1234tg.setTransform(domain1234t3d);

        }

        catch (java.io.FileNotFoundException ex){
        }

        lightForDomain1234North = new AmbientLight();

        domainBlock1234tg = new TransformGroup();
        domainBlock1234t3d = new Transform3D();

        domainBlock1234tg.addChild(domain1234tg);

        domainBlock1234t3d.mul(rotation1234);
        Vector3f domainBlock1234Position = new Vector3f(x,y,z*-1);
        domainBlock1234t3d.setTranslation(domainBlock1234Position);
        domainBlock1234tg.setTransform(domainBlock1234t3d);

        lightForDomain1234North.setEnable(true);
        lightForDomain1234North.setColor(new Color3f(0/255f, 0/255f, 255/255f));
        lightForDomain1234North.setInfluencingBounds(bounds);
        lightForDomain1234North.addScope(domain1234tg);

        masterTrans.addChild(lightForDomain1234North);
        masterTrans.addChild(domainBlock1234tg);
    }
    public static void getDomain1234WestBlock(float x, float y, float z){

        TransformGroup domain1234tg = new TransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234X = new Transform3D();
        Transform3D rotation1234Z = new Transform3D();
        rotation1234X.rotX(Math.PI/2);
        rotation1234Z.rotZ(Math.PI/2);

        try
        {
            Scene scene1 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            String s1 = "FullBrick.obj";
            scene1 = f.load (s1);

            domain1234tg.addChild (scene1.getSceneGroup ());
            domain1234t3d.mul(rotation1234X);
            domain1234t3d.mul(rotation1234Z);
            domain1234t3d.setTranslation(new Vector3f(-2.0f,0f,0f));
            domain1234tg.setTransform(domain1234t3d);

        }

        catch (java.io.FileNotFoundException ex){
        }

        lightForDomain1234West = new AmbientLight();

        domainBlock1234tg = new TransformGroup();
        domainBlock1234t3d = new Transform3D();

        domainBlock1234tg.addChild(domain1234tg);

        domainBlock1234t3d.mul(rotation1234X);

        Vector3f domainBlock1234Position = new Vector3f(x,y,z*-1);
        domainBlock1234t3d.setTranslation(domainBlock1234Position);
        domainBlock1234tg.setTransform(domainBlock1234t3d);

        lightForDomain1234West.setEnable(true);
        lightForDomain1234West.setColor(new Color3f(255/255f, 255/255f, 0/255f));
        lightForDomain1234West.setInfluencingBounds(bounds);
        lightForDomain1234West.addScope(domain1234tg);

        masterTrans.addChild(lightForDomain1234West);
        masterTrans.addChild(domainBlock1234tg);
    }
    public static void getDomain1234SouthBlock(float x, float y, float z){

        TransformGroup domain1234tg = new TransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234 = new Transform3D();
        rotation1234.rotX(Math.PI/2);

        try
        {
            Scene scene1 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            String s1 = "FullBrick.obj";
            scene1 = f.load (s1);

            domain1234tg.addChild (scene1.getSceneGroup ());
            domain1234t3d.mul(rotation1234);
            domain1234t3d.setTranslation(new Vector3f(-2.0f,0f,0f));
            domain1234tg.setTransform(domain1234t3d);

        }

        catch (java.io.FileNotFoundException ex){
        }

        lightForDomain1234South = new AmbientLight();

        domainBlock1234tg = new TransformGroup();
        domainBlock1234t3d = new Transform3D();

        domainBlock1234tg.addChild(domain1234tg);

        domainBlock1234t3d.mul(rotation1234);
        Vector3f domainBlock1234Position = new Vector3f(x,y,z*-1);
        domainBlock1234t3d.setTranslation(domainBlock1234Position);
        domainBlock1234tg.setTransform(domainBlock1234t3d);

        lightForDomain1234South.setEnable(true);
        lightForDomain1234South.setColor(new Color3f(255/255f, 0/255f, 255/255f));
        lightForDomain1234South.setInfluencingBounds(bounds);
        lightForDomain1234South.addScope(domain1234tg);

        masterTrans.addChild(lightForDomain1234South);
        masterTrans.addChild(domainBlock1234tg);
    }
    public static void getDomain1234EastBlock(float x, float y, float z){

        TransformGroup domain1234tg = new TransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234 = new Transform3D();
        rotation1234.rotX(Math.PI/2);

        try
        {
            Scene scene1 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            String s1 = "FullBrick.obj";
            scene1 = f.load (s1);

            domain1234tg.addChild (scene1.getSceneGroup ());
            domain1234t3d.mul(rotation1234);
            domain1234t3d.setTranslation(new Vector3f(-2.0f,0f,0f));
            domain1234tg.setTransform(domain1234t3d);

        }

        catch (java.io.FileNotFoundException ex){
        }

        lightForDomain1234East = new AmbientLight();

        domainBlock1234tg = new TransformGroup();
        domainBlock1234t3d = new Transform3D();

        domainBlock1234tg.addChild(domain1234tg);

        domainBlock1234t3d.mul(rotation1234);
        Vector3f domainBlock1234Position = new Vector3f(x,y,z*-1);
        domainBlock1234t3d.setTranslation(domainBlock1234Position);
        domainBlock1234tg.setTransform(domainBlock1234t3d);

        lightForDomain1234East.setEnable(true);
        lightForDomain1234East.setColor(new Color3f(0/255f, 255/255f, 255/255f));
        lightForDomain1234East.setInfluencingBounds(bounds);
        lightForDomain1234East.addScope(domain1234tg);

        masterTrans.addChild(lightForDomain1234East);
        masterTrans.addChild(domainBlock1234tg);
    }

    public static AmbientLight lightForDomain1234North;
    public static AmbientLight lightForDomain1234West;
    public static AmbientLight lightForDomain1234South;
    public static AmbientLight lightForDomain1234East;
    public static TransformGroup masterTrans;
    public static TransformGroup domainBlock12tg;
    public static Transform3D domainBlock12t3d;
    public static TransformGroup domainBlock34tg;
    public static Transform3D domainBlock34t3d;
    public static TransformGroup domainBlock1234tg;
    public static Transform3D domainBlock1234t3d;
    public static float canvasX;
    public static float canvasY;
    public static float canvasZ;
    public static BranchGroup objRoot;
    public static BranchGroup visualizeScene;
    public static ViewingPlatform viewingPlatform;
    public static Canvas3D visualizeCanvas;
    public static SimpleUniverse visualizationSimpleUniverse;
    public static JPanel visualizeFrameBasePanel;
    public static JFrame visualizeFrame;
    public static JToolBar leftJToolBar;
    public static JToolBar rightJToolBar;
    public static JButton zoomInButton;
    public static JButton zoomOutButton;
    public static JButton upButton;
    public static JButton downButton;
    public static JButton rightButton;
    public static JButton leftButton;
    public static JButton resetButton;
    public static MouseRotate rotateBehaviour;
    public static BoundingSphere bounds;
    public static Boolean isCompleted = false;
    public static float visualizeCanvasStep = 1.00f;
}
