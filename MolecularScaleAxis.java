import com.sun.j3d.utils.geometry.Cylinder;

import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

/**
 * Created by shikhar.kumar-gupta on 20.08.2014.
 */
public class MolecularScaleAxis extends Shape3D {

    public MolecularScaleAxis(){
        LineArray lineArray1 = new LineArray(2, LineArray.COORDINATES);
        lineArray1.setCoordinate(0, new Point3f(-1.0f,0.0f,0.0f));
        lineArray1.setCoordinate(0, new Point3f(1.0f,0.0f,0.0f));
        new Shape3D(lineArray1);
    }
}
