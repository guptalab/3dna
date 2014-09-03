import java.awt.Frame;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.*;

public class WavefrontObjectLoader extends Applet {
    SimpleUniverse simpleU;
    ViewingPlatform viewingPlatform;
    static boolean application = false;

    public WavefrontObjectLoader (){
    }

    public void init() {

        setLayout(new BorderLayout());
        Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        add("Center", c);
        simpleU= new SimpleUniverse(c); // setup the SimpleUniverse, attach the Canvas3D
        BranchGroup scene = createSceneGraph();
        //simpleU.getViewingPlatform().setNominalViewingTransform();
        ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
        BranchGroup backgroundBranchGroup = new BranchGroup();
        Background background = new Background(new Color3f(75/255f,75/255f,75/255f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000);
        background.setApplicationBounds(sphere);
        backgroundBranchGroup.addChild(background);
        viewingPlatform.addChild(backgroundBranchGroup);
        TransformGroup View_TransformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

        Transform3D View_Transform3D = new Transform3D();    // create a Transform3D for the ViewingPlatform
        View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform

        View_Transform3D.setTranslation(new Vector3f(0f, 0f, 2f)); // set 3d to  x=0, y=0, z=2
        View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform

        scene.compile();
        simpleU.addBranchGraph(scene); //add your SceneGraph to the SimpleUniverse

    }

    public BranchGroup createSceneGraph() {

        BranchGroup objRoot = new BranchGroup();
        TransformGroup tg1 = new TransformGroup();
        TransformGroup tg2 = new TransformGroup();
        Transform3D t3d = new Transform3D();
        MouseRotate rotateBehaviour=new MouseRotate();
        tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        try
        {
            Scene scene1 = null;
            Scene scene2 = null;
            ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

            if (application == false){
                java.net.URL airFile = new java.net.URL (getCodeBase(), "3DNADomian.obj");
                scene1 = f.load (airFile);
                scene2 = f.load (airFile);

                tg1.addChild (scene1.getSceneGroup ());
                rotateBehaviour=new MouseRotate();
                rotateBehaviour.setTransformGroup(tg1);
                rotateBehaviour.setSchedulingBounds(new BoundingSphere());
                tg1.addChild(rotateBehaviour);

                tg2.addChild (scene2.getSceneGroup ());
                rotateBehaviour=new MouseRotate();
                rotateBehaviour.setTransformGroup(tg2);
                rotateBehaviour.setSchedulingBounds(new BoundingSphere());
                tg2.addChild(rotateBehaviour);

            }
            else {
                String s1 = "3DNADomian.obj";
                scene1 = f.load (s1);
                scene2 = f.load (s1);




                tg1.addChild (scene1.getSceneGroup ());
                rotateBehaviour=new MouseRotate();
                rotateBehaviour.setTransformGroup(tg1);
                rotateBehaviour.setSchedulingBounds(new BoundingSphere());
                tg1.addChild(rotateBehaviour);

                tg2.addChild (scene2.getSceneGroup ());
                rotateBehaviour=new MouseRotate();
                rotateBehaviour.setTransformGroup(tg2);
                rotateBehaviour.setSchedulingBounds(new BoundingSphere());
                tg2.addChild(rotateBehaviour);
            }

        }

        catch (java.net.MalformedURLException ex){
        }
        catch (java.io.FileNotFoundException ex){
        }


        BoundingSphere bounds = new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 1000.0);

        Color3f ambientColor = new Color3f (255/255f, 0/255f, 0/255f);

        AmbientLight ambientLightNode = new AmbientLight (ambientColor);

        ambientLightNode.setInfluencingBounds (bounds);

        objRoot.addChild (ambientLightNode);


        t3d.setTranslation(new Vector3f(1f,1f,-5f));
        tg1.setTransform(t3d);
        objRoot.addChild(tg1);

        t3d.setTranslation(new Vector3f(0f,0f,-5f));
        tg2.setTransform(t3d);
        objRoot.addChild(tg2);

        return objRoot;
    }



    public static void main(String[] args) {
        application = true;
        Frame frame = new MainFrame(new WavefrontObjectLoader(), 1500, 1500);

    }
}