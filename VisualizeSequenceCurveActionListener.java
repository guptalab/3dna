/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import javax.media.j3d.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class VisualizeSequenceCurveActionListener implements ActionListener, MouseListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        isVisualizePlaneSelected=true;
        visualizePlaneFrame = new JFrame("3DNA Sequence Curve Visualization");
        visualizePlaneFrame.setVisible(true);
        visualizePlaneFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        visualizePlaneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Initialize the visualizeFrameBasePanel
        visualizePlaneFrameBasePanel = new JPanel();
        visualizePlaneFrameBasePanel.setLayout(new BorderLayout());
        visualizePlaneFrame.getContentPane().add(visualizePlaneFrameBasePanel);

        //Initialize LeftJToolbar
        initializeLeftJToolBar();
        visualizePlaneFrameBasePanel.add(leftJToolBar,BorderLayout.WEST);

        //Initialize RightJToolbar
        initializeRightJToolBar();
        visualizePlaneFrameBasePanel.add(rightJToolBar,BorderLayout.EAST);
        VisualizeSequenceCurveActionListener.printSuccess("Visualization Canvas has been initiated....");

        //Initialize Canvas3D
        initializeCanvas3D();
        visualizePlaneFrameBasePanel.add(visualizePlaneCanvas,BorderLayout.CENTER);
    }
    public static void initializeRightJToolBar(){
        rightJToolBar = new JToolBar("Visualization Status Toolbar", JToolBar.VERTICAL);
        rightJToolBar.setBackground(Color.DARK_GRAY);

        removeFocusButton = new JButton("<html><style>h4{color:white;}</style><h4> Remove Focus </h4></html>");
        removeFocusButton.setBackground(Color.DARK_GRAY);
        removeFocusButton.setEnabled(false);
        removeFocusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                removeFocusButton.setEnabled(false);
                for (int i =0; masterTrans.getAllChildren().hasMoreElements();i++){
                    try {
                        if(masterTrans.getChild(i).getClass().getName().equals("javax.media.j3d.BranchGroup")){
                            masterTrans.removeChild(i);
                            isHighlightSelected=false;
                            break;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Index out of bounds");
                    }
                }
            }
        });

        visualizePlaneLog = new JTextPane();
        visualizePlaneLog.setEditable(false);
        visualizePlaneDoc = visualizePlaneLog.getStyledDocument();
        visualizePlaneStyle = visualizePlaneLog.addStyle("3DNA Style", null);
        visualizePlaneLog.setBackground(Color.DARK_GRAY);
        visualizePlaneLogScrollPane = new JScrollPane(visualizePlaneLog);
        visualizePlaneLogScrollPane.setBackground(Color.DARK_GRAY);

        rightJToolBar.add(removeFocusButton);
        rightJToolBar.add(visualizePlaneLogScrollPane);
    }

    public void initializeLeftJToolBar(){
        leftJToolBar = new JToolBar("Visualization Control Toolbar", JToolBar.VERTICAL);
        leftJToolBar.setBackground(Color.DARK_GRAY);

        // Add leftJToolBar Images and Buttons
        ImageIcon zoominIcon = new ImageIcon("icons/zoomin.png");
        ImageIcon zoomoutIcon = new ImageIcon("icons/zoomout.png");
        ImageIcon upArrowIcon = new ImageIcon("icons/arrow-up.png");
        ImageIcon downArrowIcon = new ImageIcon("icons/arrow-down.png");
        ImageIcon leftArrowIcon = new ImageIcon("icons/arrow-left.png");
        ImageIcon rightArrowIcon = new ImageIcon("icons/arrow-right.png");
        ImageIcon previousIcon = new ImageIcon("icons/previous.png");
        ImageIcon nextIcon = new ImageIcon("icons/next.png");

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
        previousPlaneButton = new JButton(previousIcon);
        previousPlaneButton.setBackground(Color.DARK_GRAY);
        nextPlaneButton= new JButton(nextIcon);
        nextPlaneButton.setBackground(Color.DARK_GRAY);

        zoomInButton.addActionListener(new VisualizationZoomInActionListener());
        zoomOutButton.addActionListener(new VisualizationZoomOutActionListener());
        upButton.addActionListener(new VisualizationUpButtonActionListener());
        downButton.addActionListener(new VisualizationDownButtonActionListener());
        leftButton.addActionListener(new VisualizationLeftButtonActionListener());
        rightButton.addActionListener(new VisualizationRightButtonActionListener());
        nextPlaneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPlane++;
                if(displayPlane<=MainFrame.depth){
                    visualizePlaneScene.detach();
                    visualizePlaneScene=createSceneGraph();
                    visualizePlaneScene.setCapability(BranchGroup.ALLOW_DETACH);
                    visualizePlaneScene.compile();
                    visualizationPlaneSimpleUniverse.addBranchGraph(visualizePlaneScene);
                    visualizePlanePickCanvas= new PickCanvas(visualizePlaneCanvas, visualizePlaneScene);
                    visualizePlanePickCanvas.setMode(PickCanvas.GEOMETRY);
                }else {
                    JOptionPane.showMessageDialog(null, "Canvas plane at maximum depth",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        previousPlaneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPlane--;
                if(displayPlane>=1){
                    visualizePlaneScene.detach();
                    visualizePlaneScene=createSceneGraph();
                    visualizePlaneScene.setCapability(BranchGroup.ALLOW_DETACH);
                    visualizePlaneScene.compile();
                    visualizationPlaneSimpleUniverse.addBranchGraph(visualizePlaneScene);
                    visualizePlanePickCanvas= new PickCanvas(visualizePlaneCanvas, visualizePlaneScene);
                    visualizePlanePickCanvas.setMode(PickCanvas.GEOMETRY);
                    VisualizeSequenceCurveActionListener.printMessage("Plane Number: "+displayPlane);
                }else {
                    JOptionPane.showMessageDialog(null, "Canvas plane at minimum depth",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        leftJToolBar.add(zoomInButton);
        leftJToolBar.add(zoomOutButton);
        leftJToolBar.add(upButton);
        leftJToolBar.add(leftButton);
        leftJToolBar.add(rightButton);
        leftJToolBar.add(downButton);
        leftJToolBar.add(previousPlaneButton);
        leftJToolBar.add(nextPlaneButton);

    }

    public static void  printSuccess (String text){
        visualizePlaneStyle.addAttribute(StyleConstants.FontFamily, "Lucida Console");
        visualizePlaneStyle.addAttribute(StyleConstants.FontSize, new Integer(14));
        StyleConstants.setForeground(visualizePlaneStyle, Color.CYAN);
        try { visualizePlaneDoc.insertString(visualizePlaneDoc.getLength(), ">>>" +text + "\n",visualizePlaneStyle);
            visualizePlaneLog.setCaretPosition(visualizePlaneLog.getDocument().getLength());}
        catch (BadLocationException e){}
    }

    public static void  printMessage (String text){
        visualizePlaneStyle.addAttribute(StyleConstants.FontFamily, "Lucida Console");
        visualizePlaneStyle.addAttribute(StyleConstants.FontSize, new Integer(14));
        StyleConstants.setForeground(visualizePlaneStyle, Color.GREEN);
        try { visualizePlaneDoc.insertString(visualizePlaneDoc.getLength(), ">>>" +text + "\n",visualizePlaneStyle);
            visualizePlaneLog.setCaretPosition(visualizePlaneLog.getDocument().getLength());}
        catch (BadLocationException e){}
    }

    public static void  printAlert (String text){
        visualizePlaneStyle.addAttribute(StyleConstants.FontFamily, "Lucida Console");
        visualizePlaneStyle.addAttribute(StyleConstants.FontSize, new Integer(14));
        StyleConstants.setForeground(visualizePlaneStyle, Color.PINK);
        try { visualizePlaneDoc.insertString(visualizePlaneDoc.getLength(), ">>>" +text + "\n",visualizePlaneStyle);
            visualizePlaneLog.setCaretPosition(visualizePlaneLog.getDocument().getLength());}
        catch (BadLocationException e){}
    }

    public void initializeCanvas3D(){
        visualizePlaneCanvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        visualizationPlaneSimpleUniverse = new SimpleUniverse(visualizePlaneCanvas);
        //initialize viewing platform
        viewingPlatform = visualizationPlaneSimpleUniverse.getViewingPlatform();

        //define background colors for the Canvas
        BranchGroup backgroundBranchGroup = new BranchGroup();
        Background background = new Background(new Color3f(1f,1f,1f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000);
        background.setApplicationBounds(sphere);
        backgroundBranchGroup.addChild(background);
        viewingPlatform.addChild(backgroundBranchGroup);

        //setting the Viewing Platform with the initial values
        canvasX = 2.0f;
        canvasY = 2.0f;
        canvasZ = 30.0f;
        TransformGroup View_TransformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated
        Transform3D View_Transform3D = new Transform3D();    // create a Transform3D for the ViewingPlatform
        View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform
        View_Transform3D.setTranslation(new Vector3f(canvasX,canvasY,canvasZ)); // set 3d to  x=0, y=0, z=20
        View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform

        //initialize visualizeScene
        visualizePlaneScene = createSceneGraph();
        visualizePlaneScene.setCapability(BranchGroup.ALLOW_DETACH);
        visualizePlaneScene.compile();
        visualizationPlaneSimpleUniverse.addBranchGraph(visualizePlaneScene);
        visualizePlanePickCanvas= new PickCanvas(visualizePlaneCanvas, visualizePlaneScene);
        visualizePlanePickCanvas.setMode(PickCanvas.GEOMETRY);
        visualizePlaneCanvas.addMouseListener(this);
    }


    public static BranchGroup createSceneGraph() {

        objRoot = new BranchGroup();
        objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
        masterTrans = new TransformGroup();

        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        masterTrans.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        masterTrans.setCapability(Group.ALLOW_CHILDREN_WRITE);

        rotateBehaviour=new MouseRotate();
        rotateBehaviour.setTransformGroup(masterTrans);
        rotateBehaviour.setSchedulingBounds(new BoundingSphere());

        masterTrans.addChild(rotateBehaviour);

        int x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4;
        int canvasDomainXCoordinate, canvasDomainYCoordinate, canvasDomainZCoordinate;
        ArrayList<VoxelToBrick> dnaSequenceDate = ImportSequencesCoordinatesMap.brickList;

        for (int i = 0; i < dnaSequenceDate.size(); i++) {
            //identify and print for domains 1 and 2(HalfBrick)
            if (dnaSequenceDate.get(i).x3 == -1 && dnaSequenceDate.get(i).z2 == displayPlane ) {
                x1 = dnaSequenceDate.get(i).x1;
                y1 = dnaSequenceDate.get(i).y1;
                z1 = dnaSequenceDate.get(i).z1;

                x2 = dnaSequenceDate.get(i).x2;
                y2 = dnaSequenceDate.get(i).y2;
                z2 = dnaSequenceDate.get(i).z2;

                System.out.println("Printing Domain12HalfBrick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2);

                canvasDomainXCoordinate = x2;
                canvasDomainYCoordinate = y2;
                canvasDomainZCoordinate = z2;

                //Code for Domain12
//                getDomain12Block((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate, dnaSequenceDate.get(i).getCompleteSequence());
            }else if (dnaSequenceDate.get(i).x1 == -1 && dnaSequenceDate.get(i).z3 == displayPlane) {//identify and print for domains 3 and 4(HalfBrick)
                x3 = dnaSequenceDate.get(i).x3;
                y3 = dnaSequenceDate.get(i).y3;
                z3 = dnaSequenceDate.get(i).z3;

                x4 = dnaSequenceDate.get(i).x4;
                y4 = dnaSequenceDate.get(i).y4;
                z4 = dnaSequenceDate.get(i).z4;

                System.out.println("Printing Domain34HalfBrick"+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4);

                canvasDomainXCoordinate = x3;
                canvasDomainYCoordinate = y3;
                canvasDomainZCoordinate = z3;

                //Code for Domain34
//                getDomain34Block((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate ,dnaSequenceDate.get(i).getCompleteSequence());
            }else {//identify and print for full bricks(FullBrick)
                x1 = dnaSequenceDate.get(i).x1;
                y1 = dnaSequenceDate.get(i).y1;
                z1 = dnaSequenceDate.get(i).z1;

                x2 = dnaSequenceDate.get(i).x2;
                y2 = dnaSequenceDate.get(i).y2;
                z2 = dnaSequenceDate.get(i).z2;

                x3 = dnaSequenceDate.get(i).x3;
                y3 = dnaSequenceDate.get(i).y3;
                z3 = dnaSequenceDate.get(i).z3;

                x4 = dnaSequenceDate.get(i).x4;
                y4 = dnaSequenceDate.get(i).y4;
                z4 = dnaSequenceDate.get(i).z4;

                //Code for adding Domain1234(FullBrick) based on their orientation

                if(z2%4 == 0 && dnaSequenceDate.get(i).z2 == displayPlane){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;
                    System.out.println("Printing North Brick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4);
//                    getDomain1234NorthBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate, dnaSequenceDate.get(i).getCompleteSequence());
                }else if(z2%4 == 1 && dnaSequenceDate.get(i).z2 == displayPlane ){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;
                    System.out.println("Printing West Brick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4);
                    getDomain1234WestBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate, dnaSequenceDate.get(i).getCompleteSequence());
                }else if(z2%4 == 2 && dnaSequenceDate.get(i).z2 == displayPlane){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;
                    System.out.println("Printing South Brick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4);
//                    getDomain1234SouthBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate,dnaSequenceDate.get(i).getCompleteSequence());
                }else if(z2%4 == 3 && dnaSequenceDate.get(i).z2 == displayPlane){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;
                    System.out.println("Printing East Brick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4);
//                    getDomain1234EastBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate,dnaSequenceDate.get(i).getCompleteSequence());
                }
            }
        }
        /**Code for creating 3DNA Domain Blocks
         *The building units of the Visualization class comprises of 3 modular Blocks viz. domain12Block, domain34Block,
         *  domain1234Block
         *  Each block has its own domainXX[XX]tg, domainXX[XX]t3d, domainxx[XX]CoverBoxTransformGroup,
         *  domainxx[XX]CoverBoxTransform3D, domainxx[XX]CoverBox, lightForDomainxx[XX].
         *  The final parameters which will be used for creating the visualization canvas will include:
         *  domainBlockxx[XX]tg and domainBlockxx[XX]t3d
         */
        objRoot.addChild(masterTrans);
        return objRoot;
    }


    public static void getDomain12Block(float x, float y, float z, String sequence){

    }
    public static void getDomain34Block(float x, float y, float z, String sequence){

    }

    public static void getDomain1234NorthBlock(float x, float y, float z, String sequence){
        DNATransformGroup domain1234CoverBoxTransformGroup = new DNATransformGroup();
        Transform3D domain1234CoverBoxTransform3D = new Transform3D();

        TransformGroup domain1234tg = new TransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        domain1234CoverBoxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234CoverBoxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);



        lightForDomain1234North = new AmbientLight();

        domainBlock1234tg = new DNATransformGroup();
        domainBlock1234t3d = new Transform3D();

        domainBlock1234tg.addChild(domain1234tg);
        domainBlock1234tg.addChild(domain1234CoverBoxTransformGroup);
        domainBlock1234tg.setSequence(sequence);
        domainBlock1234tg.setCordinate(x,y,z);
        domainBlock1234tg.isHalfBrick=false;

        domainBlock1234tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domainBlock1234tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domainBlock1234tg.setCapability(Node.ENABLE_PICK_REPORTING);

        Vector3f domainBlock1234Position = new Vector3f(1f+z,MainFrame.height-y+0.5f,x);
        domainBlock1234t3d.setTranslation(domainBlock1234Position);
        domainBlock1234tg.setTransform(domainBlock1234t3d);

        lightForDomain1234North.setEnable(true);
        lightForDomain1234North.setColor(new Color3f(0/255f, 0/255f, 255/255f));
        lightForDomain1234North.setInfluencingBounds(bounds);
        lightForDomain1234North.addScope(domain1234tg);
        lightForDomain1234North.addScope(domain1234CoverBoxTransformGroup);

        masterTrans.addChild(lightForDomain1234North);
        masterTrans.addChild(domainBlock1234tg);
    }
    public static void getDomain1234WestBlock(float x, float y, float z, String sequence){


    }
    public static void getDomain1234SouthBlock(float x, float y, float z, String sequence){
        DNATransformGroup domain1234CoverBoxTransformGroup = new DNATransformGroup();
        Transform3D domain1234CoverBoxTransform3D = new Transform3D();

        TransformGroup domain1234tg = new TransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234 = new Transform3D();
        rotation1234.rotX(Math.PI);

        domain1234CoverBoxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234CoverBoxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        try
        {
            Scene scene1 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            String s1 = "FullBrick.obj";
            scene1 = f.load (s1);

            domain1234tg.addChild (scene1.getSceneGroup ());
            domain1234tg.setTransform(domain1234t3d);

            Appearance ap = new Appearance();

            Color3f col = new Color3f(1.0f, 0.0f, 1.0f);
            ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);
            ap.setColoringAttributes(ca);

            TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
            ap.setTransparencyAttributes( transparencyAttributes );

            com.sun.j3d.utils.geometry.Box domain1234CoverBox = new com.sun.j3d.utils.geometry.Box(1f, 1f, 0.5f, ap);
            domain1234CoverBoxTransformGroup.setTransform(domain1234CoverBoxTransform3D);
            domain1234CoverBoxTransformGroup.addChild(domain1234CoverBox);
        }

        catch (java.io.FileNotFoundException ex){
        }

        lightForDomain1234South = new AmbientLight();

        domainBlock1234tg = new DNATransformGroup();
        domainBlock1234t3d = new Transform3D();

        domainBlock1234tg.addChild(domain1234tg);
        domainBlock1234tg.addChild(domain1234CoverBoxTransformGroup);
        domainBlock1234tg.setSequence(sequence);
        domainBlock1234tg.setCordinate(x,y,z);
        domainBlock1234tg.isHalfBrick=false;

        domainBlock1234tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domainBlock1234tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domainBlock1234tg.setCapability(Node.ENABLE_PICK_REPORTING);

        domainBlock1234t3d.mul(rotation1234);
        Vector3f domainBlock1234Position = new Vector3f(1f+z,MainFrame.height-y-0.5f,x);
        domainBlock1234t3d.setTranslation(domainBlock1234Position);
        domainBlock1234tg.setTransform(domainBlock1234t3d);

        lightForDomain1234South.setEnable(true);
        lightForDomain1234South.setColor(new Color3f(255/255f, 0/255f, 255/255f));
        lightForDomain1234South.setInfluencingBounds(bounds);
        lightForDomain1234South.addScope(domain1234tg);
        lightForDomain1234South.addScope(domain1234CoverBoxTransformGroup);

        masterTrans.addChild(lightForDomain1234South);
        masterTrans.addChild(domainBlock1234tg);
    }
    public static void getDomain1234EastBlock(float x, float y, float z, String sequence){
        DNATransformGroup domain1234CoverBoxTransformGroup = new DNATransformGroup();
        Transform3D domain1234CoverBoxTransform3D = new Transform3D();

        TransformGroup domain1234tg = new TransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234X = new Transform3D();
        rotation1234X.rotX(3*Math.PI/2);

        domain1234CoverBoxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234CoverBoxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        try
        {
            Scene scene1 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            String s1 = "FullBrick.obj";
            scene1 = f.load (s1);

            domain1234tg.addChild (scene1.getSceneGroup ());
            domain1234tg.setTransform(domain1234t3d);

            Appearance ap = new Appearance();

            Color3f col = new Color3f(0.0f, 1.0f, 1.0f);
            ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);
            ap.setColoringAttributes(ca);

            TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
            ap.setTransparencyAttributes( transparencyAttributes );

            com.sun.j3d.utils.geometry.Box domain1234CoverBox = new com.sun.j3d.utils.geometry.Box(1f, 1f, 0.5f, ap);
            domain1234CoverBoxTransformGroup.setTransform(domain1234CoverBoxTransform3D);
            domain1234CoverBoxTransformGroup.addChild(domain1234CoverBox);        }

        catch (java.io.FileNotFoundException ex){
        }

        lightForDomain1234East = new AmbientLight();

        domainBlock1234tg = new DNATransformGroup();
        domainBlock1234t3d = new Transform3D();

        domainBlock1234tg.addChild(domain1234tg);
        domainBlock1234tg.addChild(domain1234CoverBoxTransformGroup);
        domainBlock1234tg.setSequence(sequence);
        domainBlock1234tg.setCordinate(x,y,z);
        domainBlock1234tg.isHalfBrick=false;

        domainBlock1234tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domainBlock1234tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domainBlock1234tg.setCapability(Node.ENABLE_PICK_REPORTING);

        domainBlock1234t3d.mul(rotation1234X);
        Vector3f domainBlock1234Position = new Vector3f(1f+z,MainFrame.height-y,x+0.5f);
        domainBlock1234t3d.setTranslation(domainBlock1234Position);
        domainBlock1234tg.setTransform(domainBlock1234t3d);

        lightForDomain1234East.setEnable(true);
        lightForDomain1234East.setColor(new Color3f(0/255f, 255/255f, 255/255f));
        lightForDomain1234East.setInfluencingBounds(bounds);
        lightForDomain1234East.addScope(domain1234tg);
        lightForDomain1234East.addScope(domain1234CoverBoxTransformGroup);

        masterTrans.addChild(lightForDomain1234East);
        masterTrans.addChild(domainBlock1234tg);

    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isHighlightSelected) {
            visualizePlanePickCanvas.setShapeLocation(e);
            PickResult mouseClickResult = visualizePlanePickCanvas.pickClosest();
            if (mouseClickResult == null) {
                int xCord=e.getX();
                int yCord=e.getY();
                System.out.println("Nothing selected at xCord "+xCord+" yCord"+yCord);
            } else {
                isHighlightSelected=true;
                DNATransformGroup pickedSequence = (DNATransformGroup)mouseClickResult.getNode(PickResult.TRANSFORM_GROUP);
                if (pickedSequence!= null) {
                    VisualizeSequenceCurveActionListener.printSuccess(pickedSequence.getSequence());
                    Sphere sphere = new Sphere(1.0f);
                    Appearance sphereAppearance = new Appearance();
                    Color3f col = new Color3f(0.0f, 0.0f, 1.0f);
                    ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);
                    sphereAppearance.setColoringAttributes(ca);
                    TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.6f);
                    sphereAppearance.setTransparencyAttributes(transparencyAttributes);
                    sphere.setAppearance(sphereAppearance);

                    TransformGroup sphereTransformGroup= new TransformGroup();
                    Transform3D sphereTransform3D = new Transform3D();
                    sphereTransformGroup.addChild(sphere);
                    Vector3f spherePosition;
                    if(pickedSequence.isHalfBrick){
                        if(pickedSequence.isDomain12){
                            spherePosition = new Vector3f(pickedSequence.zCord -0.5f, MainFrame.height -pickedSequence.yCord, pickedSequence.xCord);
                        } else {
                            spherePosition = new Vector3f(pickedSequence.zCord -0.5f, MainFrame.height -pickedSequence.yCord, pickedSequence.xCord);
                        }
                    }else{
                        if(!pickedSequence.isHalfBrick && pickedSequence.zCord%4 ==0){
                            spherePosition = new Vector3f(pickedSequence.zCord, MainFrame.height -pickedSequence.yCord+0.5f, pickedSequence.xCord);
                        }
                        else if(!pickedSequence.isHalfBrick && pickedSequence.zCord%4 ==1){
                            spherePosition = new Vector3f(pickedSequence.zCord -0.5f , MainFrame.height -pickedSequence.yCord, pickedSequence.xCord - 0.5f);
                        }
                        else if(!pickedSequence.isHalfBrick && pickedSequence.zCord%4 ==2){
                            spherePosition = new Vector3f(pickedSequence.zCord, MainFrame.height -pickedSequence.yCord -0.5f, pickedSequence.xCord);
                        }
                        else{
                            spherePosition = new Vector3f(pickedSequence.zCord , MainFrame.height -pickedSequence.yCord, pickedSequence.xCord + 0.5f);
                        }
                    }
                    sphere.setBounds(pickedSequence.getBounds());
                    sphereTransform3D.setTranslation(spherePosition);
                    sphereTransformGroup.setTransform(sphereTransform3D);

                    BranchGroup coverSphereBranchGroup = new BranchGroup();
                    coverSphereBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
                    coverSphereBranchGroup.addChild(sphereTransformGroup);
                    removeFocusButton.setEnabled(true);
                    masterTrans.addChild(coverSphereBranchGroup);
                }else{
                    VisualizeSequenceCurveActionListener.printAlert("Unidentified Class");
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static JTextPane visualizePlaneLog;
    public static StyledDocument visualizePlaneDoc;
    public static Style visualizePlaneStyle;
    public static JScrollPane visualizePlaneLogScrollPane;
    public static AmbientLight lightForDomain1234North;
    public static AmbientLight lightForDomain1234West;
    public static AmbientLight lightForDomain1234South;
    public static AmbientLight lightForDomain1234East;
    public static TransformGroup masterTrans;
    public static DNATransformGroup domainBlock12tg;
    public static Transform3D domainBlock12t3d;
    public static DNATransformGroup domainBlock34tg;
    public static Transform3D domainBlock34t3d;
    public static DNATransformGroup domainBlock1234tg;
    public static Transform3D domainBlock1234t3d;
    public static float canvasX;
    public static float canvasY;
    public static float canvasZ;
    public static BranchGroup objRoot;
    public static BranchGroup visualizePlaneScene;
    public static ViewingPlatform viewingPlatform;
    public static Canvas3D visualizePlaneCanvas;
    public static SimpleUniverse visualizationPlaneSimpleUniverse;
    public static JPanel visualizePlaneFrameBasePanel;
    public static JFrame visualizePlaneFrame;
    public static JToolBar leftJToolBar;
    public static JToolBar rightJToolBar;
    public static JButton zoomInButton;
    public static JButton zoomOutButton;
    public static JButton upButton;
    public static JButton downButton;
    public static JButton rightButton;
    public static JButton leftButton;
    public static JButton previousPlaneButton;
    public static JButton nextPlaneButton;
    public static MouseRotate rotateBehaviour;
    public static BoundingSphere bounds;
    public static Boolean isHighlightSelected= false;
    public static float visualizePlaneCanvasStep = 1.00f;
    public static PickCanvas visualizePlanePickCanvas;
    public static JButton removeFocusButton;
    public static boolean isVisualizePlaneSelected=false;
    public static int displayPlane = 1;
}

