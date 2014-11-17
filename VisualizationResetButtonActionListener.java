import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lenovo on 9/15/2014.
 */
public class VisualizationResetButtonActionListener implements ActionListener {
    @Override   
    public void actionPerformed(ActionEvent e) {
        //set the ViewingPlatform by setting the canvasx, canvasy, canvasz values as 0.0,0.0,2.0
        VisualizeActionListener.canvasX=0.0f;
        VisualizeActionListener.canvasY=0.0f;
        VisualizeActionListener.canvasZ=20.0f;

        ImportActionListener.canvasx=0.0f;
        ImportActionListener.canvasy=0.0f;
        ImportActionListener.canvasz=2.0f;

        if(MainFrame.isImported){
            ViewingPlatform vp = ImportActionListener.simpleU.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse

            TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

            Transform3D View_Transform3D = new Transform3D();	// create a Transform3D for the ViewingPlatform
            View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform

            View_Transform3D.setTranslation(new Vector3f(ImportActionListener.canvasx,
                    ImportActionListener.canvasy,ImportActionListener.canvasz)); // set 3d to  x=0, y=-1, z= 5
            View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform
        }
        else{
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
