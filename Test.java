import java.awt.Frame;

import java.applet.Applet;

import java.awt.*;

import java.awt.event.*;

import com.sun.j3d.utils.applet.MainFrame;

import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.universe.*;

import javax.media.j3d.*;

import javax.vecmath.*;



public class Test extends Applet {
    SimpleUniverse simpleU;


    public BranchGroup createSceneGraph() {

        BranchGroup objRoot = new BranchGroup();


        Appearance polygon1Appearance = new Appearance();
        Color3f color1 = new Color3f (1.0f, 1.0f, 0.0f);
        ColoringAttributes color1ca = new ColoringAttributes (color1, 1);
        polygon1Appearance.setColoringAttributes(color1ca);



        QuadArray polygon1 = new QuadArray (4, QuadArray.COORDINATES);
        polygon1.setCoordinate (0, new Point3f (0f, 0f, 0f));
        polygon1.setCoordinate (1, new Point3f (2f, 0f, 0f));
        polygon1.setCoordinate (2, new Point3f (2f, 3f, 0f));
        polygon1.setCoordinate (3, new Point3f (0f, 3f, 0f));
        objRoot.addChild(new Shape3D(polygon1,polygon1Appearance));

        return objRoot;


    }

    public void init() {



        setLayout(new BorderLayout());

        Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        add("Center", c);



        BranchGroup scene = createSceneGraph();

        simpleU = new SimpleUniverse(c);

        TransformGroup tg = simpleU.getViewingPlatform().getViewPlatformTransform();

        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0f,0f,10f));
        tg.setTransform(t3d);

        scene.compile();

        simpleU.addBranchGraph(scene);

    }

    public void destroy(){
        simpleU.removeAllLocales();
    }



    public static void main(String[] args) {

        Frame frame = new MainFrame(new Test(), 500, 500);

    }

}
