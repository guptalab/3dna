/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import javax.imageio.ImageIO;
import javax.media.j3d.Transform3D;
import javax.swing.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SimulateImageActionListener implements ActionListener {
    static ArrayList<VoxelToBrick> DNASequenceData=SaveFinalSequences.FinalData;
    static CoordinatesSeqMap c=new CoordinatesSeqMap();
    public static Container contentPane;
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Simulation Pressed");
        JFrame gui = new JFrame("3DNA Visulaization");
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(1500,1000);
        gui.setResizable(false);
        ImageIcon imgd = new ImageIcon("images/logod.png");
        gui.setIconImage(imgd.getImage());
        contentPane = gui.getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(Color.WHITE);

        //code for Capture Screenshot
        JMenuBar menubar= new JMenuBar();
        JButton capture= new JButton("Capture Screenshot");
        menubar.add(capture);
        gui.setJMenuBar(menubar);
        capture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                BufferedImage image=getScereenshot(contentPane);
                try{
                    ImageIO.write(image,"png", new File("3DNA_screenshot.png"));
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null, "No Brick has been created. Use 'New' to create a Brick first",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        int localZ;
        int localx1;
        int localx2;
        int localy1;
        int localy2;
        for(int i=0;i<DNASequenceData.size();i++){
            if(!DNASequenceData.get(i).isVisualized){
                if(DNASequenceData.get(i).voxel1[0]==-1&&DNASequenceData.get(i).voxel2[0]==-1) {
                    localZ=DNASequenceData.get(i).voxel1[2];
                    localx1=DNASequenceData.get(i).voxel1[0];
                    localy1=DNASequenceData.get(i).voxel1[1];
                    localx2=DNASequenceData.get(i).voxel2[0];
                    localy2=DNASequenceData.get(i).voxel2[1];
                    if(localZ%4==0){//Condition for North Brick
                        DNASequenceData.get(i).isVisualized=true;
                        for (int j=0;j<DNASequenceData.size();j++){
                            if(!DNASequenceData.get(j).isVisualized){
                                if((localZ==DNASequenceData.get(j).voxel1[2]-1)&&(localx1==DNASequenceData.get(j).voxel2[0])
                                && (localy1==DNASequenceData.get(j).voxel2[1])){
                                    foundN1W3(localZ,localx1,localy1);
                                    DNASequenceData.get(j).isVisualized=true;
                                }
                                else if((localZ==DNASequenceData.get(j).voxel1[2]-1)&&(localx2==DNASequenceData.get(j).voxel1[0])
                                && (localy2==DNASequenceData.get(j).voxel1[1])){
                                    foundN4W2(localZ, localx2,localy2);
                                    DNASequenceData.get(j).isVisualized=true;
                                }
                                else if((localZ==DNASequenceData.get(j).voxel1[2]+1)&&(localx1==DNASequenceData.get(j).voxel2[0])
                                && (localy1==DNASequenceData.get(j).voxel2[1])){
                                    foundN2E4(localZ,localx1,localy1);
                                    DNASequenceData.get(j).isVisualized=true;
                                }
                                else if((localZ==DNASequenceData.get(j).voxel1[2]+1)&&(localx2==DNASequenceData.get(j).voxel1[0])
                                && (localy2==DNASequenceData.get(j).voxel1[1])){
                                    foundN3E1(localZ,localx2,localy2);
                                    DNASequenceData.get(j).isVisualized=true;
                                }
                            }
                        }
                    }
                    else if(localZ%4==2){//Condition for South Brick
                        DNASequenceData.get(i).isVisualized=true;
                        for (int j=0;j<DNASequenceData.size();j++){
                            if(!DNASequenceData.get(j).isVisualized){
                                if((localZ==DNASequenceData.get(j).voxel1[2]-1)&&(localx1==DNASequenceData.get(j).voxel2[0])
                                        && (localy1==DNASequenceData.get(j).voxel2[1])){
                                    foundS1E3(localZ,localx1,localy1);
                                    DNASequenceData.get(j).isVisualized=true;
                                }
                                else if((localZ==DNASequenceData.get(j).voxel1[2]-1)&&(localx2==DNASequenceData.get(j).voxel1[0])
                                        && (localy2==DNASequenceData.get(j).voxel1[1])){
                                    foundS4E2(localZ,localx2,localy2);
                                    DNASequenceData.get(j).isVisualized=true;
                                }
                                else if((localZ==DNASequenceData.get(j).voxel1[2]+1)&&(localx1==DNASequenceData.get(j).voxel2[0])
                                        && (localy1==DNASequenceData.get(j).voxel2[1])){
                                    foundS2W4(localZ,localx1,localy1);
                                    DNASequenceData.get(j).isVisualized=true;
                                }
                                else if((localZ==DNASequenceData.get(j).voxel1[2]+1)&&(localx2==DNASequenceData.get(j).voxel1[0])
                                        && (localy2==DNASequenceData.get(j).voxel1[1])){
                                    foundS3W1(localZ,localx2,localy2);
                                    DNASequenceData.get(j).isVisualized=true;
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Simulation Finished");
    }
    public static BufferedImage getScereenshot(Component component){
        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        component.paint( image.getGraphics() );
        return image;
    }
    public static void addNorthBrick(int x,int y,int z,String details){
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("Brick_Images/north.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setBounds(x,y,30,200);
            picLabel.setToolTipText(details);
            contentPane.add(picLabel);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public static void addSouthBrick(int x,int y,int z,String details){
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("Brick_Images/south.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setBounds(x,y,30,200);
            picLabel.setToolTipText(details);
            contentPane.add(picLabel);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public static void addEastBrick(int x,int y,int z,String details){
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("Brick_Images/east.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setBounds(x,y,200,30);
            picLabel.setToolTipText(details);
            contentPane.add(picLabel);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public static void addWestBrick(int x,int y,int z,String details){
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("Brick_Images/west.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setBounds(x,y,200,30);
            picLabel.setToolTipText(details);
            contentPane.add(picLabel);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public static void addBond(int x,int y,int z,String details){
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("Brick_Images/bonds.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setBounds(x,y,24,24);
            picLabel.setToolTipText(details);
            contentPane.add(picLabel);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public static void foundN1W3(int z,int x, int y){
        addBond(x+30-24,y+200-(27/2)-24,2,new String("Bonds"));
        addWestBrick(x+30-30-5,y+200-(30/2)-30-10,2,new String("West Brick"));
    }
    public static void foundN4W2(int z,int x, int y){
        addBond(x+30-(24/2)-10,y-10,2,new String("Bonds"));
        addWestBrick(x+30-200+10,y-(30/2)-10,2,new String("West Brick"));
    }
    public static void foundN2E4(int z,int x, int y){
        addBond(x-(24/2),y+200-24,2,new String("Bonds"));
        addEastBrick(x - 200, y + 200 - 24, 2, new String("East Brick"));
    }
    public static void foundN3E1(int z,int x, int y){
        addBond(x-(24/2),y+30-24,2,new String("Bonds"));
        addEastBrick(x-(30/2)-24,y+30-24,2,new String("East Brick"));

    }
    public static void foundS1E3(int z,int x, int y){
        addBond(x+30-24,y-10,2,new String("Bonds"));
        addEastBrick(x-200+30+10,y-(30/2)-(24/2),2,new String("East Brick"));
    }
    public static void foundS4E2(int z,int x, int y){
        addBond(x+30-24,y+200-(30/2)-24,2,new String("Bonds"));
        addEastBrick(800-(24/2)+5,200+200-(30/2)-30-5,2,new String("East Brick"));
    }
    public static void foundS2W4(int z,int x, int y){
        addBond(x-10,y-30+24+10,2,new String("Bonds"));
        addWestBrick(x-(30/2)-25,y-30+24+10,2,new String("West Brick"));
    }
    public static void foundS3W1(int z,int x, int y){
        addBond(x-10,y+200-24,2,new String("Bonds"));
        addWestBrick(x-200+(30/2)-10,y+200-(30/2)-10,2,new String("West Brick"));
    }
}