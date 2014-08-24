/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */
import java.awt.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.TextureLoader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class VisualizeActionListener implements ActionListener{
    public static TransformGroup masterTrans;
    public static SimpleUniverse simpleU;
    public static TransformGroup tg;
    public static Transform3D transform;
    public static BranchGroup objRoot;
    public static Container contentPane;
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Visualization Started");

        JFrame visualizeFrame = new JFrame("Visualize Frame");
        visualizeFrame.setLayout(new BorderLayout());
        visualizeFrame.setVisible(true);
        visualizeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        contentPane=visualizeFrame.getContentPane();

        Canvas3D visualizeCanvas= new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        visualizeFrame.add("Center",visualizeCanvas);

        BranchGroup scene = createSceneGraph();
        simpleU = new SimpleUniverse(visualizeCanvas);
        TransformGroup tg = simpleU.getViewingPlatform().getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(2f,2f,15f));
        tg.setTransform(t3d);
        scene.compile();
        simpleU.addBranchGraph(scene);

        System.out.println("Visualization Finished");
    }
    public BranchGroup createSceneGraph() {

        objRoot = new BranchGroup();
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        objRoot.setCapability(BranchGroup.ALLOW_PICKABLE_WRITE);
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(BranchGroup.ALLOW_DETACH);
        objRoot.setCapability(Node.ENABLE_PICK_REPORTING);

        for(int i=0;i<MainFrame.depth;i++){
            if(i%4==0){
                getNorthPlane(i);
            }
            if(i%4==1){
                getWestPlane(i);
            }
            if(i%4==2){
                getSouthPlane(i);
            }
            if(i%4==3){
                getEastPlane(i);
            }
        }
        return objRoot;
    }

    public static void getNorthPlane(int a){
        masterTrans=new TransformGroup();
        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        masterTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        masterTrans.setCapability(Node.ENABLE_PICK_REPORTING);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        masterTrans.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        for(int i=0;i<MainFrame.width;i++){
            for(int j=0;j<MainFrame.height;j++){
                DNAColorCube c1 = new DNAColorCube();
                c1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                c1.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
                c1.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
                c1.setCapability(Node.ENABLE_PICK_REPORTING);
                c1.setCordinate(i, j-MainFrame.height+1, a-MainFrame.depth+1);
                tg = new TransformGroup();
                tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                transform = new Transform3D();
                Vector3f vector = new Vector3f(0f+i*0.105f,0f+j*0.105f, 0f+a*0.105f);
                transform.setTranslation(vector);
                tg.addChild(c1);
                tg.setTransform(transform);
                masterTrans.addChild(tg);
                Color3f light1Color = new Color3f(.1f, 1.4f, .1f); // green light
                BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
                Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
                DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
                light1.setInfluencingBounds(bounds);
                masterTrans.addChild(light1);
            }
        }
    }

    public static void getWestPlane(int a){
        for(int i=0;i<MainFrame.width;i++){
            for(int j=0;j<MainFrame.height;j++){
                //getWestBrick(i,j,(a+1)*-1);
                System.out.println("West brick added at plane: "+a);
            }
        }
    }

    public static void getSouthPlane(int a){
        for(int i=0;i<MainFrame.width;i++){
            for(int j=0;j<MainFrame.height;j++){
                //getSouthBrick(i,j,a*-1);
                System.out.println("South brick added at plane: "+a);
            }
        }
    }

    public static void getEastPlane(int a){
        for(int i=0;i<MainFrame.width;i++){
            for(int j=0;j<MainFrame.height;j++){
                //getEastBrick(i,j,a*-1);
                System.out.println("East brick added at plane: "+a);
            }
        }
    }

    public void destroy(){
        simpleU.removeAllLocales();
    }
}
