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
    public static final float[] colorLightSteelBlue = {

            176/255f, 196/255f, 222/255f,
            176/255f, 196/255f, 222/255f,
            176/255f, 196/255f, 222/255f,
            176/255f, 196/255f, 222/255f
    };
    public static final float[] colorLemonChiffon = {

            1f, 250/255f, 205/255f,
            1f, 250/255f, 205/255f,
            1f, 250/255f, 205/255f,
            1f, 250/255f, 205/255f
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
        cubeArray.setColor(4,colorLightGray );
        cubeArray.setColor(5,colorLightGray );
        cubeArray.setColor(6,colorLightGray );
        cubeArray.setColor(7,colorLightGray );
        cubeArray.setColor(8,colorMistyRose );
        cubeArray.setColor(9,colorMistyRose );
        cubeArray.setColor(10,colorMistyRose );
        cubeArray.setColor(11,colorMistyRose );
        cubeArray.setColor(12,colorLightSteelBlue);
        cubeArray.setColor(13,colorLightSteelBlue);
        cubeArray.setColor(14,colorLightSteelBlue);
        cubeArray.setColor(15,colorLightSteelBlue);
        cubeArray.setColor(16,colorLemonChiffon);
        cubeArray.setColor(17,colorLemonChiffon);
        cubeArray.setColor(18,colorLemonChiffon);
        cubeArray.setColor(19,colorLemonChiffon);
        cubeArray.setColor(20,colorLightSteelBlue);
        cubeArray.setColor(21,colorLightSteelBlue);
        cubeArray.setColor(22,colorLightSteelBlue);
        cubeArray.setColor(23,colorLightSteelBlue);
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
