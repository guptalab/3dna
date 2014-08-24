import java.awt.Frame;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.*;

public class Test extends Applet { 
        SimpleUniverse simpleU; 
        static boolean application = false;
    
    public Test (){    
    }    

    public void init() { 
      
    setLayout(new BorderLayout()); 
    Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration()); 
        add("Center", c);    
    simpleU= new SimpleUniverse(c); // setup the SimpleUniverse, attach the Canvas3D
        BranchGroup scene = createSceneGraph(); 
        simpleU.getViewingPlatform().setNominalViewingTransform();
        scene.compile(); 
        simpleU.addBranchGraph(scene); //add your SceneGraph to the SimpleUniverse   
    }

    public BranchGroup createSceneGraph() {      

    BranchGroup objRoot = new BranchGroup(); 
    TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        try
    {   
        Scene s = null;
        ObjectFile f = new ObjectFile ();
            f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);

        if (application == false){
            java.net.URL airFile = new java.net.URL (getCodeBase(), "airtable.obj");
            s = f.load (airFile);
            tg.addChild (s.getSceneGroup ());
    
        }
        else {
            String s1 = "airtable.obj";
            s = f.load (s1);
            tg.addChild (s.getSceneGroup ());
        }

    }

        catch (java.net.MalformedURLException ex){  
        }
        catch (java.io.FileNotFoundException ex){
        }


        BoundingSphere bounds = new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 100.0);

        Color3f ambientColor = new Color3f (0.5f, 0.5f, 0.5f);

        AmbientLight ambientLightNode = new AmbientLight (ambientColor);

        ambientLightNode.setInfluencingBounds (bounds);

        objRoot.addChild (ambientLightNode);    


        t3d.setTranslation(new Vector3f(0f,0f,-5f));
        tg.setTransform(t3d);
        objRoot.addChild(tg);
        return objRoot;
        }

        public void destroy() { 
        simpleU.removeAllLocales();    
        }  

        public static void main(String[] args) {
        application = true;    
            Frame frame = new MainFrame(new Test(), 500, 500);    
        }
}
