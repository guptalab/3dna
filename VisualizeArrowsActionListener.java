/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */
import java.awt.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.util.ArrayList;

public class VisualizeArrowsActionListener {

    public VisualizeArrowsActionListener(){
        visualizeFrame = new JFrame("3DNA Elementary Visualization");
        visualizeFrame.setVisible(true);
        visualizeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        visualizeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Initialize the visualizeFrameBasePanel
        visualizeFrameBasePanel = new JPanel();
        visualizeFrameBasePanel.setLayout(new BorderLayout());
        visualizeFrame.getContentPane().add(visualizeFrameBasePanel);
        //Initialize Canvas3D
        initializeCanvas3D();
        visualizeFrameBasePanel.add(visualizeCanvas,BorderLayout.CENTER);
    }


    public void initializeCanvas3D(){
        visualizeCanvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        visualizationSimpleUniverse = new SimpleUniverse(visualizeCanvas);
        //initialize viewing platform
        viewingPlatform = visualizationSimpleUniverse.getViewingPlatform();

        //define background colors for the Canvas
        BranchGroup backgroundBranchGroup = new BranchGroup();
        Background background = new Background(new Color3f(67/255f,99/255f,120/255f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000);
        background.setApplicationBounds(sphere);
        backgroundBranchGroup.addChild(background);
        viewingPlatform.addChild(backgroundBranchGroup);

        //setting the Viewing Platform with the initial values
        canvasX = 3.0f;
        canvasY = 5.0f;
        canvasZ = 20.0f;
        TransformGroup View_TransformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated
        Transform3D View_Transform3D = new Transform3D();    // create a Transform3D for the ViewingPlatform
        View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform
        View_Transform3D.setTranslation(new Vector3f(canvasX,canvasY,canvasZ)); // set 3d to  x=0, y=0, z=20
        View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform

        //initialize visualizeScene
        visualizeScene = createSceneGraph();
        visualizeScene.setCapability(BranchGroup.ALLOW_DETACH);
        visualizeScene.compile();
        visualizationSimpleUniverse.addBranchGraph(visualizeScene);
        visualizePickCanvas= new PickCanvas(visualizeCanvas, visualizeScene);
        visualizePickCanvas.setMode(PickCanvas.GEOMETRY);
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

        translateBehaviour=new MouseTranslate();
        translateBehaviour.setTransformGroup(masterTrans);
        translateBehaviour.setSchedulingBounds(new BoundingSphere());

        zoomBehaviour=new MouseZoom();
        zoomBehaviour.setTransformGroup(masterTrans);
        zoomBehaviour.setSchedulingBounds(new BoundingSphere());

        masterTrans.addChild(rotateBehaviour);
        masterTrans.addChild(zoomBehaviour);
        masterTrans.addChild(translateBehaviour);

        int x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4;
        int canvasDomainXCoordinate, canvasDomainYCoordinate, canvasDomainZCoordinate;
        ArrayList<VoxelToBrick> dnaSequenceDate;
        if(MainFrame.isImportSequencesListEnabled || MainFrame.isYonggangSequencesListEnabled)
            dnaSequenceDate= ImportSequencesCoordinatesMap.brickList;
        else
            dnaSequenceDate= CoordinatesSequenceMap.brickList;

        for (int i = 0; i < dnaSequenceDate.size(); i++) {
            //identify and print for domains 1 and 2(HalfBrick)
            if (dnaSequenceDate.get(i).x3 == -1) {
                x1 = dnaSequenceDate.get(i).x1;
                y1 = dnaSequenceDate.get(i).y1;
                z1 = dnaSequenceDate.get(i).z1;

                x2 = dnaSequenceDate.get(i).x2;
                y2 = dnaSequenceDate.get(i).y2;
                z2 = dnaSequenceDate.get(i).z2;

                System.out.println("Printing Domain12HalfBrick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+dnaSequenceDate.get(i).getCompleteSequence());

                canvasDomainXCoordinate = x2;
                canvasDomainYCoordinate = y2;
                canvasDomainZCoordinate = z2;

                //Code for Domain12
                getDomain12Block((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate, dnaSequenceDate.get(i).getCompleteSequence());
            }else if (dnaSequenceDate.get(i).x1 == -1) {//identify and print for domains 3 and 4(HalfBrick)
                x3 = dnaSequenceDate.get(i).x3;
                y3 = dnaSequenceDate.get(i).y3;
                z3 = dnaSequenceDate.get(i).z3;

                x4 = dnaSequenceDate.get(i).x4;
                y4 = dnaSequenceDate.get(i).y4;
                z4 = dnaSequenceDate.get(i).z4;

                System.out.println("Printing Domain34HalfBrick"+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4+dnaSequenceDate.get(i).getCompleteSequence());

                canvasDomainXCoordinate = x3;
                canvasDomainYCoordinate = y3;
                canvasDomainZCoordinate = z3;

                //Code for Domain34
                getDomain34Block((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate ,dnaSequenceDate.get(i).getCompleteSequence());
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

                if(z2%4 == 0){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;
                    System.out.println("Printing North Brick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4+dnaSequenceDate.get(i).getCompleteSequence());
                  getDomain1234NorthBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate,dnaSequenceDate.get(i).getCompleteSequence());
                }else if(z2%4 == 1){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;
                    System.out.println("Printing West Brick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4+dnaSequenceDate.get(i).getCompleteSequence());
                    getDomain1234WestBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate, dnaSequenceDate.get(i).getCompleteSequence());
                }else if(z2%4 == 2){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;
                    System.out.println("Printing South Brick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4+dnaSequenceDate.get(i).getCompleteSequence());
                    getDomain1234SouthBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate,dnaSequenceDate.get(i).getCompleteSequence());
                }else if(z2%4 == 3){
                    canvasDomainXCoordinate = x2;
                    canvasDomainYCoordinate = y2;
                    canvasDomainZCoordinate = z2;
                    System.out.println("Printing East Brick"+x1+":"+y1+":"+z1+","+x2+":"+y2+":"+z2+","+x3+":"+y3+":"+z3+","+x4+":"+y4+":"+z4+dnaSequenceDate.get(i).getCompleteSequence());
                    getDomain1234EastBlock((float) canvasDomainXCoordinate, (float) canvasDomainYCoordinate, (float) canvasDomainZCoordinate,dnaSequenceDate.get(i).getCompleteSequence());
                }
            }
        }

        objRoot.addChild(masterTrans);
        return objRoot;
    }


    public static void getDomain12Block(float x, float y, float z, String sequence){

        DNATransformGroup domain12tg = new DNATransformGroup();
        Transform3D domain12t3d = new Transform3D();

        TransformGroup arrow12TransformGroup = new ArrowHalfBrick12();
        arrow12TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        arrow12TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain12tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain12tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain12tg.addChild(arrow12TransformGroup);
        domain12tg.setSequence(sequence);
        domain12tg.setCordinate(x,y,z);
        domain12tg.isHalfBrick=true;
        domain12tg.isDomain12=true;

        domain12tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domain12tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domain12tg.setCapability(Node.ENABLE_PICK_REPORTING);


        Vector3f domain12Position = new Vector3f(x*0.5f,MainFrame.height-y+(y+1)*0.5f,MainFrame.depth-z+((z-1)*0.75f));
        domain12t3d.setTranslation(domain12Position);
        domain12tg.setTransform(domain12t3d);

        masterTrans.addChild(domain12tg);

    }
    public static void getDomain34Block(float x, float y, float z, String sequence){
        DNATransformGroup domain34tg = new DNATransformGroup();
        Transform3D domain34t3d = new Transform3D();

        TransformGroup arrow34TransformGroup = new ArrowHalfBrick34();
        arrow34TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        arrow34TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain34tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain34tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain34tg.addChild(arrow34TransformGroup);
        domain34tg.setSequence(sequence);
        domain34tg.setCordinate(x,y,z);
        domain34tg.isHalfBrick=true;
        domain34tg.isDomain12=true;

        domain34tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domain34tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domain34tg.setCapability(Node.ENABLE_PICK_REPORTING);


        Vector3f domain34Position = new Vector3f(x*0.5f,MainFrame.height-y+(y+1)*0.5f,MainFrame.depth-z+((z-1)*0.75f));
        domain34t3d.setTranslation(domain34Position);
        domain34tg.setTransform(domain34t3d);

        masterTrans.addChild(domain34tg);
    }

    public static void getDomain1234NorthBlock(float x, float y, float z, String sequence){
        DNATransformGroup domain1234tg = new DNATransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234Z = new Transform3D();
        rotation1234Z.rotZ(-Math.PI/2);

        TransformGroup arrow1234TransformGroup = new ArrowFullBrick();
        arrow1234TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        arrow1234TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain1234t3d.mul(rotation1234Z);
        domain1234tg.addChild(arrow1234TransformGroup);
        domain1234tg.setSequence(sequence);
        domain1234tg.setCordinate(x,y,z);
        domain1234tg.isHalfBrick=true;
        domain1234tg.isDomain12=true;

        domain1234tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domain1234tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domain1234tg.setCapability(Node.ENABLE_PICK_REPORTING);


        Vector3f domain34Position = new Vector3f(x-(x*0.5f),MainFrame.height-y+(y+2)*0.5f,MainFrame.depth-z+((z-1)*(0.5f+0.25f)));
        domain1234t3d.setTranslation(domain34Position);
        domain1234tg.setTransform(domain1234t3d);

        masterTrans.addChild(domain1234tg);
    }
    public static void getDomain1234WestBlock(float x, float y, float z, String sequence){
        DNATransformGroup domain1234tg = new DNATransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        TransformGroup arrow1234TransformGroup = new ArrowFullBrick();
        arrow1234TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        arrow1234TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain1234tg.addChild(arrow1234TransformGroup);
        domain1234tg.setSequence(sequence);
        domain1234tg.setCordinate(x,y,z);
        domain1234tg.isHalfBrick=true;
        domain1234tg.isDomain12=true;

        domain1234tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domain1234tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domain1234tg.setCapability(Node.ENABLE_PICK_REPORTING);


        Vector3f domain34Position = new Vector3f(x-(x+1)*0.5f,MainFrame.height-y+(y+1)*0.5f,MainFrame.depth-z+((z-1)*0.75f));
        domain1234t3d.setTranslation(domain34Position);
        domain1234tg.setTransform(domain1234t3d);

        masterTrans.addChild(domain1234tg);
    }

    public static void getDomain1234SouthBlock(float x, float y, float z, String sequence){
        DNATransformGroup domain1234tg = new DNATransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234Z = new Transform3D();
        rotation1234Z.rotZ(Math.PI/2);

        TransformGroup arrow1234TransformGroup = new ArrowFullBrick();
        arrow1234TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        arrow1234TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain1234t3d.mul(rotation1234Z);
        domain1234tg.addChild(arrow1234TransformGroup);
        domain1234tg.setSequence(sequence);
        domain1234tg.setCordinate(x,y,z);
        domain1234tg.isHalfBrick=true;
        domain1234tg.isDomain12=true;

        domain1234tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domain1234tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domain1234tg.setCapability(Node.ENABLE_PICK_REPORTING);


        Vector3f domain34Position = new Vector3f(x-(x*0.5f),MainFrame.height-y+(y)*0.5f,MainFrame.depth-z+((z-1)*0.75f));
        domain1234t3d.setTranslation(domain34Position);
        domain1234tg.setTransform(domain1234t3d);

        masterTrans.addChild(domain1234tg);
    }
    public static void getDomain1234EastBlock(float x, float y, float z, String sequence){
        DNATransformGroup domain1234tg = new DNATransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234Z = new Transform3D();
        rotation1234Z.rotZ(Math.PI);

        TransformGroup arrow1234TransformGroup = new ArrowFullBrick();
        arrow1234TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        arrow1234TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        domain1234t3d.mul(rotation1234Z);
        domain1234tg.addChild(arrow1234TransformGroup);
        domain1234tg.setSequence(sequence);
        domain1234tg.setCordinate(x,y,z);
        domain1234tg.isHalfBrick=true;
        domain1234tg.isDomain12=true;

        domain1234tg.setCapability(Node.ALLOW_PICKABLE_READ);
        domain1234tg.setCapability(Node.ALLOW_PICKABLE_WRITE);
        domain1234tg.setCapability(Node.ENABLE_PICK_REPORTING);


        Vector3f domain34Position = new Vector3f(x-(x-1)*0.5f,MainFrame.height-y+(y+1)*0.5f,MainFrame.depth-z+((z-1)*0.75f));
        domain1234t3d.setTranslation(domain34Position);
        domain1234tg.setTransform(domain1234t3d);

        masterTrans.addChild(domain1234tg);
    }

    public static TransformGroup masterTrans;
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
    public static MouseRotate rotateBehaviour;
    public static MouseTranslate translateBehaviour;
    public static MouseZoom zoomBehaviour;
    public static PickCanvas visualizePickCanvas;
}