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
        ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
        BranchGroup backgroundBranchGroup = new BranchGroup();
        Background background = new Background(new Color3f(1f,1f,1f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000);
        background.setApplicationBounds(sphere);
        backgroundBranchGroup.addChild(background);
        viewingPlatform.addChild(backgroundBranchGroup);

        TransformGroup View_TransformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated
        Transform3D View_Transform3D = new Transform3D();    // create a Transform3D for the ViewingPlatform
        View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform
        View_Transform3D.setTranslation(new Vector3f(0f, 0f,10f)); // set 3d to  x=0, y=0, z=5
        View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform

        scene.compile();
        simpleU.addBranchGraph(scene); //add your SceneGraph to the SimpleUniverse
    }

    public BranchGroup createSceneGraph() {

        BranchGroup objRoot = new BranchGroup();

        /**Code for creating 3DNA Domain Blocks
         *The building units of the Visualization class comprises of 3 modular Blocks viz. domain12Block, domain34Block,
         *  domain1234Block
         *  Each block has its own domainXX[XX]tg, domainXX[XX]t3d, domainxx[XX]CoverCylinderTransformGroup,
         *  domainxx[XX]CoverCylinderTransform3D, domainxx[XX]CoverCylinder, lightForDomainxx[XX].
         *  The final parameters which will be used for creating the visualization canvas will include:
         *  domainBlockxx[XX]tg and domainBlockxx[XX]t3d
         */

        //Code for Domain12
        TransformGroup domain12CoverCylinderTransformGroup = new TransformGroup();
        Transform3D domain12CoverCylinderTransform3D = new Transform3D();

        TransformGroup domain12tg = new TransformGroup();
        Transform3D domain12t3d = new Transform3D();

        Transform3D rotation12 = new Transform3D();
        rotation12.rotX(Math.PI/2);

        MouseRotate rotateBehaviour;

        domain12tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        domain12tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain12CoverCylinderTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain12CoverCylinderTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

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

            Cylinder domain12CoverCylinder = new Cylinder(0.4f, 2f);
            Vector3f domain12CoverCylinderVector = new Vector3f(-0.10f,0f,0.08f);
            domain12CoverCylinderTransform3D.setTranslation(domain12CoverCylinderVector);
            domain12CoverCylinderTransformGroup.setTransform(domain12CoverCylinderTransform3D);
            domain12CoverCylinderTransformGroup.addChild(domain12CoverCylinder);

            Appearance ap = new Appearance();
            TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
            ap.setTransparencyAttributes( transparencyAttributes );
            domain12CoverCylinder.setAppearance(ap);
        }

        catch (java.io.FileNotFoundException ex){
        }

        BoundingSphere bounds = new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 100.0);

        AmbientLight lightForDomain12 = new AmbientLight();

        TransformGroup domainBlock12tg = new TransformGroup();
        Transform3D domainBlock12t3d = new Transform3D();

        domainBlock12tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        domainBlock12tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        rotateBehaviour=new MouseRotate();
        rotateBehaviour.setTransformGroup(domainBlock12tg);
        rotateBehaviour.setSchedulingBounds(new BoundingSphere());

        domainBlock12tg.addChild(rotateBehaviour);
        domainBlock12tg.addChild(domain12tg);
        domainBlock12tg.addChild(domain12CoverCylinderTransformGroup);

        domainBlock12t3d.mul(rotation12);
        domainBlock12tg.setTransform(domainBlock12t3d);

        lightForDomain12.setEnable(true);
        lightForDomain12.setColor(new Color3f(255/255f, 0/255f, 0/255f));
        lightForDomain12.setInfluencingBounds(bounds);
        lightForDomain12.addScope(domain12tg);
        lightForDomain12.addScope(domain12CoverCylinderTransformGroup);

        objRoot.addChild (lightForDomain12);
        objRoot.addChild(domainBlock12tg);


        //Code for Domain34
        TransformGroup domain34CoverCylinderTransformGroup = new TransformGroup();
        Transform3D domain34CoverCylinderTransform3D = new Transform3D();

        TransformGroup domain34tg = new TransformGroup();
        Transform3D domain34t3d = new Transform3D();

        Transform3D rotation34 = new Transform3D();
        rotation34.rotX(Math.PI/2);

        domain34tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        domain34tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain34CoverCylinderTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain34CoverCylinderTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

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

            Cylinder domain34CoverCylinder = new Cylinder(0.4f, 2f);
            Vector3f domain34CoverCylinderVector = new Vector3f(-1.10f,0f,0.08f);
            domain34CoverCylinderTransform3D.setTranslation(domain34CoverCylinderVector);
            domain34CoverCylinderTransformGroup.setTransform(domain34CoverCylinderTransform3D);
            domain34CoverCylinderTransformGroup.addChild(domain34CoverCylinder);

            Appearance ap = new Appearance();
            TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
            ap.setTransparencyAttributes( transparencyAttributes );
            domain34CoverCylinder.setAppearance(ap);
        }

        catch (java.io.FileNotFoundException ex){
        }

        AmbientLight lightForDomain34 = new AmbientLight();

        TransformGroup domainBlock34tg = new TransformGroup();
        Transform3D domainBlock34t3d = new Transform3D();

        domainBlock34tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        domainBlock34tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        rotateBehaviour=new MouseRotate();
        rotateBehaviour.setTransformGroup(domainBlock34tg);
        rotateBehaviour.setSchedulingBounds(new BoundingSphere());

        domainBlock34tg.addChild(rotateBehaviour);
        domainBlock34tg.addChild(domain34tg);
        domainBlock34tg.addChild(domain34CoverCylinderTransformGroup);

        domainBlock34t3d.mul(rotation34);
        domainBlock34tg.setTransform(domainBlock34t3d);

        lightForDomain34.setEnable(true);
        lightForDomain34.setColor(new Color3f(0/255f, 255/255f, 0/255f));
        lightForDomain34.setInfluencingBounds(bounds);
        lightForDomain34.addScope(domain34tg);
        lightForDomain34.addScope(domain34CoverCylinderTransformGroup);

        objRoot.addChild (lightForDomain34);
        objRoot.addChild(domainBlock34tg);


        //Code for Domain1234(FullBrick)
        TransformGroup domain1234CoverCylinderTransformGroup = new TransformGroup();
        Transform3D domain1234CoverCylinderTransform3D = new Transform3D();

        TransformGroup domain1234tg = new TransformGroup();
        Transform3D domain1234t3d = new Transform3D();

        Transform3D rotation1234 = new Transform3D();
        rotation1234.rotX(Math.PI/2);

        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        domain1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234CoverCylinderTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        domain1234CoverCylinderTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

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

            Cylinder domain1234CoverCylinder = new Cylinder(0.7f, 2f);
            Vector3f domain1234CoverCylinderVector = new Vector3f(-2.50f,0f,0.08f);
            domain1234CoverCylinderTransform3D.setTranslation(domain1234CoverCylinderVector);
            domain1234CoverCylinderTransformGroup.setTransform(domain1234CoverCylinderTransform3D);
            domain1234CoverCylinderTransformGroup.addChild(domain1234CoverCylinder);

            Appearance ap = new Appearance();
            TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
            ap.setTransparencyAttributes( transparencyAttributes );
            domain1234CoverCylinder.setAppearance(ap);
        }

        catch (java.io.FileNotFoundException ex){
        }

        AmbientLight lightForDomain1234 = new AmbientLight();

        TransformGroup domainBlock1234tg = new TransformGroup();
        Transform3D domainBlock1234t3d = new Transform3D();

        domainBlock1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        domainBlock1234tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        rotateBehaviour=new MouseRotate();
        rotateBehaviour.setTransformGroup(domainBlock1234tg);
        rotateBehaviour.setSchedulingBounds(new BoundingSphere());

        domainBlock1234tg.addChild(rotateBehaviour);
        domainBlock1234tg.addChild(domain1234tg);
        domainBlock1234tg.addChild(domain1234CoverCylinderTransformGroup);

        //domainBlock1234t3d.mul(rotation1234);
        domainBlock1234tg.setTransform(domainBlock1234t3d);

        lightForDomain1234.setEnable(true);
        lightForDomain1234.setColor(new Color3f(0/255f, 0/255f, 255/255f));
        lightForDomain1234.setInfluencingBounds(bounds);
        lightForDomain1234.addScope(domain1234tg);
        lightForDomain1234.addScope(domain1234CoverCylinderTransformGroup);

        objRoot.addChild (lightForDomain1234);
        objRoot.addChild(domainBlock1234tg);

        return objRoot;
    }



    public static void main(String[] args) {
        application = true;
        Frame frame = new MainFrame(new WavefrontObjectLoader(), 1500, 1500);
    }
}
