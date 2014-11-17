/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */

import com.sun.j3d.utils.geometry.ColorCube;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.QuadArray;
import javax.media.j3d.*;
import javax.vecmath.Vector3f;

import java.awt.*;


public class DNAProtectorCube extends DNAColorCube{

    public int xCord;
    public int yCord;
    public int zCord;
    public static final float[] protectedCubeColor= {
            130/255f, 234/255f, 255/255f,
            130/255f, 234/255f, 255/255f,
            130/255f, 234/255f, 255/255f,
            130/255f, 234/255f, 255/255f,
    };

    public DNAProtectorCube(float f) {
        super(f);
        QuadArray cubeArray= (QuadArray)this.getGeometry();
        cubeArray.setColor(0,protectedCubeColor);
        cubeArray.setColor(1,protectedCubeColor);
        cubeArray.setColor(2,protectedCubeColor);
        cubeArray.setColor(3,protectedCubeColor);
        cubeArray.setColor(4,protectedCubeColor);
        cubeArray.setColor(5,protectedCubeColor);
        cubeArray.setColor(6,protectedCubeColor);
        cubeArray.setColor(7,protectedCubeColor);
        cubeArray.setColor(8,protectedCubeColor);
        cubeArray.setColor(9,protectedCubeColor);
        cubeArray.setColor(10,protectedCubeColor);
        cubeArray.setColor(11,protectedCubeColor);
        cubeArray.setColor(12,protectedCubeColor);
        cubeArray.setColor(13,protectedCubeColor);
        cubeArray.setColor(14,protectedCubeColor);
        cubeArray.setColor(15,protectedCubeColor);
        cubeArray.setColor(16,protectedCubeColor);
        cubeArray.setColor(17,protectedCubeColor);
        cubeArray.setColor(18,protectedCubeColor);
        cubeArray.setColor(19,protectedCubeColor);
        cubeArray.setColor(20,protectedCubeColor);
        cubeArray.setColor(21,protectedCubeColor);
        cubeArray.setColor(22,protectedCubeColor);
        cubeArray.setColor(23,protectedCubeColor);
    }

}
