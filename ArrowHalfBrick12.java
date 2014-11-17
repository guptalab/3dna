import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.LineArray;
import javax.vecmath.Point3f;

/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */
public class ArrowHalfBrick12 extends TransformGroup {
    public ArrowHalfBrick12(){
        super();

        float x1 = 0f;
        float y1 = 0f;
        float z1 = -0.50f;

        float x2 = 0f;
        float y2 = 0f;
        float z2 = 0f;

        float x5 = 0f;
        float y5 = 0.02f;
        float z5 = -0.02f;

        float x6 = 0f;
        float y6 = -0.02f;
        float z6 = -0.02f;

        //Initializing LineArrays for the Molecular Axis
        LineArray l1LineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray l4LineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray l5LineArray = new LineArray(2, LineArray.COORDINATES);

        //Initializing the LineArray(s)


        l1LineArray.setCoordinate(0,new Point3f(x1,y1,z1));
        l1LineArray.setCoordinate(1,new Point3f(x2,y2,z2));

        l4LineArray.setCoordinate(0,new Point3f(x2,y2,z2));
        l4LineArray.setCoordinate(1,new Point3f(x5,y5,z5));

        l5LineArray.setCoordinate(0,new Point3f(x2,y2,z2));
        l5LineArray.setCoordinate(1,new Point3f(x6,y6,z6));

        this.addChild(new Shape3D(l1LineArray));
        this.addChild(new Shape3D(l4LineArray));
        this.addChild(new Shape3D(l5LineArray));
    }
}
