/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.media.j3d.*;
import javax.swing.JFileChooser;
import javax.vecmath.Vector3f;

public class ImportActionListener extends MouseAdapter implements ActionListener {

    static int pressed=0;
    public static int lastx;
    public static int lasty;
    public static int lastz;
    private static final float[] colors = {
            // front face (yellow)
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            // back face (green)
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            // right face (blue)
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            // left face (red)
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            // top face (magenta)
            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            // bottom face (cyan)
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
    };
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

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        JFileChooser fc=new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc = new JFileChooser();
        int value = fc.showOpenDialog(null);
        if(value == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(f));
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            MainFrame.projectPath=f.getAbsolutePath();
            int x,y,z;
            String st;
            try {
                st=br.readLine();
                MainFrame.height=Integer.parseInt(st);
                st=br.readLine();
                MainFrame.width=Integer.parseInt(st);
                st=br.readLine();
                MainFrame.depth=Integer.parseInt(st);

                for(int i=0;i<MainFrame.width;i++)
                    for(int j=0;j<MainFrame.height;j++)
                        for (int k=0;k<MainFrame.depth;k++)
                            MainFrame.deletedCoordinates[i][j][k]=false;
                /*st=br.readLine();
                x=Integer.parseInt(st);
                st=br.readLine();
                y=Integer.parseInt(st);
                st=br.readLine();
                z=Integer.parseInt(st);*/


                while(((st = br.readLine()) != null)){
                    x=Integer.parseInt(st);
                    st=br.readLine();
                    //if(st!=null)
                        y=Integer.parseInt(st);
                    st=br.readLine();
                    //if(st!=null)
                        z=Integer.parseInt(st);
                    MainFrame.deletedCoordinates[x][y][z]=true;
                }
                MainFrame.isImported=true;
                System.out.println("Canvas height: "+MainFrame.height);
                System.out.println("Canvas width: "+MainFrame.width);
                System.out.println("Canvas depth: "+MainFrame.depth);
                createCanvas();
            }
            catch (IOException ep) {
                ep.printStackTrace();
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
        canvasx = 0.0f;
        canvasy = 0.0f;
        canvasz = 2.0f;

        ViewingPlatform vp = simpleU.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse

        TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

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

        float vposition=0.105f;
        float hposition=0.105f;
        float zposition=0.105f;

        for(int i=0;i<MainFrame.width;i++){
            for(int j=0;j<MainFrame.height;j++){
                for (int k=0;k<MainFrame.depth;k++){
                    if(MainFrame.deletedCoordinates[i][j][k]==true){
                        DNAColorCube c1 = new DNAColorCube(0.05f);
                        c1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                        c1.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
                        c1.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
                        c1.setCapability(Node.ENABLE_PICK_REPORTING);
                        c1.setCordinate(i, j-MainFrame.height+1, k-MainFrame.depth+1);
                        tg = new TransformGroup();
                        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                        transform = new Transform3D();
                        Vector3f vector = new Vector3f(0f+i*hposition,(0f+j*vposition)*-1, (0f+k*zposition)*-1);
                        transform.setTranslation(vector);
                        tg.addChild(c1);
                        //c1.printDetails();
                        tg.setTransform(transform);
                        masterTrans.addChild(tg);
                    }
                }
            }
        }
        objRoot.addChild(masterTrans);
        rotateBehaviour=new MouseRotate();
        rotateBehaviour.setTransformGroup(masterTrans);
        rotateBehaviour.setSchedulingBounds(new BoundingSphere());
        masterTrans.addChild(rotateBehaviour);

        //}
        //  if(pressed==1)
        //  importingFunction();
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
                MainFrame.deletedCoordinates[s.getX()][(s.getY()*-1)][(s.getZ()*-1)]=true;
                lastx=s.getX();
                lasty=s.getY();
                lastz=s.getZ();
                System.out.println(s.getClass().getName());
                System.out.println("X cordinate"+s.getX());
                System.out.println("Y cordinate"+s.getY());
                System.out.println("Z cordinate"+s.getZ());
                QuadArray cube = new QuadArray(24, QuadArray.COORDINATES|QuadArray.COLOR_3);
                cube.setColors(0, colors);
                s.setGeometry(cube);

            }
            else{
                System.out.println("null");
            }
        }
    }

}