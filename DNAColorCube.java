import com.sun.j3d.utils.geometry.ColorCube;
import javax.media.j3d.QuadArray;


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
    public DNAColorCube() {
        super();
        QuadArray cubeArray= (QuadArray)this.getGeometry();
        cubeArray.setColor(0,colorLightGreen);
        cubeArray.setColor(1,colorLightGreen);
        cubeArray.setColor(2,colorLightGreen);
        cubeArray.setColor(3,colorLightGreen);
        cubeArray.setColor(4,colorLightGray);
        cubeArray.setColor(5,colorLightGray);
        cubeArray.setColor(6,colorLightGray);
        cubeArray.setColor(7,colorLightGray);
        cubeArray.setColor(8,colorLightSteelBlue);
        cubeArray.setColor(9,colorLightSteelBlue);
        cubeArray.setColor(10,colorLightSteelBlue);
        cubeArray.setColor(11,colorLightSteelBlue);
        cubeArray.setColor(12,colorLemonChiffon);
        cubeArray.setColor(13,colorLemonChiffon);
        cubeArray.setColor(14,colorLemonChiffon);
        cubeArray.setColor(15,colorLemonChiffon);
        cubeArray.setColor(16,colorMistyRose);
        cubeArray.setColor(17,colorMistyRose);
        cubeArray.setColor(18,colorMistyRose);
        cubeArray.setColor(19,colorMistyRose);

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
