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


public class DNAColorCube extends ColorCube{

    public int xCord;
    public int yCord;
    public int zCord;
    public static final float[] colorLightGreen = {

            144/255f, 238/255f, 144/255f,
            144/255f, 238/255f, 144/255f,
            144/255f, 238/255f, 144/255f,
            144/255f, 238/255f, 144/255f
    };
    public static final float[] colorLightGray = {

            0.8f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.8f,
            0.8f, 0.8f, 0.8f
    };
    public static final float[] colorMistyRose = {

            1f, 228/255f, 225/255f,
            1f, 228/255f, 225/255f,
            1f, 228/255f, 225/255f,
            1f, 228/255f, 225/255f
    };
    double scale;
    public DNAColorCube(float f) {
        super(f);
        QuadArray cubeArray= (QuadArray)this.getGeometry();
        cubeArray.setColor(0,colorLightGreen);
        cubeArray.setColor(1,colorLightGreen);
        cubeArray.setColor(2,colorLightGreen);
        cubeArray.setColor(3,colorLightGreen);
        cubeArray.setColor(4,colorLightGreen);
        cubeArray.setColor(5,colorLightGreen);
        cubeArray.setColor(6,colorLightGreen);
        cubeArray.setColor(7,colorLightGreen);
        cubeArray.setColor(8,colorLightGray);
        cubeArray.setColor(9,colorLightGray);
        cubeArray.setColor(10,colorLightGray);
        cubeArray.setColor(11,colorLightGray);
        cubeArray.setColor(12,colorLightGray    );
        cubeArray.setColor(13,colorLightGray    );
        cubeArray.setColor(14,colorLightGray    );
        cubeArray.setColor(15,colorLightGray    );
        cubeArray.setColor(16,colorMistyRose);
        cubeArray.setColor(17,colorMistyRose);
        cubeArray.setColor(18,colorMistyRose);
        cubeArray.setColor(19,colorMistyRose);
        cubeArray.setColor(20,colorMistyRose);
        cubeArray.setColor(21,colorMistyRose);
        cubeArray.setColor(22,colorMistyRose);
        cubeArray.setColor(23,colorMistyRose);
    }
    public void setCordinate(int x, int y, int z){
        xCord=x;
        yCord=y;
        zCord=z;
    }
    public int getX(){
        return xCord;
    }
    public int getY(){
        return yCord;
    }
    public int getZ(){
        return zCord;
    }
    public void printDetails(){
        System.out.println("New DNA Cube has been added with xCord "+xCord+" yCord"+yCord+" zCord"+zCord);
    }

}
