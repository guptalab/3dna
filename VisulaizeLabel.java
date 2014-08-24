import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

public class VisulaizeLabel {
    public static int xCord;
    public static int yCord;
    public static int zCord;
    public static String brickType;
    public static JLabel picLabel;
    public static boolean isSelected=true;

    public void setLocation(int x, int y, int z){
        xCord=x;
        yCord=y;
        zCord=z;
    }
    public void setType(String type){
        brickType=type;
    }

    public JLabel getShape(){
        if(brickType.equals("North")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/north.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,30,225);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(brickType.equals("South")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/south.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,30,225);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(brickType.equals("East")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/east.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,225,30);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(brickType.equals("West")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/west.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,225,30);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(brickType.equals("WestHalf")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/west_half.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,35,30);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(brickType.equals("NorthHalf")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/north_half.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,30,30);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(brickType.equals("SouthHalf")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/south_half.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,30,29);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(brickType.equals("EastHalf")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/east_half.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,35,29);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(brickType.equals("Bond")){
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File("Brick_Images/bond.png"));
                picLabel = new JLabel(new ImageIcon(myPicture));
                picLabel.setBounds(xCord,yCord,23,20);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return picLabel;
    }
}
