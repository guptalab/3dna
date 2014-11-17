import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lenovo on 9/15/2014.
 */
public class VisualizationDownButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if(MainFrame.isImported){
            ImportActionListener.canvasy=ImportActionListener.canvasy+VisualizeActionListener.visualizeCanvasStep;
            ViewingPlatform vp = ImportActionListener.simpleU.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse

            TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

            Transform3D View_Transform3D = new Transform3D();	// create a Transform3D for the ViewingPlatform
            View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform

            View_Transform3D.setTranslation(new Vector3f(ImportActionListener.canvasx,
                    ImportActionListener.canvasy,ImportActionListener.canvasz)); // set 3d to  x=0, y=-1, z= 5
            View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform
        }
        else if(VisualizePlaneActionListener.isVisualizePlaneSelected){
            VisualizePlaneActionListener.canvasY=VisualizePlaneActionListener.canvasY+VisualizePlaneActionListener.visualizePlaneCanvasStep;
            ViewingPlatform vp = VisualizePlaneActionListener.visualizationPlaneSimpleUniverse.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse

            TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

            Transform3D View_Transform3D = new Transform3D();	// create a Transform3D for the ViewingPlatform
            View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform

            View_Transform3D.setTranslation(new Vector3f(VisualizePlaneActionListener.canvasX,
                    VisualizePlaneActionListener.canvasY,VisualizePlaneActionListener.canvasZ)); // set 3d to  x=0, y=-1, z= 5
            View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform
        }
        else{
            VisualizeActionListener.canvasY=VisualizeActionListener.canvasY+VisualizeActionListener.visualizeCanvasStep;
            ViewingPlatform vp = VisualizeActionListener.visualizationSimpleUniverse.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse

            TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

            Transform3D View_Transform3D = new Transform3D();	// create a Transform3D for the ViewingPlatform
            View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform

            View_Transform3D.setTranslation(new Vector3f(VisualizeActionListener.canvasX,
                    VisualizeActionListener.canvasY,VisualizeActionListener.canvasZ)); // set 3d to  x=0, y=-1, z= 5
            View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform
        }
    }
}
