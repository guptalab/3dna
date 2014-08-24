/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import sun.applet.Main;

public class CanvasActionListener extends MouseAdapter implements ActionListener {
    static int pressed=0;
    public static int lastx;
    public static int lasty;
    public static int lastz;
    public static PickCanvas pickCanvas;
    public static BranchGroup objRoot;
    public static Transform3D transform;
    public static TransformGroup tg;
    public static SimpleUniverse simpleU;
    public static MouseRotate rotateBehaviour;
    public static TransformGroup masterTrans;
    public static BranchGroup scene;
    public static float canvasx;
    public static float canvasy;
    public static float canvasz;
    public static boolean isDimensionEntered=false;

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        if (!MainFrame.isProjectCreated) {

            MainFrame.projectName=JOptionPane.showInputDialog(null,"Please enter a name for your project","3DNA Project details",
                    JOptionPane.QUESTION_MESSAGE);
            if(MainFrame.projectName==null){

            }
            else{
                MainFrame.projectPath=System.getProperty("user.home")+"/Desktop/3DNA_Workspace/"+MainFrame.projectName;
                File projectDirectory=new File(MainFrame.projectPath);
                projectDirectory.mkdirs();

                JOptionPane.showMessageDialog(null,"A new project has been created in your 3DNA workspace", "3DNA Project",
                        JOptionPane.INFORMATION_MESSAGE);
                MainFrame.isProjectCreated=true;
            }
            if(MainFrame.isProjectCreated){
                final JFrame dimensionFrame = new JFrame("3DNA Dimensions");
                ImageIcon img = new ImageIcon("images/logod.png");
                Image imag=img.getImage();
                dimensionFrame.setIconImage(imag);
                dimensionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new GridBagLayout());
                GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
                dimensionFrameGridBagConstraints.insets = new Insets(5, 5, 5, 5);

                final JLabel canvasHeightLabel = new JLabel("3DNA Canvas Height");
                JLabel canvasWidthLabel = new JLabel("3DNA Canvas Width");
                JLabel canvasDepthLabel = new JLabel("3DNA Canvas Depth");

                final JTextField canvasHeightTextField = new JTextField(5);
                final JTextField canvasWidthTextField = new JTextField(5);
                final JTextField canvasDepthTextField = new JTextField(5);

                JButton okButton = new JButton("OK");
                JButton cancelButton = new JButton("Cancel");
                JLabel infoHeightLabel =new JLabel(new ImageIcon("images/INFO.png"));
                JLabel infoWidthLabel =new JLabel(new ImageIcon("images/INFO.png"));
                JLabel infoDepthLabel =new JLabel(new ImageIcon("images/INFO.png"));

                infoHeightLabel.setToolTipText("Height to be entered in terms of DNA Helices");
                infoWidthLabel.setToolTipText("Width to be entered in terms of DNA Helices");
                infoDepthLabel.setToolTipText("Depth to be entered in terms of BP (a multiple of 8");

                dimensionFrameGridBagConstraints.gridx = 2;
                dimensionFrameGridBagConstraints.gridy = 0;
                mainPanel.add(infoHeightLabel, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 2;
                dimensionFrameGridBagConstraints.gridy = 1;
                mainPanel.add(infoWidthLabel, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 2;
                dimensionFrameGridBagConstraints.gridy = 2;
                mainPanel.add(infoDepthLabel, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 0;
                dimensionFrameGridBagConstraints.gridy = 0;
                mainPanel.add(canvasHeightLabel, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 0;
                dimensionFrameGridBagConstraints.gridy = 1;
                mainPanel.add(canvasWidthLabel, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 0;
                dimensionFrameGridBagConstraints.gridy = 2;
                mainPanel.add(canvasDepthLabel, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 1;
                dimensionFrameGridBagConstraints.gridy = 0;
                mainPanel.add(canvasHeightTextField, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 1;
                dimensionFrameGridBagConstraints.gridy = 1;
                mainPanel.add(canvasWidthTextField, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 1;
                dimensionFrameGridBagConstraints.gridy = 2;
                mainPanel.add(canvasDepthTextField, dimensionFrameGridBagConstraints);

                dimensionFrameGridBagConstraints.gridx = 1;
                dimensionFrameGridBagConstraints.gridy = 3;
                mainPanel.add(okButton, dimensionFrameGridBagConstraints);

                dimensionFrame.add(mainPanel, BorderLayout.CENTER);
                dimensionFrame.pack();
                dimensionFrame.setVisible(true);
                dimensionFrame.setLocation(500, 300);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(Integer.parseInt(canvasDepthTextField.getText())%8==0){
                            isDimensionEntered=true;
                            System.out.println("the value of isDimensionEntered is true");
                            MainFrame.height=Integer.parseInt(canvasHeightTextField.getText());
                            MainFrame.width=Integer.parseInt(canvasWidthTextField.getText());
                            MainFrame.depth=Integer.parseInt(canvasDepthTextField.getText())/8;
                            dimensionFrame.dispose();
                            for(int i=0;i<MainFrame.width;i++)
                                for(int j=0;j<MainFrame.height;j++)
                                    for (int k=0;k<MainFrame.depth;k++)
                                        MainFrame.StoreCoordinates[i][j][k]=false;

                            createCanvas();
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Canvas Depth should be a multiple of 8","3DNA Dimension Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dimensionFrame.dispose();
                    }
                });
            }
        }
    }

    public void createCanvas(){
        MainFrame.TrueEnableContent();
        // Canvas3D is where all the action will be taking place, don't worry, after adding it
        // to your layout, you don't have to touch it.
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        // add Canvas3D

        MainFrame.vPanel.setTopComponent(canvas);

        simpleU = new SimpleUniverse(canvas); // setup the SimpleUniverse, attach the Canvas3D

        //This is very important, the SceneGraph (where all the action takes place) is created
        //by calling a function which here is called 'createSceneGraph'.
        //The function is not necessary, you can put all your code here, but it is a
        //standard in Java3D to create your SceneGraph contents in the function 'createSceneGraph'

        scene = createSceneGraph();
        //set the ViewingPlatform by setting the canvasx, canvasy, canvasz values as 0.0,0.0,2.0
        canvasx = 5.0f;
        canvasy = 5.0f;
        canvasz = 40.0f;

        ViewingPlatform viewingPlatform = CanvasActionListener.simpleU.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse
        BranchGroup backgroundBranchGroup = new BranchGroup();
        //adding Dark Slate Gray as Simple Universe Color
        Background background = new Background(new Color3f(47/255f,79/255f,79/255f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000);
        background.setApplicationBounds(sphere);
        backgroundBranchGroup.addChild(background);
        viewingPlatform.addChild(backgroundBranchGroup);

        TransformGroup View_TransformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

        Transform3D View_Transform3D = new Transform3D();    // create a Transform3D for the ViewingPlatform
        View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform

        View_Transform3D.setTranslation(new Vector3f(canvasx, canvasy, canvasz)); // set 3d to  x=0, y=-1, z= 5
        View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform

        // this will optimize your SceneGraph, not necessary, but it will allow your program to run faster.
        scene.compile();
        simpleU.addBranchGraph(scene); //add your SceneGraph to the SimpleUniverse

        //We now add a pick canvas so that we can get
        pickCanvas = new PickCanvas(canvas, scene);
        pickCanvas.setMode(PickCanvas.GEOMETRY);
        canvas.addMouseListener(this);
    }

    public static BranchGroup createSceneGraph() {

        // This BranchGroup is the root of the SceneGraph, 'objRoot' is the name I use,
        // and it is typically the standard name for it, but can be named anything.
        objRoot = new BranchGroup();
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        objRoot.setCapability(BranchGroup.ALLOW_PICKABLE_WRITE);
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(BranchGroup.ALLOW_DETACH);
        objRoot.setCapability(Node.ENABLE_PICK_REPORTING);
        getNewBoxPlane();//creates a plane of cubes, planes can be combined to form the full fledged canvas.

        return objRoot;
    }
    public static void getNewBoxPlane(){
        masterTrans=new TransformGroup();
        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        masterTrans.setCapability(Node.ENABLE_PICK_REPORTING);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        if(pressed==0){
            for(int i=0;i<MainFrame.width;i++)
                for(int j=0;j<MainFrame.height;j++)
                    for (int k=0;k<MainFrame.depth;k++)
                        MainFrame.StoreCoordinates[i][j][k]=false;
        }
        float positionOffset=2.105f;

        for(int i=0;i<MainFrame.width;i++){
            for(int j=0;j<MainFrame.height;j++){
                for (int k=0;k<MainFrame.depth;k++){
                    //if(MainFrame.StoreCoordinates[i][j][k]==false){
                    DNAColorCube c1 = new DNAColorCube();
                    c1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                    c1.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
                    c1.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
                    c1.setCapability(Node.ENABLE_PICK_REPORTING);
                    c1.setCordinate(i, j-MainFrame.height+1, k-MainFrame.depth+1);
                    tg = new TransformGroup();
                    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    transform = new Transform3D();
                    Vector3f vector = new Vector3f(i*positionOffset,j*positionOffset,k*positionOffset);
                    transform.setTranslation(vector);
                    tg.addChild(c1);
                    tg.setTransform(transform);
                    masterTrans.addChild(tg);

                }
            }
        }

        //Adding molecular scale and axis to the master TransformGroup
        LineArray xAxisLineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray yAxisLineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray zAxisLineArray = new LineArray(2, LineArray.COORDINATES);

        xAxisLineArray.setCoordinate(0, new Point3f(0.0f,(float)(MainFrame.height+2),(float)(MainFrame.depth+2)));
        xAxisLineArray.setCoordinate(1, new Point3f(MainFrame.width+4,(float)(MainFrame.height+2),(float)(MainFrame.depth+2)));

        yAxisLineArray.setCoordinate(0, new Point3f(0.0f,(float)(MainFrame.height+2),(float)(MainFrame.depth+2)));
        yAxisLineArray.setCoordinate(1, new Point3f(0.0f,MainFrame.height*-1+(-1*2),(float)(MainFrame.depth+2)));

        zAxisLineArray.setCoordinate(0, new Point3f(0.0f,(float)(MainFrame.height+2),(float)(MainFrame.depth+2)));
        zAxisLineArray.setCoordinate(1, new Point3f(0.0f,(float)(MainFrame.height+2),MainFrame.depth*-1+(-1*2)));

        masterTrans.addChild(new Shape3D(xAxisLineArray));
        masterTrans.addChild(new Shape3D(yAxisLineArray));
        masterTrans.addChild(new Shape3D(zAxisLineArray));

        //Adding Text3D objects to label x,y and z axis
        Font font=new Font("Label Font",Font.PLAIN,1);
        Font3D labelAxisFont= new Font3D(font, new FontExtrusion());
        Text3D labelXText3D = new Text3D(labelAxisFont, new String("x"), new Point3f(MainFrame.width+4,(float)(MainFrame.height+2),(float)(MainFrame.depth+2)));
        Text3D labelYText3D = new Text3D(labelAxisFont, new String("y"), new Point3f(0.0f,MainFrame.height*-1+(-1*2),(float)(MainFrame.depth+2)));
        Text3D labelZText3D = new Text3D(labelAxisFont, new String("z"), new Point3f(0.0f,(float)(MainFrame.height+2),MainFrame.depth*-1+(-1*2)));

        masterTrans.addChild(new Shape3D(labelXText3D));
        masterTrans.addChild(new Shape3D(labelYText3D));
        masterTrans.addChild(new Shape3D(labelZText3D));

        objRoot.addChild(masterTrans);
        rotateBehaviour=new MouseRotate();
        rotateBehaviour.setTransformGroup(masterTrans);
        rotateBehaviour.setSchedulingBounds(new BoundingSphere());
        masterTrans.addChild(rotateBehaviour);

    }
    public void destroy() { // this function will allow Java3D to clean up upon quiting
        simpleU.removeAllLocales();
    }
    public void importingFunction(){
        pressed=0;
    }
    public void mouseClicked(MouseEvent e)
    {
        MainFrame.isPDFSaved=false;
        MainFrame.isCSVSaved=false;
        pickCanvas.setShapeLocation(e);
        PickResult result = pickCanvas.pickClosest();
        if (result == null) {
            System.out.println("Nothing picked");
            int xCord=e.getX();
            int yCord=e.getY();
            System.out.println("Nothing selected at xCord "+xCord+" yCord"+yCord);

        }
        else {
            DNAColorCube s = (DNAColorCube)result.getNode(PickResult.SHAPE3D);
            if (s != null) {
                MainFrame.StoreCoordinates[s.getX()][(s.getY()*-1)][(s.getZ()*-1)]=true;
                lastx=s.getX();
                lasty=s.getY();
                lastz=s.getZ();
                System.out.println(s.getClass().getName());
                System.out.println("X cordinate"+s.getX());
                System.out.println("Y cordinate"+s.getY());
                System.out.println("Z cordinate"+s.getZ());
                QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);
                s.setGeometry(cube);
            }
            else{
                System.out.println("null");
            }
        }
    }

}
