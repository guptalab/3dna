/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
public class CanvasActionListener extends MouseAdapter implements ActionListener {
    static int pressed=0;
    public static Stack undoStack;
    public static int lastx;
    public static int lasty;
    public static int lastz;
    public static Transform3D canvasRotation;
    public static double angle=0 ;
    public static PickCanvas pickCanvas;
    public static BranchGroup objRoot;
    public static Transform3D transform;
    public static TransformGroup tg;
    public static SimpleUniverse simpleU;
    public static MouseRotate rotateBehaviour;
    public static MouseTranslate translateBehaviour;
    public static MouseZoom zoomBehaviour;
    public static TransformGroup masterTrans;
    public static BranchGroup scene;
    public static float canvasx;
    public static float canvasy;
    public static float canvasz;
    public static boolean isDimensionEntered=false;
    public static ArrayList<DNAProtectorCubeArray> protectorCubeArrayList;
    public static ArrayList<DNAColorCubeArray> colorCubeArrayList;
    @Override
    public void actionPerformed(ActionEvent arg0) {
    // TODO Auto-generated method stub
        if (!MainFrame.isProjectCreated) {
            Toolkit toolkit=Toolkit.getDefaultToolkit();
            int screenWidth = toolkit.getScreenSize().width;
            int screenHeight = toolkit.getScreenSize().height;
            final JFrame projectNameFrame = new JFrame("3DNA new project ");
            projectNameFrame.setBackground(Color.DARK_GRAY);
            ImageIcon img = new ImageIcon("icons/software_logo.png");
            Image imag=img.getImage();
            projectNameFrame.setIconImage(imag);
            projectNameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel projectNamePanel = new JPanel();
            projectNamePanel.setBackground(Color.DARK_GRAY);
            final JLabel projectNameLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Enter Project Name  </h4></html>");
            final JTextField projectNameTextField = new JTextField(15);
            JButton okButton = new JButton("OK");
            okButton.setBackground(Color.DARK_GRAY);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setBackground(Color.DARK_GRAY);
            JLabel infoLabel =new JLabel(new ImageIcon("images/INFO.png"));
            infoLabel.setToolTipText("A new project folder will be created in "+System.getProperty("user.home")+"/Desktop/3DNA_Workspace/");

            projectNamePanel.add(infoLabel);
            projectNamePanel.add(projectNameLabel);
            projectNamePanel.add(projectNameTextField);
            projectNamePanel.add(okButton);
            projectNameFrame.add(projectNamePanel, BorderLayout.CENTER);
            projectNameFrame.pack();
            projectNameFrame.setVisible(true);
            projectNameFrame.setLocation(screenWidth / 3, screenHeight / 3);
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame.projectName = projectNameTextField.getText();
                    if(MainFrame.projectName==null){
                    }
                    else{
                        MainFrame.projectPath=System.getProperty("user.home")+"/Desktop/3DNA_Workspace/"+MainFrame.projectName;
                        File projectDirectory=new File(MainFrame.projectPath);
                        projectDirectory.mkdirs();
                        projectNameFrame.dispose();
                        MainFrame.isProjectCreated=true;
                        MainFrame.printLog("New project created at "+ System.getProperty("user.home")+"/Desktop/3DNA_Workspace/"+MainFrame.projectName, Color.cyan);
                        enterDimensions();
                    }
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame.isProjectCreated=true;
                    projectNameFrame.dispose();
                }
            });
        }
        else{
            enterDimensions();
        }


   }

    public void enterDimensions(){

        if(MainFrame.isProjectCreated){
            final JFrame dimensionFrame = new JFrame("3DNA Dimensions "+MainFrame.projectName);
            dimensionFrame.setBackground(Color.DARK_GRAY);
            ImageIcon img = new ImageIcon("icons/software_logo.png");
            Image imag=img.getImage();
            dimensionFrame.setIconImage(imag);
            dimensionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(Color.DARK_GRAY);
            final JLabel canvasHeightLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Canvas Height </h4></html>");
            JLabel canvasWidthLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Canvas Width </h4></html>");
            JLabel canvasDepthLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Canvas Depth</h4></html>");
            final JTextField canvasHeightTextField = new JTextField(5);
            final JTextField canvasWidthTextField = new JTextField(5);
            final JTextField canvasDepthTextField = new JTextField(5);
            JButton okButton = new JButton("OK");
            okButton.setBackground(Color.DARK_GRAY);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setBackground(Color.DARK_GRAY);
            JLabel infoLabel =new JLabel(new ImageIcon("images/INFO.png"));
            infoLabel.setToolTipText("<html><style>h4{color:'black';}</style><p><em>Height</em> in DNA Helices<br><em> Width</em> in DNA Helices <br><em>Depth</em> in BP (a multiple of 8) </p></html>");

            mainPanel.add(infoLabel);
            mainPanel.add(canvasHeightLabel);
            mainPanel.add(canvasHeightTextField);
            mainPanel.add(canvasWidthLabel);
            mainPanel.add(canvasWidthTextField);
            mainPanel.add(canvasDepthLabel);
            mainPanel.add(canvasDepthTextField);
            mainPanel.add(okButton);
            dimensionFrame.add(mainPanel, BorderLayout.CENTER);
            dimensionFrame.pack();
            dimensionFrame.setVisible(true);
            dimensionFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(Integer.parseInt(canvasDepthTextField.getText())%8==0){
                        isDimensionEntered=true;
                        MainFrame.height=Integer.parseInt(canvasHeightTextField.getText());
                        MainFrame.width=Integer.parseInt(canvasWidthTextField.getText());
                        MainFrame.depth=Integer.parseInt(canvasDepthTextField.getText())/8;
                        dimensionFrame.dispose();
                        for(int i=0;i<MainFrame.width;i++)
                            for(int j=0;j<MainFrame.height;j++)
                                for (int k=0;k<=MainFrame.depth;k++)
                                    MainFrame.deletedCoordinates[i][j][k]=false;
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
    public void createCanvas(){

        MainFrame.TrueEnableContent();
        MainFrame.printLog("New canvas created. Height(y-axis):" + MainFrame.height + " Width(x-axis):" + MainFrame.width + " Depth(z-axis):" + MainFrame.depth , Color.cyan);
        // Canvas3D is where all the action will be taking place, don't worry, after adding it
        // to your layout, you don't have to touch it.
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        // add Canvas3D
        MainFrame.vPanel.setTopComponent(canvas);
        MainFrame.bottomRightJPanel.setVisible(true);
        MainFrame.vPanel.setDividerLocation(0.8);
        simpleU = new SimpleUniverse(canvas); // setup the SimpleUniverse, attach the Canvas3D
        //This is very important, the SceneGraph (where all the action takes place) is created
        //by calling a function which here is called 'createSceneGraph'.
        //The function is not necessary, you can put all your code here, but it is a
        //standard in Java3D to create your SceneGraph contents in the function 'createSceneGraph'
        scene = createSceneGraph();
        //set the ViewingPlatform by setting the canvasx, canvasy, canvasz values as 0.0,0.0,2.0
        canvasx = 0.50f;
        canvasy = -0.25f;
        canvasz = 5.0f;
        ViewingPlatform viewingPlatform = CanvasActionListener.simpleU.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse
        TransformGroup View_TransformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated
        Transform3D View_Transform3D = new Transform3D(); // create a Transform3D for the ViewingPlatform
        View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform
        View_Transform3D.setTranslation(new Vector3f(canvasx, canvasy, canvasz)); // set 3d to x=0, y=0, z=2
        View_TransformGroup.setTransform(View_Transform3D); // assign Transform3D to ViewPlatform
        simpleU.addBranchGraph(scene); //add your SceneGraph to the SimpleUniverse

        //We now add a pick canvas so that we can get
        pickCanvas = new PickCanvas(canvas, scene);
        pickCanvas.setMode(PickCanvas.GEOMETRY);
        canvas.addMouseListener(this);

    }
    public static BranchGroup createSceneGraph() {
        // This BranchGroup is the root of the SceneGraph,
        objRoot = new BranchGroup();
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        objRoot.setCapability(BranchGroup.ALLOW_PICKABLE_WRITE);
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(BranchGroup.ALLOW_DETACH);
        objRoot.setCapability(Node.ENABLE_PICK_REPORTING);
        getNewBox();//creates a plane of cubes, planes can be combined to form the full fledged canvas.

        return objRoot;
    }
    public static void getNewBox(){
        undoStack = new Stack();
        masterTrans=new TransformGroup();
        canvasRotation = new Transform3D();
        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        masterTrans.setCapability(Node.ENABLE_PICK_REPORTING);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        if(pressed==0){
            for(int i=0;i<MainFrame.width;i++)
                for(int j=0;j<MainFrame.height;j++)
                    for (int k=0;k<=MainFrame.depth;k++)
                        MainFrame.deletedCoordinates[i][j][k]=false;
        }
        float positionOffset=0.105f;
        //initialize the DNAColorCubeArray to store all the DNAColorCube(s) that will be formed in that process
        colorCubeArrayList = new ArrayList<DNAColorCubeArray>();
        for(int i=0;i<MainFrame.width;i++){
            for(int j=0;j>(MainFrame.height)*-1;j--){
                for (int k=-1;k>=(MainFrame.depth)*-1;k--){
                    DNAColorCube c1 = new DNAColorCube(0.05f);
                    c1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                    c1.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
                    c1.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
                    c1.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
                    c1.setCapability(Node.ENABLE_PICK_REPORTING);
                    c1.setCordinate(i, j*-1, k*-1);
                    tg = new TransformGroup();
                    tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
                    tg.setCapability(Node.ALLOW_PICKABLE_READ);
                    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    transform = new Transform3D();
                    Vector3f vector = new Vector3f(i*positionOffset,j*positionOffset,k*positionOffset);
                    transform.setTranslation(vector);
                    tg.addChild(c1);
                    tg.setTransform(transform);
                    masterTrans.addChild(tg);
                    // add the DNAColorCube to the DNAColorCubeArray so that it can be accessed later
                    colorCubeArrayList.add(new DNAColorCubeArray(c1,i, j*-1, k*-1));
                }
            }
        }

        //Adding molecular scale and axis to the master TransformGroup
        //Delaring variables for the endpoints fo the Molecular Axis
        float xOrigin = -0.05f;
        float yOrigin = 0.05f;
        float zOrigin = -0.05f;

        float xMaximum = (2.5f*MainFrame.width)*0.05f;
        float yMaximum = -1*(2.5f*MainFrame.height)*0.05f;
        float zMaximum = -1*(2.5f*MainFrame.depth)*0.05f;

        //Initializing LineArrays for the Molecular Axis
        LineArray xAxisLineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray yAxisLineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray zAxisLineArray = new LineArray(2, LineArray.COORDINATES);
        //Initializing the LineArray(s)
        xAxisLineArray.setCoordinate(0, new Point3f(xOrigin,yOrigin,zOrigin));
        yAxisLineArray.setCoordinate(0, new Point3f(xOrigin,yOrigin,zOrigin));
        zAxisLineArray.setCoordinate(0, new Point3f(xOrigin,yOrigin,zOrigin));

        xAxisLineArray.setCoordinate(1, new Point3f(xMaximum,yOrigin,zOrigin));
        yAxisLineArray.setCoordinate(1, new Point3f(xOrigin,yMaximum,zOrigin));
        zAxisLineArray.setCoordinate(1, new Point3f(xOrigin,yOrigin,zMaximum));
        //Adding the LineArray(s) to the masterTrans Group
        masterTrans.addChild(new Shape3D(xAxisLineArray));
        masterTrans.addChild(new Shape3D(yAxisLineArray));
        masterTrans.addChild(new Shape3D(zAxisLineArray));

        objRoot.addChild(masterTrans);

        rotateBehaviour=new MouseRotate();
        rotateBehaviour.setTransformGroup(masterTrans);
        rotateBehaviour.setSchedulingBounds(new BoundingSphere());


        translateBehaviour=new MouseTranslate();
        translateBehaviour.setTransformGroup(masterTrans);
        translateBehaviour.setSchedulingBounds(new BoundingSphere());

        zoomBehaviour=new MouseZoom();
        zoomBehaviour.setTransformGroup(masterTrans);
        zoomBehaviour.setSchedulingBounds(new BoundingSphere());

        masterTrans.addChild(rotateBehaviour);
        masterTrans.addChild(zoomBehaviour);
        masterTrans.addChild(translateBehaviour);

    }
    public static void rotateCanvasAntiClockWise(){
        angle=angle+0.5;
        canvasRotation.rotY(angle);
        masterTrans.setTransform(canvasRotation);
    }
    public static void rotateCanvasClockWise(){
        angle=angle-0.5;
        canvasRotation.rotY(angle);
        masterTrans.setTransform(canvasRotation);
    }
    public void destroy() { // this function will allow Java3D to clean up upon quiting
        simpleU.removeAllLocales();
    }
    public void importingFunction(){
        pressed=0;
    }
    public void mouseClicked(MouseEvent e){
        MainFrame.isPDFSaved=false;
        MainFrame.isCSVSaved=false;
        MainFrame.isPDFBoundarySaved=false;
        MainFrame.isCSVBoundarySaved=false;
        pickCanvas.setShapeLocation(e);
        PickResult mouseClickResult = pickCanvas.pickClosest();
        if (mouseClickResult == null) {
            int xCord=e.getX();
            int yCord=e.getY();
            System.out.println("Nothing selected at xCord "+xCord+" yCord"+yCord);
        }
        else {
            DNAColorCube pickedCube = (DNAColorCube)mouseClickResult.getNode(PickResult.SHAPE3D);
            if (pickedCube != null && MainFrame.deletedCoordinates[pickedCube.getX()][pickedCube.getY()][pickedCube.getZ()] == false) {
                int x, y,z1, z2;
                x = pickedCube.getX();
                y = pickedCube.getY();
                z1 = pickedCube.getZ();
                z2 = z1+1;

                MainFrame.deletedCoordinates[x][y][z1]=true;

                undoStack.push(new Point3f(pickedCube.getX(),pickedCube.getY(),pickedCube.getZ()));

                MainFrame.printLog("Voxel deleted at coordinates: [" + x + ", " + y + ", " + z1 +"] [" + x + ", " + y + ", " + z2 +"]" , Color.cyan);

                QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);

                pickedCube.setGeometry(cube);
            }
            else{
                System.out.println("null");
            }
        }
    }
    public static void deletePlane(String selectedPlane, int selectedPlaneNumber){
        char planeForDeletion=0;
        if(selectedPlane == "X Plane")
            planeForDeletion= 'X';
        if(selectedPlane == "Y Plane")
            planeForDeletion= 'Y';
        if(selectedPlane == "Z Plane")
            planeForDeletion= 'Z';
        deleteSelectedCanvasPlane(planeForDeletion,selectedPlaneNumber);
    }
    public static void deleteRow(String selectedAxis, int constantX, int constantY, int constantZ, int minX, int minY, int minZ, int maxX, int maxY, int maxZ){
        char axisForDeletion=0;
        if(selectedAxis == "Along X-axis")
            axisForDeletion= 'X';
        if(selectedAxis == "Along Y-axis")
            axisForDeletion= 'Y';
        if(selectedAxis == "Along Z-axis")
            axisForDeletion= 'Z';
        deleteSelectedCanvasRow(axisForDeletion,constantX, constantY, constantZ, minX, minY, minZ, maxX, maxY, maxZ);
    }
    public static void deleteSelectedCanvasRow(char selectedAxisForRowDeletion, int constantX, int constantY, int constantZ, int minX, int minY, int minZ, int maxX, int maxY, int maxZ){
        Appearance ap = new Appearance();
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,1f);
        ap.setTransparencyAttributes( transparencyAttributes );
        MainFrame.isCSVSaved = false;
        MainFrame.isPDFSaved = false;
        MainFrame.isCSVBoundarySaved = false;
        MainFrame.isPDFBoundarySaved = false;
        switch (selectedAxisForRowDeletion){
            case 'X':
                for (int i = 0; i < colorCubeArrayList.size(); i++) {
                    if (((colorCubeArrayList.get(i).canvasDNAColorCube.xCord >= minX && colorCubeArrayList.get(i).canvasDNAColorCube.xCord <= maxX)
                            && (colorCubeArrayList.get(i).canvasDNAColorCube.zCord == constantZ)
                            && (colorCubeArrayList.get(i).canvasDNAColorCube.yCord == constantY))
                            && MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                            [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] == false) {
                        MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] = true;
                        undoStack.push(new Point3f(colorCubeArrayList.get(i).canvasDNAColorCube.getX(),colorCubeArrayList.get(i).canvasDNAColorCube.getY(),colorCubeArrayList.get(i).canvasDNAColorCube.getZ()));
                        DNAColorCube pickedCube = colorCubeArrayList.get(i).canvasDNAColorCube;
                        pickedCube.setAppearance(ap);
                        QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);
                        colorCubeArrayList.get(i).canvasDNAColorCube.setGeometry(cube);

                        MainFrame.printLog("Voxel deleted at coordinates: [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() +
                                ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getZ() +
                                "] [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() +
                                ", " + (colorCubeArrayList.get(i).canvasDNAColorCube.getZ() + 1) + "]", Color.cyan);
                    }
                }
                MainFrame.printLog("Success: voxels removed along the X-Axis", Color.green );
                break;
            case 'Y':
                for (int i = 0; i < colorCubeArrayList.size(); i++) {
                    if (((colorCubeArrayList.get(i).canvasDNAColorCube.yCord >= minY && colorCubeArrayList.get(i).canvasDNAColorCube.yCord <= maxY)
                            && (colorCubeArrayList.get(i).canvasDNAColorCube.zCord == constantZ)
                            && (colorCubeArrayList.get(i).canvasDNAColorCube.xCord == constantX))
                            && MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                            [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] == false) {
                        MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] = true;
                        undoStack.push(new Point3f(colorCubeArrayList.get(i).canvasDNAColorCube.getX(),colorCubeArrayList.get(i).canvasDNAColorCube.getY(),colorCubeArrayList.get(i).canvasDNAColorCube.getZ()));
                        DNAColorCube pickedCube = colorCubeArrayList.get(i).canvasDNAColorCube;
                        pickedCube.setAppearance(ap);
                        QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);
                        colorCubeArrayList.get(i).canvasDNAColorCube.setGeometry(cube);
                        MainFrame.printLog("Voxel deleted at coordinates: [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() +
                                ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getZ() +
                                "] [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() +
                                ", " + (colorCubeArrayList.get(i).canvasDNAColorCube.getZ() + 1) + "]", Color.cyan);
                    }
                }
                MainFrame.printLog("Success: voxels removed along the Y-Axis", Color.green );
                break;
            case 'Z':
                for (int i = 0; i < colorCubeArrayList.size(); i++) {
                    if (((colorCubeArrayList.get(i).canvasDNAColorCube.zCord >= minZ && colorCubeArrayList.get(i).canvasDNAColorCube.zCord <= maxZ)
                            && (colorCubeArrayList.get(i).canvasDNAColorCube.xCord == constantX)
                            && (colorCubeArrayList.get(i).canvasDNAColorCube.yCord == constantY))
                            && MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                            [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] == false) {
                        MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] = true;
                        undoStack.push(new Point3f(colorCubeArrayList.get(i).canvasDNAColorCube.getX(),colorCubeArrayList.get(i).canvasDNAColorCube.getY(),colorCubeArrayList.get(i).canvasDNAColorCube.getZ()));
                        DNAColorCube pickedCube = colorCubeArrayList.get(i).canvasDNAColorCube;
                        pickedCube.setAppearance(ap);
                        QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);
                        colorCubeArrayList.get(i).canvasDNAColorCube.setGeometry(cube);
                        MainFrame.printLog("Voxel deleted at coordinates: [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() +
                                ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getZ() +
                                "] [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() +
                                ", " + (colorCubeArrayList.get(i).canvasDNAColorCube.getZ() + 1) + "]", Color.cyan);
                    }
                }
                MainFrame.printLog("Success: voxels removed along the Z-Axis", Color.green );
                break;
            default:
                throw new IllegalArgumentException("Invalid String has been passed");


        }
    }
    public static void deleteSelectedCanvasPlane(char plane, int planeNumber){
        Appearance ap = new Appearance();
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,1f);
        ap.setTransparencyAttributes( transparencyAttributes );
        switch (plane){
            case 'X':
                if(planeNumber < MainFrame.width) {
                    for (int i = 0; i < colorCubeArrayList.size(); i++) {
                        if (colorCubeArrayList.get(i).canvasDNAColorCube.xCord == planeNumber && MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] == false) {
                            MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                    [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()]= true;
                            undoStack.push(new Point3f(colorCubeArrayList.get(i).canvasDNAColorCube.getX(),colorCubeArrayList.get(i).canvasDNAColorCube.getY(),colorCubeArrayList.get(i).canvasDNAColorCube.getZ()));
                            DNAColorCube pickedCube = colorCubeArrayList.get(i).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                            QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);
                            colorCubeArrayList.get(i).canvasDNAColorCube.setGeometry(cube);
                            MainFrame.printLog("Voxel deleted at coordinates: [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() +
                                    ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getZ() +
                                    "] [" +colorCubeArrayList.get(i).canvasDNAColorCube.getX()+ ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() +
                                    ", " + (colorCubeArrayList.get(i).canvasDNAColorCube.getZ()+1) + "]", Color.cyan);
                        }
                    }
                    MainFrame.printLog("Success: X-Plane " + planeNumber + " deleted", Color.green);
                    MainFrame.isCSVSaved = false;
                    MainFrame.isPDFSaved = false;
                    MainFrame.isCSVBoundarySaved = false;
                    MainFrame.isPDFBoundarySaved = false;
                }
                break;
            case 'Y':
                if (planeNumber < MainFrame.height) {
                    for (int i = 0; i < colorCubeArrayList.size(); i++) {
                        if (colorCubeArrayList.get(i).canvasDNAColorCube.yCord == planeNumber && MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] == false) {
                            MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                    [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()]= true;
                            undoStack.push(new Point3f(colorCubeArrayList.get(i).canvasDNAColorCube.getX(),colorCubeArrayList.get(i).canvasDNAColorCube.getY(),colorCubeArrayList.get(i).canvasDNAColorCube.getZ()));
                            DNAColorCube pickedCube = colorCubeArrayList.get(i).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                            QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);
                            colorCubeArrayList.get(i).canvasDNAColorCube.setGeometry(cube);
                            MainFrame.printLog("Voxel deleted at coordinates: [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() +
                                    ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getZ() +
                                    "] [" +colorCubeArrayList.get(i).canvasDNAColorCube.getX()+ ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() +
                                    ", " + (colorCubeArrayList.get(i).canvasDNAColorCube.getZ()+1)+ "]", Color.cyan);
                        }
                    }
                    MainFrame.printLog("Success: Y-Plane " + planeNumber + " deleted", Color.green);
                    MainFrame.isCSVSaved = false;
                    MainFrame.isPDFSaved = false;
                    MainFrame.isCSVBoundarySaved = false;
                    MainFrame.isPDFBoundarySaved = false;
                }
                break;
            case 'Z':
                if (planeNumber <= MainFrame.depth) {
                    for (int i = 0; i < colorCubeArrayList.size(); i++) {
                        if (colorCubeArrayList.get(i).canvasDNAColorCube.zCord == planeNumber && MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()] == false) {
                            MainFrame.deletedCoordinates[colorCubeArrayList.get(i).canvasDNAColorCube.getX()]
                                    [colorCubeArrayList.get(i).canvasDNAColorCube.getY()][colorCubeArrayList.get(i).canvasDNAColorCube.getZ()]= true;
                            undoStack.push(new Point3f(colorCubeArrayList.get(i).canvasDNAColorCube.getX(),colorCubeArrayList.get(i).canvasDNAColorCube.getY(),colorCubeArrayList.get(i).canvasDNAColorCube.getZ()));
                            DNAColorCube pickedCube = colorCubeArrayList.get(i).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                            QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);
                            colorCubeArrayList.get(i).canvasDNAColorCube.setGeometry(cube);
                            MainFrame.printLog("Voxel deleted at coordinates: [" + colorCubeArrayList.get(i).canvasDNAColorCube.getX() +
                                    ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() + ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getZ() +
                                    "] [" +colorCubeArrayList.get(i).canvasDNAColorCube.getX()+ ", " + colorCubeArrayList.get(i).canvasDNAColorCube.getY() +
                                    ", " + (colorCubeArrayList.get(i).canvasDNAColorCube.getZ()+1) + "]", Color.cyan);
                        }
                    }
                    MainFrame.printLog("Success: Z-Plane " + planeNumber + " deleted", Color.green);
                    MainFrame.isCSVSaved = false;
                    MainFrame.isPDFSaved = false;
                    MainFrame.isCSVBoundarySaved = false;
                    MainFrame.isPDFBoundarySaved = false;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid String has been passed");
        }
    }

}