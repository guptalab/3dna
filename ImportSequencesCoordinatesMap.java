
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class ImportSequencesCoordinatesMap {

    static ArrayList<DNASequence> randomSequencesList= new ArrayList<DNASequence>();
    static ArrayList<DNASequence> sequencesList= new ArrayList<DNASequence>();
    static ArrayList<HelixAndCoordinates> fullBrickSequencesList = new ArrayList<HelixAndCoordinates>();
    static ArrayList<HelixAndCoordinates> halfBrickSequencesList = new ArrayList<HelixAndCoordinates>();
    static ArrayList<HelixAndCoordinates> boundaryBrickSequencesList = new ArrayList<HelixAndCoordinates>();
    public static ArrayList<XYZCoordinates> voxelList = new ArrayList<XYZCoordinates>();
    static ArrayList<VoxelToBrick> brickList=new ArrayList<VoxelToBrick>();
    static ArrayList<VoxelToBrick> boundaryBrickList=new ArrayList<VoxelToBrick>();
    //using a 3d array to store the position for each voxel in the arraylist coordinate-list
    static int[][][] ivalue = new int[20][20][20];
    int[][][] newIndex = new int[MainFrame.width][MainFrame.height][MainFrame.depth+2];
    int x,y,z,indexfront, indexback;
    String poly1T="TTTTTTTT";
    static int i1, i2, i3, i4;


    //constructor for the class
    public ImportSequencesCoordinatesMap() {
        //clear all arraylists
        voxelList.clear();
        for (int k = 0; k <= MainFrame.depth; k++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int i = 0; i < MainFrame.width; i++)
                    ivalue[i][j][k] = -1;
        readFullBrickSequences();
        readHalfBrickSequences();
        readBoundaryBrickSequences();
        addCoordinates();
        identifyBoundaryVoxels();
        assignOrientation();
        assignDomain();
        assignNullSequences();

        brickList.clear();
        MainFrame.enableGraph();
        convertToT();
        //removeTVoxels();
        //set all values in newIndex to -1
        for(int i = 0; i<MainFrame.width; i++)
            for(int j = 0; j<MainFrame.height; j++)
                for(int k = 0; k<=MainFrame.depth; k++)
                    newIndex [i][j][k] = -1;
        setBrickList();
        addHelixNumber();
        mapSequencesFromList();
        if (MainFrame.isXCrystalEnabled || MainFrame.isYCrystalEnabled || MainFrame.isZCrystalEnabled)
            createCrystal();
        if(MainFrame.isBoundaryCalled || MainFrame.isCavityEnabled)
            attachBoundaryBricks();


    }


    /**
     * Reads strand number, plate number, position, sequences, helix and coordinates for 32nt and saves it to the arraylist - fullBrickSequencesList
     */
    public static void readFullBrickSequences() {
        int fullBrickCounter = 0;
        fullBrickSequencesList.clear();
        //String csvFile = MainFrame.importedFullBrickFilePath;
        String csvFile ="32ntFile.csv"; //for now
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use semicolon
                String[] data = line.split(cvsSplitBy);
                HelixAndCoordinates h = new HelixAndCoordinates( Integer.parseInt(data[7]),
                        Integer.parseInt(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[10]), Integer.parseInt(data[11]),
                        Integer.parseInt(data[12]), Integer.parseInt(data[13]), Integer.parseInt(data[14]),-1, -1, -1, -1,
                        Integer.parseInt(data[15]),  Integer.parseInt(data[16]),  Integer.parseInt(data[17]),  Integer.parseInt(data[18]));
                fullBrickSequencesList.add(h);
                fullBrickSequencesList.get(fullBrickCounter).addData(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),data[3].toString(), data[4].toString(), data[5].toString(), data[6].toString(), null, null);
                fullBrickCounter++;
                //System.out.println("full data added to fullBrickList");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Reads strand number, plate number, position, sequences, helix and coordinates for 16nt and saves it to the arraylist - halfBrickSequencesList
     */
    public static void readHalfBrickSequences() {

        int halfBrickCounter = 0;
        halfBrickSequencesList.clear();
        //String csvFile = MainFrame.importedHalfBrickFilePath;
        String csvFile ="16ntFile.csv"; //for now
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use semicolon
                String[] data = line.split(cvsSplitBy);
                HelixAndCoordinates h = new HelixAndCoordinates( Integer.parseInt(data[5]),
                        Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]), -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0);
                halfBrickSequencesList.add(h);
                //System.out.println("new added to halfBrickList");
                halfBrickSequencesList.get(halfBrickCounter).addData(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),data[3].toString(), data[4].toString(), null, null, null, null);
                halfBrickCounter++;
                //System.out.println("more data added to halfBrickList");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Reads strand number, plate number, position, sequences, helix and coordinates for 48nt and saves it to the arraylist - boundaryBrickSequencesList
     */
    public static void readBoundaryBrickSequences() {

        int boundaryBrickCounter = 0;
        boundaryBrickSequencesList.clear();
        //String csvFile = MainFrame.importedBoundaryBrickFilePath;
        String csvFile ="48ntFile.csv"; //for now
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use semicolon
                String[] data = line.split(cvsSplitBy);
                HelixAndCoordinates h = new HelixAndCoordinates( Integer.parseInt(data[9]),
                        Integer.parseInt(data[10]), Integer.parseInt(data[11]), Integer.parseInt(data[12]), Integer.parseInt(data[13]),
                        Integer.parseInt(data[14]), Integer.parseInt(data[15]), Integer.parseInt(data[16]), Integer.parseInt(data[17]),
                        Integer.parseInt(data[18]), Integer.parseInt(data[19]), Integer.parseInt(data[20]), 0, 0, 0, 0);
                boundaryBrickSequencesList.add(h);
                //System.out.println("new added to halfBrickList");
                boundaryBrickSequencesList.get(boundaryBrickCounter).addData(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),data[3].toString(), data[4].toString(), data[5].toString(), data[6].toString(), data[7].toString(), data[8].toString());
                boundaryBrickCounter++;
                //System.out.println("more data added to halfBrickList");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Stores all coordinates of the 3D canvas into the arrayList voxelList, identifies head and tail protector bricks and mark all voxels as removed=true or removed=false.
     */
    public static void addCoordinates() {

        int count = 0;
        int x, y, z;
        for (int k = 0; k <= MainFrame.depth; k++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int i = 0; i < MainFrame.width; i++) {

                    voxelList.add(new XYZCoordinates(i, j, k));
                    ivalue[i][j][k] = count;
                    if(k == 0){
                        MainFrame.deletedCoordinates[i][j][k] = true;
                    }
                    //while storing the voxel coordinates, find out if it has been deleted
                    if (MainFrame.deletedCoordinates[i][j][k] == true) {
                        voxelList.get(count).isRemoved = true;
                    }
                    count++;
                }

        //now we identify the head bricks i.e. the coordinates for min. z for each (x,y) pair so that protector bricks may be attached to them.
        for (int i = 0; i < MainFrame.width; i++)
            for (int j = 0; j < MainFrame.height; j++) {
                z = 1;
                while (voxelList.get(ivalue[i][j][z]).isRemoved == true && z < MainFrame.depth) {
                    z++;
                }
                if(z < MainFrame.depth) // if z >= means that an entire row along z axis has been removed
                    voxelList.get(ivalue[i][j][z]).isHeadBrick = true;
            }
        ////now we identify the tail bricks i.e. the coordinates for max. z for each (x,y) pair so that protector bricks may be added to them.
        for (int i = MainFrame.width - 1; i >= 0; i--)
            for (int j = MainFrame.height - 1; j >= 0; j--) {
                z = MainFrame.depth;
                while (voxelList.get(ivalue[i][j][z]).isRemoved == true && z > 0) {
                    z--;
                }
                if (voxelList.get(ivalue[i][j][z]).isRemoved == false) { // since no new brick is added for tail brick
                    voxelList.get(ivalue[i][j][z]).isTailBrick = true;
                }
            }
        //adding the protector bricks for the head and tail bricks
        int s = voxelList.size();
        if (MainFrame.isMinZProtectorPlaneEnabled == true) {
            for (int i = 0; i < s; i++) {
                if (voxelList.get(i).isHeadBrick && voxelList.get(i).isRemoved == false) {
                    x = voxelList.get(i).x;
                    y = voxelList.get(i).y;
                    z = voxelList.get(i).z - 1;
                    ///when a protector brick is added in place of a removed brick,
                    // in order to avoid adding a new element to the arraylist,
                    // we simply set the removed brick to head brick.
                    if (voxelList.get(ivalue[x][y][z]).isRemoved == true) {
                        voxelList.get(ivalue[x][y][z]).isHeadBrick = true;
                    }

                    //the previously assigned head brick is now set to false as now the protector brick is the new head brick.
                    voxelList.get(i).isHeadBrick = false;
                }
            }
            //when a protector brick is added in place of a removed brick, the isRemoved condition is set to false.
            for (int i = 0; i < voxelList.size(); i++) {
                if (voxelList.get(i).isHeadBrick && voxelList.get(i).isRemoved == true) {
                    voxelList.get(i).isRemoved = false;
                }
            }
        }
        if (MainFrame.isMaxZProtectorPlaneEnabled == true) {
            //when a protector brick is added in place of a removed brick, the isRemoved condition is set to false.

        }
    }

    /**
     * Identifies all the half bricks on the top (min Y), bottom (max Y), left (min X) and right (max X) surfaces of the canvas that can be converted to boundary bricks
     */
    public void identifyBoundaryVoxels() {
        int minX = 0, minY = 0, x, y;
        int maxX = MainFrame.width - 1;
        int maxY = MainFrame.height - 1;
        int maxZ = MainFrame.depth;
        int minZ = 1;

        if (MainFrame.isMaxZProtectorPlaneEnabled)
            maxZ = MainFrame.depth;
        if (MainFrame.isMinZProtectorPlaneEnabled)
            minZ = 0;

        for (int z = minZ; z <= maxZ; z++)
            for (y = 0; y <= MainFrame.height; y++) {
                minX = 0;
                maxX = MainFrame.width - 1;
                while (voxelList.get(ivalue[minX][y][z]).isRemoved == true && minX < MainFrame.width) {
                    minX++;
                }
                if (voxelList.get(ivalue[minX][y][z]).isRemoved == false) {
                    voxelList.get(ivalue[minX][y][z]).isLeftmostBrick = true;
                    //System.out.println("Leftmost brick at " + minX + " " + y + " " + z);
                }

                while (voxelList.get(ivalue[maxX][y][z]).isRemoved == true && maxX > 0) {
                    maxX--;
                }
                if (voxelList.get(ivalue[maxX][y][z]).isRemoved == false) {
                    voxelList.get(ivalue[maxX][y][z]).isRightmostBrick = true;
                    //System.out.println("Rightmost brick at " + maxX + " " + y + " " + z);
                }

            }

        for (int z = minZ; z <= maxZ; z++)
            for (x = 0; x < MainFrame.width; x++) {
                minY = 0;
                maxY = MainFrame.height - 1;
                while (voxelList.get(ivalue[x][minY][z]).isRemoved == true && minY < MainFrame.height) {
                    minY++;
                }
                if (voxelList.get(ivalue[x][minY][z]).isRemoved == false) {
                    voxelList.get(ivalue[x][minY][z]).isTopmostBrick = true;
                }

                while (voxelList.get(ivalue[x][maxY][z]).isRemoved == true && maxY > 0) {
                    maxY--;
                }
                if (voxelList.get(ivalue[x][maxY][z]).isRemoved == false) {
                    voxelList.get(ivalue[x][maxY][z]).isBottommostBrick = true;
                }
            }
    }

    /**
     * Assigns orientation (north/ west/ south/ east) for all voxels in the voxelList
     */
    public static void assignOrientation() {
        for (int i = 0; i < voxelList.size(); i++) {
            if (voxelList.get(i).z % 4 == 0) {
                voxelList.get(i).orientation = 'n';
            } else if (voxelList.get(i).z % 4 == 1) {
                voxelList.get(i).orientation = 'w';
            } else if (voxelList.get(i).z % 4 == 2) {
                voxelList.get(i).orientation = 's';
            } else {
                voxelList.get(i).orientation = 'e';
            }

        }
    }

    /**
     * Assigns domain numbers (1-2 or 3-4) to each voxel in the coordinate-list
     */
    public static void assignDomain() {

        for (int i = 0; i < voxelList.size(); i++) {
            //Identification of north-bricks
            if (voxelList.get(i).orientation == 'n') {
                //identifying half-north-bricks
                // 1. when height is even half-brick is at coordinates x%2=0 and y=0 or at y=height-1 and x%2=0
                // 2. when height is odd half-brick is at coordinates x%2=0 and y=0 or at y=height-1 and x%2=1
                if (voxelList.get(i).y == 0 && voxelList.get(i).x % 2 == 0) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 0 && voxelList.get(i).y == MainFrame.height - 1 && voxelList.get(i).x % 2 == 0) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 1 && voxelList.get(i).y == MainFrame.height - 1 && voxelList.get(i).x % 2 == 1) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                }

                //identifying other full-north-bricks
                else if (((voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 0)) ||
                        (voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 1)) {
                    voxelList.get(i).editdomain(34);
                } else
                    voxelList.get(i).editdomain(12);
            }
            //Identifying west-bricks
            else if (voxelList.get(i).orientation == 'w') {
                //Identifying half-west-bricks
                // 1. when width is even, half-bricks are at coordinates x=0 and y%2=1 or at y%2=1 and x=width-1
                // 2. when width is odd, half-bricks are at coordinates x=0 and y%2=1 or at y%2=0 and x=width-1
                if (voxelList.get(i).x == 0 && voxelList.get(i).y % 2 == 1) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 0 && voxelList.get(i).x == MainFrame.width - 1 && voxelList.get(i).y % 2 == 1) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 1 && voxelList.get(i).x == MainFrame.width - 1 && voxelList.get(i).y % 2 == 0) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                }

                //Identifying other full-west-bricks
                else if (((voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 0) &&
                        (voxelList.get(i).x != 0)) ||
                        (voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 1)) {
                    voxelList.get(i).editdomain(12);
                } else
                    voxelList.get(i).editdomain(34);
            }
            //Identifying south-bricks
            else if (voxelList.get(i).orientation == 's') {
                //Identifying half-south-bricks
                // 1. when height is even , half-bricks are at coordinates x%2=1 and y=0 or at y=height-1 and x%2=1
                // 2. when height is odd , half-bricks are at coordinates x%2=1 and y=0 or at y=height-1 and x%2=0
                if (voxelList.get(i).y == 0 && voxelList.get(i).x % 2 == 1) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 0 && voxelList.get(i).y == MainFrame.height - 1 && voxelList.get(i).x % 2 == 1) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 1 && voxelList.get(i).y == MainFrame.height - 1 && voxelList.get(i).x % 2 == 0) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                }

                //Identifying other full-south-bricks
                else if ((voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 1) ||
                        (voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 0)) {
                    voxelList.get(i).editdomain(12);
                } else
                    voxelList.get(i).editdomain(34);
            }
            //Identifying east-bricks
            else if (voxelList.get(i).orientation == 'e') {
                //Identifying half-east-bricks
                // 1. when width is even ,the half-bricks are at coordinates x=0 and y%2=0 or at y%2=0 and x=width-1
                // 2. when width is odd ,the half-bricks are at coordinates x=0 and y%2=0 or at y%2=1 and x=width-1
                if (voxelList.get(i).x == 0 && voxelList.get(i).y % 2 == 0) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 0 && voxelList.get(i).x == MainFrame.width - 1 && voxelList.get(i).y % 2 == 0) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 1 && voxelList.get(i).x == MainFrame.width - 1 && voxelList.get(i).y % 2 == 1) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                }

                //Identifying other full-east-bricks
                else if (((voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 1)) ||
                        (voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 0)) {
                    voxelList.get(i).editdomain(12);
                } else
                    voxelList.get(i).editdomain(34);
            }
            //missing sequences in yongang's file
            if(MainFrame.isReplaceWithTEnabled && voxelList.get(i).isHalfBrick && (voxelList.get(i).isHeadBrick && voxelList.get(i).z == 0))
                voxelList.get(i).isRemoved = true;
            if(MainFrame.isReplaceWithTEnabled && voxelList.get(i).isHalfBrick && ( voxelList.get(i).isTailBrick && voxelList.get(i).z == 10))
                voxelList.get(i).isRemoved = true;
        }

    }

    /**
     * Assigns all sequences as null by default
     */
    public static void assignNullSequences(){

        for(int i=0;i<voxelList.size();i++){

            voxelList.get(i).Domain1=null;
            voxelList.get(i).Domain2=null;
            voxelList.get(i).Domain3=null;
            voxelList.get(i).Domain4=null;

        }
    }

    /**
     * STEP 9: Replace the remaining complementary strands of the deleted voxels with (8-length) poly-T sequences.
     */
    public void convertToT(){
        for(int i=0;i<voxelList.size();i++){
            x=voxelList.get(i).x;
            y=voxelList.get(i).y;
            z=voxelList.get(i).z;

            //assign poly-1T domain to tail protector bricks
            if (MainFrame.isMaxZProtectorPlaneEnabled && MainFrame.isZCrystalEnabled == false){
                if(voxelList.get(i).isTailBrick && voxelList.get(i).domain == 12)
                    voxelList.get(i).isDomain1Replaced = true;
                else if(voxelList.get(i).isTailBrick && voxelList.get(i).domain == 34)
                    voxelList.get(i).isDomain4Replaced = true;
            }

            //assign poly-1T domain to head protector bricks
            if (MainFrame.isMinZProtectorPlaneEnabled && MainFrame.isZCrystalEnabled == false){
                if(voxelList.get(i).isHeadBrick && voxelList.get(i).domain == 12)
                    voxelList.get(i).isDomain2Replaced = true;
                else if(voxelList.get(i).isHeadBrick && voxelList.get(i).domain == 34)
                    voxelList.get(i).isDomain3Replaced = true;
            }

            /**
             *due to lack of random sequences in Peng Yin's case at z = 10 and z = 0, we initailly assign poly-1T domain to tail and head protector bricks
             */
            if (MainFrame.isZCrystalEnabled){
                if(z == 10 || z == 0){
                    if(voxelList.get(i).isTailBrick && voxelList.get(i).domain == 12)
                        voxelList.get(i).isDomain1Replaced = true;
                    else if(voxelList.get(i).isTailBrick && voxelList.get(i).domain == 34)
                        voxelList.get(i).isDomain4Replaced = true;
                    if(voxelList.get(i).isHeadBrick && voxelList.get(i).domain == 12)
                        voxelList.get(i).isDomain2Replaced = true;
                    else if(voxelList.get(i).isHeadBrick && voxelList.get(i).domain == 34)
                        voxelList.get(i).isDomain3Replaced = true;
                }
            }

            //if a voxel is adjacent to cavity, do not convert it to poly-1T strands
            if(voxelList.get(i).isRemoved){
                if(z > 0&& MainFrame.isMinZProtectorPlaneEnabled || z>1&&!MainFrame.isMinZProtectorPlaneEnabled){
                    indexback=ivalue[x][y][z-1];
                    if (voxelList.get(indexback).isRemoved == false && voxelList.get(indexback).isHeadBrick == false && voxelList.get(indexback).isTailBrick == false) {
                        if(MainFrame.isCavityEnabled) {
                            voxelList.get(indexback).isAdjacentToCavity = true;
                        }
                        else{
                            if (voxelList.get(i).domain == 34)
                                voxelList.get(indexback).isDomain1Replaced = true;

                            else
                                voxelList.get(indexback).isDomain4Replaced = true;
                        }
                    }
                }
                if((z>1&&!MainFrame.isMinZProtectorPlaneEnabled || z > 0&& MainFrame.isMinZProtectorPlaneEnabled )&& z <MainFrame.depth){
                    indexfront=ivalue[x][y][z+1];

                    if (voxelList.get(indexfront).isRemoved == false && voxelList.get(indexfront).isHeadBrick == false && voxelList.get(indexfront).isTailBrick == false) {
                        if(MainFrame.isCavityEnabled) {
                            voxelList.get(indexfront).isAdjacentToCavity = true;
                        }
                        else {
                            if (voxelList.get(i).domain == 34)
                                voxelList.get(indexfront).isDomain2Replaced = true;

                            else
                                voxelList.get(indexfront).isDomain3Replaced = true;
                        }
                    }
                }
                //identify the voxels on the top and bottom of the cavity and mark it as adjacentToCavity
                if (y > 0 && voxelList.get(ivalue [x][y-1][z]).isRemoved == false ) {
                    voxelList.get(ivalue[x][y - 1][z]).isAdjacentToCavity = true;
                }
                if (y < MainFrame.height - 1 && voxelList.get(ivalue [x][y+1][z]).isRemoved == false ) {
                    voxelList.get(ivalue[x][y + 1][z]).isAdjacentToCavity = true;
                }
                if (x > 0 && voxelList.get(ivalue [x-1][y][z]).isRemoved == false ) {
                    voxelList.get(ivalue[x - 1][y][z]).isAdjacentToCavity = true;
                }
                if (x < MainFrame.width - 1 && voxelList.get(ivalue [x+1][y][z]).isRemoved == false ) {
                    voxelList.get(ivalue[x + 1][y][z]).isAdjacentToCavity = true;
                }

            }
        }
    }

    //STEP 10: Identify and remove those voxels which have d3&d4 OR d1&d2 as T-sequences (i.e., when an entire voxel is composed of only Thymidine).
    public void removeTVoxels(){
        for(int i=0;i<voxelList.size();i++){

            x=voxelList.get(i).x;
            y=voxelList.get(i).y;
            z=voxelList.get(i).z;

            if(voxelList.get(i).isRemoved==false){
                if(voxelList.get(i).domain==34){
                    if(voxelList.get(i).Domain3.equals(poly1T)&&
                            voxelList.get(i).Domain4.equals(poly1T)){
                        System.out.println("removed in removeTVoxels:"+x+""+y+""+z);
                        voxelList.get(i).isRemoved=true;
                    }
                }
                else if(voxelList.get(i).domain==12){
                    if(voxelList.get(i).Domain1.equals(poly1T)&&
                            voxelList.get(i).Domain2.equals(poly1T)){
                        System.out.println("removed in removeTVoxels:"+x+""+y+""+z);
                        voxelList.get(i).isRemoved=true;
                    }
                }
            }
        }
    }

    //STEP 11: Combine voxels to form bricks of size=2 voxels, set them as full bricks and save them in the brickList list.
    public void setBrickList(){
        System.out.println("SetbrickList called");
        int x,y,z;
        int brickCounter = 0;
        for(int i=0;i<voxelList.size();i++){

            if(voxelList.get(i).isRemoved == false){

                x = voxelList.get(i).x;
                y = voxelList.get(i).y;
                z = voxelList.get(i).z;
                //Adding half-bricks with domains 3 and 4 to the final-coordinate-list.
                if(voxelList.get(i).isHalfBrick){
                    if(voxelList.get(i).domain==34) {
                        brickList.add(new VoxelToBrick(-1, -1, -1, x, y, z, null, null,
                                voxelList.get(i).Domain3, voxelList.get(i).Domain4));
                        brickList.get(brickCounter).isDomain3Replaced = voxelList.get(i).isDomain3Replaced;
                        brickList.get(brickCounter).isDomain4Replaced = voxelList.get(i).isDomain4Replaced;
                    }
                    else {
                        brickList.add(new VoxelToBrick(x, y, z, -1, -1, -1, voxelList.get(i).Domain1,
                                voxelList.get(i).Domain2, null, null));
                        brickList.get(brickCounter).isDomain1Replaced = voxelList.get(i).isDomain1Replaced;
                        brickList.get(brickCounter).isDomain2Replaced = voxelList.get(i).isDomain2Replaced;
                    }
                    brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                    brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                    brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                    brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                    brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                    brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                    brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity; //this check is added only to the half bricks
                }
                else{
                    //Identifying full and half north-bricks and adding them to the final-coordinate-list.
                    if(voxelList.get(i).orientation=='n'){
                        if(voxelList.get(i).domain==34){
                            if(voxelList.get(ivalue[x][y+1][z]).isRemoved==false){

                                brickList.add(new VoxelToBrick(x,y+1,z,x,y,z,
                                        voxelList.get(ivalue[x][y+1][z]).Domain1,
                                        voxelList.get(ivalue[x][y+1][z]).Domain2,
                                        voxelList.get(i).Domain3, voxelList.get(i).Domain4));

                                voxelList.get(ivalue[x][y+1][z]).isRemoved=true;
                                newIndex[x][y+1][z] = brickCounter;
                                brickList.get(brickCounter).isDomain1Replaced = voxelList.get(ivalue[x][y+1][z]).isDomain1Replaced;
                                brickList.get(brickCounter).isDomain2Replaced = voxelList.get(ivalue[x][y+1][z]).isDomain2Replaced;

                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x][y+1][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x][y+1][z]).isTailBrick || voxelList.get(i).isTailBrick;

                            }
                            else{
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        voxelList.get(i).Domain3, voxelList.get(i).Domain4));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                            brickList.get(brickCounter).isDomain3Replaced = voxelList.get(i).isDomain3Replaced;
                            brickList.get(brickCounter).isDomain4Replaced = voxelList.get(i).isDomain4Replaced;
                        }
                        else{
                            if(voxelList.get(ivalue[x][y-1][z]).isRemoved==false){

                                brickList.add(new VoxelToBrick(x,y,z,x,y-1,z,
                                        voxelList.get(i).Domain1,
                                        voxelList.get(i).Domain2,
                                        voxelList.get(ivalue[x][y-1][z]).Domain3,
                                        voxelList.get(ivalue[x][y-1][z]).Domain4));

                                voxelList.get(ivalue[x][y-1][z]).isRemoved=true;
                                newIndex[x][y-1][z] = brickCounter;
                                brickList.get(brickCounter).isDomain3Replaced = voxelList.get(ivalue[x][y-1][z]).isDomain3Replaced;
                                brickList.get(brickCounter).isDomain4Replaced = voxelList.get(ivalue[x][y-1][z]).isDomain4Replaced;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x][y-1][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x][y-1][z]).isTailBrick || voxelList.get(i).isTailBrick;

                            }
                            else{
                                brickList.add(new VoxelToBrick(x,y,z,-1,-1,-1,voxelList.get(i).Domain1,
                                        voxelList.get(i).Domain2, null, null));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }

                            brickList.get(brickCounter).isDomain1Replaced = voxelList.get(i).isDomain1Replaced;
                            brickList.get(brickCounter).isDomain2Replaced = voxelList.get(i).isDomain2Replaced;

                        }
                    }

                    //Identifying full and half west-bricks and adding them to the final-coordinate-list.
                    if(voxelList.get(i).orientation=='w'){
                        if(voxelList.get(i).domain==34){
                            if(voxelList.get(ivalue[x+1][y][z]).isRemoved==false){
                                brickList.add(new VoxelToBrick(x+1,y,z,x,y,z,
                                        voxelList.get(ivalue[x+1][y][z]).Domain1,
                                        voxelList.get(ivalue[x+1][y][z]).Domain2,
                                        voxelList.get(i).Domain3, voxelList.get(i).Domain4));

                                voxelList.get(ivalue[x+1][y][z]).isRemoved=true;
                                newIndex[x+1][y][z] = brickCounter;
                                brickList.get(brickCounter).isDomain1Replaced = voxelList.get(ivalue[x+1][y][z]).isDomain1Replaced;
                                brickList.get(brickCounter).isDomain2Replaced = voxelList.get(ivalue[x+1][y][z]).isDomain2Replaced;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x+1][y][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x+1][y][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        voxelList.get(i).Domain3, voxelList.get(i).Domain4));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                            brickList.get(brickCounter).isDomain3Replaced = voxelList.get(i).isDomain3Replaced;
                            brickList.get(brickCounter).isDomain4Replaced = voxelList.get(i).isDomain4Replaced;
                        }
                        else{
                            if(voxelList.get(ivalue[x-1][y][z]).isRemoved==false){
                                brickList.add(new VoxelToBrick(x,y,z,x-1,y,z,
                                        voxelList.get(i).Domain1,
                                        voxelList.get(i).Domain2,
                                        voxelList.get(ivalue[x-1][y][z]).Domain3,
                                        voxelList.get(ivalue[x-1][y][z]).Domain4));

                                voxelList.get(ivalue[x-1][y][z]).isRemoved=true;
                                newIndex[x-1][y][z] = brickCounter;
                                brickList.get(brickCounter).isDomain3Replaced = voxelList.get(ivalue[x-1][y][z]).isDomain3Replaced;
                                brickList.get(brickCounter).isDomain4Replaced = voxelList.get(ivalue[x-1][y][z]).isDomain4Replaced;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x-1][y][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x-1][y][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(x,y,z,-1,-1,-1,voxelList.get(i).Domain1,
                                        voxelList.get(i).Domain2, null, null));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                            brickList.get(brickCounter).isDomain1Replaced = voxelList.get(i).isDomain1Replaced;
                            brickList.get(brickCounter).isDomain2Replaced = voxelList.get(i).isDomain2Replaced;
                        }
                    }

                    //Identifying full and half south-bricks and adding them to the final-coordinate-list.
                    if(voxelList.get(i).orientation=='s'){
                        if(voxelList.get(i).domain==12){
                            if(voxelList.get(ivalue[x][y+1][z]).isRemoved==false){
                                brickList.add(new VoxelToBrick(x,y,z,x,y+1,z,
                                        voxelList.get(i).Domain1, voxelList.get(i).Domain2,
                                        voxelList.get(ivalue[x][y+1][z]).Domain3,
                                        voxelList.get(ivalue[x][y+1][z]).Domain4
                                ));

                                voxelList.get(ivalue[x][y+1][z]).isRemoved=true;
                                newIndex[x][y+1][z] = brickCounter;
                                brickList.get(brickCounter).isDomain3Replaced = voxelList.get(ivalue[x][y+1][z]).isDomain3Replaced;
                                brickList.get(brickCounter).isDomain4Replaced = voxelList.get(ivalue[x][y+1][z]).isDomain4Replaced;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x][y+1][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x][y+1][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(x,y,z,-1,-1,-1,null, null,
                                        voxelList.get(i).Domain1, voxelList.get(i).Domain2));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                            brickList.get(brickCounter).isDomain1Replaced = voxelList.get(i).isDomain1Replaced;
                            brickList.get(brickCounter).isDomain2Replaced = voxelList.get(i).isDomain2Replaced;
                        }
                        else{
                            if(voxelList.get(ivalue[x][y-1][z]).isRemoved==false){
                                brickList.add(new VoxelToBrick(x,y-1,z,x,y,z,
                                        voxelList.get(ivalue[x][y-1][z]).Domain1,
                                        voxelList.get(ivalue[x][y-1][z]).Domain2,
                                        voxelList.get(i).Domain3,
                                        voxelList.get(i).Domain4
                                ));

                                voxelList.get(ivalue[x][y-1][z]).isRemoved=true;
                                newIndex[x][y-1][z] = brickCounter;
                                brickList.get(brickCounter).isDomain1Replaced = voxelList.get(ivalue[x][y-1][z]).isDomain1Replaced;
                                brickList.get(brickCounter).isDomain2Replaced = voxelList.get(ivalue[x][y-1][z]).isDomain2Replaced;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x][y-1][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x][y-1][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z, null, null,voxelList.get(i).Domain3,
                                        voxelList.get(i).Domain4));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                            brickList.get(brickCounter).isDomain3Replaced = voxelList.get(i).isDomain3Replaced;
                            brickList.get(brickCounter).isDomain4Replaced = voxelList.get(i).isDomain4Replaced;
                        }
                    }
                    //Identifying full and half east-bricks and adding them to the final-coordinate-list.
                    if(voxelList.get(i).orientation=='e'){
                        if(voxelList.get(i).domain==12){
                            if(voxelList.get(ivalue[x+1][y][z]).isRemoved==false){
                                brickList.add(new VoxelToBrick(x,y,z,x+1,y,z,
                                        voxelList.get(i).Domain1, voxelList.get(i).Domain2,
                                        voxelList.get(ivalue[x+1][y][z]).Domain3,
                                        voxelList.get(ivalue[x+1][y][z]).Domain4
                                ));

                                voxelList.get(ivalue[x+1][y][z]).isRemoved=true;
                                newIndex[x+1][y][z] = brickCounter;
                                brickList.get(brickCounter).isDomain3Replaced = voxelList.get(ivalue[x+1][y][z]).isDomain3Replaced;
                                brickList.get(brickCounter).isDomain4Replaced = voxelList.get(ivalue[x+1][y][z]).isDomain4Replaced;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x+1][y][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x+1][y][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(x,y,z,-1,-1,-1,null, null,
                                        voxelList.get(i).Domain1, voxelList.get(i).Domain2));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                            brickList.get(brickCounter).isDomain1Replaced = voxelList.get(i).isDomain1Replaced;
                            brickList.get(brickCounter).isDomain2Replaced = voxelList.get(i).isDomain2Replaced;

                        }
                        else{
                            if(voxelList.get(ivalue[x-1][y][z]).isRemoved==false){
                                brickList.add(new VoxelToBrick(x-1,y,z,x,y,z,
                                        voxelList.get(ivalue[x-1][y][z]).Domain1,
                                        voxelList.get(ivalue[x-1][y][z]).Domain2,
                                        voxelList.get(i).Domain3,
                                        voxelList.get(i).Domain4
                                ));

                                voxelList.get(ivalue[x-1][y][z]).isRemoved=true;
                                newIndex[x-1][y][z] = brickCounter;
                                brickList.get(brickCounter).isDomain1Replaced = voxelList.get(ivalue[x-1][y][z]).isDomain1Replaced;
                                brickList.get(brickCounter).isDomain2Replaced = voxelList.get(ivalue[x-1][y][z]).isDomain2Replaced;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x-1][y][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x-1][y][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,voxelList.get(i).Domain3,
                                        voxelList.get(i).Domain4));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                            brickList.get(brickCounter).isDomain3Replaced = voxelList.get(i).isDomain3Replaced;
                            brickList.get(brickCounter).isDomain4Replaced = voxelList.get(i).isDomain4Replaced;

                        }
                    }
                }
                //MainFrame.printLog("Added in set brick list: " + x + " " + y + " " + z + " head : " + brickList.get(brickCounter).isHeadProtector + " tail : " + brickList.get(brickCounter).isTailProtector, Color.WHITE);
                //Remove the individual voxel after combining it as a full brick in the final-coordinate-list.
                voxelList.get(i).isRemoved=true;
                newIndex[x][y][z] = brickCounter;
                brickCounter++;
            }

        }
    }

    //STEP 12: Assign helix number to each voxel
    public void addHelixNumber(){
        int x2, x3, y2, y3;

        for (int i = 0; i<brickList.size(); i++) {
            x2= brickList.get(i).x2;
            x3= brickList.get(i).x3;
            y2= brickList.get(i).y2;
            y3= brickList.get(i).y3;
            if (x2!=-1) {
                if(y2==0) {
                    brickList.get(i).helix1 = x2;
                    brickList.get(i).helix2 = x2;
                }
                else if (y2%2==1) {
                    brickList.get(i).helix1 = (y2 + 1) * MainFrame.width - (x2 + 1);
                    brickList.get(i).helix2 = brickList.get(i).helix1;

                }
                else {
                    brickList.get(i).helix1 = MainFrame.width * y2 + x2;
                    brickList.get(i).helix2 = brickList.get(i).helix1;
                }
            }
            if (x3!=-1){
                if(y3==0) {
                    brickList.get(i).helix3 = x3;
                    brickList.get(i).helix4 = brickList.get(i).helix3;
                }
                else if (y3%2==1) {
                    brickList.get(i).helix3 = (y3 + 1) * MainFrame.width - (x3 + 1);
                    brickList.get(i).helix4 = brickList.get(i).helix3;
                }
                else {
                    brickList.get(i).helix3 = MainFrame.width * y3 + x3;
                    brickList.get(i).helix4 = brickList.get(i).helix3;
                }
            }
        }
    }

    //STEP 13: Map each coordinate to DNA sequence fetched from the imported file
    public void mapSequencesFromList(){
        int index = -1;
        int h1, h2, h3, h4, z1, z2, z3, z4;
        boolean domain1Replacement, domain2Replacement, domain3Replacement, domain4Replacement;

        for(int i = 0; i<brickList.size(); i++){
            index = -1;
            //identifying half-bricks having domains 34
            if(brickList.get(i).x2==-1){
                h3 = brickList.get(i).helix3;
                h4 = h3;
                z3 = brickList.get(i).z3;
                z4 = z3+1;

                for(int j = 0; j<halfBrickSequencesList.size(); j++){
                    if (halfBrickSequencesList.get(j).helix1 == h3&&
                            halfBrickSequencesList.get(j).zCoordinate1 == z3&&
                            halfBrickSequencesList.get(j).zCoordinate2 == z4&&
                            halfBrickSequencesList.get(j).helix2 == h3) {
                        index = j;
                    }
                }
                if(index==-1) {

                    MainFrame.printLog("error: could not map 16nt coordinates [" + h3 + ", " + z3 + ", " + h4 + ", " + z4 + "] from imported file ", Color.pink);
                    brickList.get(i).isMapped = false;

                }
                else {
                    brickList.get(i).Domain3 = halfBrickSequencesList.get(index).domain1;
                    brickList.get(i).Domain4 = halfBrickSequencesList.get(index).domain2;
                    brickList.get(i).strandNumber = halfBrickSequencesList.get(index).strandNumber;
                    brickList.get(i).plateNumber = halfBrickSequencesList.get(index).plateNumber;
                    brickList.get(i).position = halfBrickSequencesList.get(index).position;
                    //System.out.println("*************found for "+-1+" "+-1+" "+-1+" "+-1+" "+h3+" "+z3+" "+h4+" "+z4+"?"+index);
                }
            }
            //identifying half bricks having domains 1 and 2
            else if(brickList.get(i).x3==-1){
                h1 = brickList.get(i).helix1;
                h2 = h1;
                z1 = brickList.get(i).z1;
                z2 = brickList.get(i).z2;

                for(int j = 0; j<halfBrickSequencesList.size(); j++){
                    if (halfBrickSequencesList.get(j).helix2 == h1&&
                            halfBrickSequencesList.get(j).zCoordinate1 == z2&&
                            halfBrickSequencesList.get(j).zCoordinate2 == z1&&
                            halfBrickSequencesList.get(j).helix1 == h2) {
                        index = j;
                    }
                }
                if(index==-1) {
                    MainFrame.printLog("error: could not map 16nt coordinates [" + h1 + ", " + z1 + ", " + h2 + ", " + z2 + "] from imported file", Color.pink);
                    brickList.get(i).isMapped = false;


                }
                else{
                    String d1 = new StringBuilder(halfBrickSequencesList.get(index).domain1).toString();//need to reverse and swap??
                    String d2 = new StringBuilder(halfBrickSequencesList.get(index).domain2).toString();//need to reverse and swap??
                    brickList.get(i).Domain1 = d1;
                    brickList.get(i).Domain2 = d2;
                    brickList.get(i).strandNumber = halfBrickSequencesList.get(index).strandNumber;
                    brickList.get(i).plateNumber = halfBrickSequencesList.get(index).plateNumber;
                    brickList.get(i).position = halfBrickSequencesList.get(index).position;
                    //System.out.println("*************found for "+h1+" "+z1+" "+h2+" "+z2+" "+-1+" "+-1+" "+-1+" "+-1+"?"+index);
                }
            }
            //identifying full bricks
            else if((brickList.get(i).x2!=-1)&&(brickList.get(i).x3!=-1)){
                h1 = brickList.get(i).helix1;
                h2 = h1;
                h3 = brickList.get(i).helix3;
                h4 = h3;
                z1 = brickList.get(i).z1;
                z2 = z1 - 1;
                z4 = brickList.get(i).z4;
                z3 = z4 - 1;
                domain1Replacement = brickList.get(i).isDomain1Replaced ;
                domain2Replacement = brickList.get(i).isDomain2Replaced ;
                domain3Replacement = brickList.get(i).isDomain3Replaced ;
                domain4Replacement = brickList.get(i).isDomain4Replaced ;

                for(int j = 0; j<fullBrickSequencesList.size(); j++){
                    if (fullBrickSequencesList.get(j).helix1 == h1 &&
                            fullBrickSequencesList.get(j).zCoordinate1 == z1 &&
                            fullBrickSequencesList.get(j).zCoordinate2 == z2 &&
                            fullBrickSequencesList.get(j).helix3 == h3 &&
                            fullBrickSequencesList.get(j).helix2 == h2 &&
                            fullBrickSequencesList.get(j).helix4 == h4 &&
                            fullBrickSequencesList.get(j).zCoordinate3 == z3 &&
                            fullBrickSequencesList.get(j).zCoordinate4 == z4 &&
                            domain1Replacement == fullBrickSequencesList.get(j).isDomain1Replaced &&
                            domain2Replacement == fullBrickSequencesList.get(j).isDomain2Replaced &&
                            domain3Replacement == fullBrickSequencesList.get(j).isDomain3Replaced &&
                            domain4Replacement == fullBrickSequencesList.get(j).isDomain4Replaced ) {
                        index = j;
                    }
                }
                if(index!=-1){
                    brickList.get(i).Domain1 = fullBrickSequencesList.get(index).domain1;
                    brickList.get(i).Domain2 = fullBrickSequencesList.get(index).domain2;
                    brickList.get(i).Domain3 = fullBrickSequencesList.get(index).domain3;
                    brickList.get(i).Domain4 = fullBrickSequencesList.get(index).domain4;
                    brickList.get(i).strandNumber = fullBrickSequencesList.get(index).strandNumber;
                    brickList.get(i).plateNumber = fullBrickSequencesList.get(index).plateNumber;
                    brickList.get(i).position = fullBrickSequencesList.get(index).position;

                    /*System.out.println("fullbrick data added "+h1+" "+z1+" "+h2+" "+z2+" "+h3+" "+z3+" "+h4+" "+z4+" "+
                            brickList.get(i).Domain1+" "+brickList.get(i).Domain2
                            +" "+
                            brickList.get(i).Domain3+" "+brickList.get(i).Domain4);*/
                }
                else if(index==-1) {
                    MainFrame.printLog("error: could not map 32nt coordinates [" + h1 + ", " + z1 + ", " + h2 + ", " + z2 + ", " + h3 + ", " + z3 + ", " + h4 + ", " + z4 + "] from imported file", Color.pink);
                    brickList.get(i).isMapped = false;
                }

            }
        }
        for (int i = 0; i < brickList.size(); i++){
            if (MainFrame.isZCrystalEnabled&& brickList.get(i).isHeadProtector) {

                if (brickList.get(i).x3==-1){
                    brickList.get(i).Domain1 = CoordinatesSequenceMap.negateSeqRev(brickList.get(newIndex[brickList.get(i).x1][brickList.get(i).y1][brickList.get(i).z1]).Domain3);
                    brickList.get(i).Domain2 = poly1T;
                    brickList.get(i).strandNumber = -1;
                    brickList.get(i).plateNumber = -1;
                    brickList.get(i).position = -1;
                    brickList.get(i).isMapped = true;
                }

                if (brickList.get(i).x1==-1) {
                    brickList.get(i).Domain3 = poly1T;
                    brickList.get(i).Domain4 = CoordinatesSequenceMap.negateSeqRev(brickList.get(newIndex[brickList.get(i).x4][brickList.get(i).y4][brickList.get(i).z4]).Domain2);
                    brickList.get(i).strandNumber = -1;
                    brickList.get(i).plateNumber = -1;
                    brickList.get(i).position = -1;
                    brickList.get(i).isMapped = true;
                }
            }
        }
    }

    //STEP 14: If set boundary bricks option is enabled, combine bricks to form 48-nt boundary bricks
    // and re-map the coordinates to DNA sequence fetched from the imported file
    public void attachBoundaryBricks(){
        int x, y, z1, z2, z3, z4, z5, z6, h1, h2, h3, h4, h5, h6;
        int fullBrickIndex;
        int domains;
        String d1,d2;
        boundaryBrickList = brickList;
        if(MainFrame.isMinXBoundaryPlaneEnabled || MainFrame.isMaxXBoundaryPlaneEnabled ||
                MainFrame.isMinYBoundaryPlaneEnabled || MainFrame.isMaxYBoundaryPlaneEnabled ) {

            for (int i = 0; i < boundaryBrickList.size(); i++) {
                if (boundaryBrickList.get(i).isMapped == true && boundaryBrickList.get(i).isHeadProtector == false &&
                        (boundaryBrickList.get(i).isLeftmostHalfBrick ||
                                boundaryBrickList.get(i).isBottommostHalfBrick ||
                                boundaryBrickList.get(i).isTopmostHalfBrick ||
                                boundaryBrickList.get(i).isRightmostHalfBrick)) {

                    if (boundaryBrickList.get(i).x3 == -1) {
                        x = boundaryBrickList.get(i).x2;
                        y = boundaryBrickList.get(i).y2;
                        z6 = boundaryBrickList.get(i).z1;
                        z5 = boundaryBrickList.get(i).z2;
                        d1 = new String(boundaryBrickList.get(i).Domain1);
                        d2 = new String(boundaryBrickList.get(i).Domain2);
                        h5 = boundaryBrickList.get(i).helix1;
                        h6 = h5;
                        domains = 12;
                    } else {
                        x = boundaryBrickList.get(i).x3;
                        y = boundaryBrickList.get(i).y3;
                        z5 = boundaryBrickList.get(i).z3;
                        z6 = boundaryBrickList.get(i).z4;
                        d1 = new String(boundaryBrickList.get(i).Domain3);
                        d2 = new String(boundaryBrickList.get(i).Domain4);
                        h5 = boundaryBrickList.get(i).helix3;
                        h6 = h5;
                        domains = 34;

                    }

                    if (((MainFrame.isMinXBoundaryPlaneEnabled && boundaryBrickList.get(i).isLeftmostHalfBrick) ||
                            (MainFrame.isMaxXBoundaryPlaneEnabled && boundaryBrickList.get(i).isRightmostHalfBrick)||
                            (MainFrame.isMinYBoundaryPlaneEnabled && boundaryBrickList.get(i).isTopmostHalfBrick) ||
                            (MainFrame.isMaxYBoundaryPlaneEnabled && boundaryBrickList.get(i).isBottommostHalfBrick)) &&
                            z5 > 1) {

                        fullBrickIndex = newIndex[x][y][z5 - 2];

                        if (fullBrickIndex != -1 && boundaryBrickList.get(fullBrickIndex).isDomain1Replaced == false &&
                                boundaryBrickList.get(fullBrickIndex).isDomain2Replaced == false &&
                                boundaryBrickList.get(fullBrickIndex).isDomain3Replaced == false &&
                                boundaryBrickList.get(fullBrickIndex).isDomain4Replaced == false ) {
                            h1 = boundaryBrickList.get(fullBrickIndex).helix1;
                            h2 = h1;
                            h3 = boundaryBrickList.get(fullBrickIndex).helix3;
                            h4 = h3;
                            z1 = boundaryBrickList.get(fullBrickIndex).z1;
                            z2 = boundaryBrickList.get(fullBrickIndex).z2;
                            z3 = boundaryBrickList.get(fullBrickIndex).z3;
                            z4 = boundaryBrickList.get(fullBrickIndex).z4;

                            int index = -1;

                            for (int j = 0; j < boundaryBrickSequencesList.size(); j++) {
                                if (domains == 34 && boundaryBrickSequencesList.get(j).helix1 == h1 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                        boundaryBrickSequencesList.get(j).helix3 == h3 &&
                                        boundaryBrickSequencesList.get(j).helix2 == h2 &&
                                        boundaryBrickSequencesList.get(j).helix4 == h4 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                        boundaryBrickSequencesList.get(j).helix5 == h5 &&
                                        boundaryBrickSequencesList.get(j).helix6 == h6 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                    index = j;
                                }
                                else if (domains == 12 && boundaryBrickSequencesList.get(j).helix1 == h6 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate1 == z6 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate2 == z5 &&
                                        boundaryBrickSequencesList.get(j).helix3 == h1 &&
                                        boundaryBrickSequencesList.get(j).helix2 == h5 &&
                                        boundaryBrickSequencesList.get(j).helix4 == h2 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate3 == z1 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate4 == z2 &&
                                        boundaryBrickSequencesList.get(j).helix5 == h3 &&
                                        boundaryBrickSequencesList.get(j).helix6 == h4 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate5 == z3 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate6 == z4) {
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                if (domains == 34)
                                    boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z5, d1, d2, h5, h6, domains);
                                else
                                    boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z5, d2, d1, h5, h6, domains);

                                boundaryBrickList.get(fullBrickIndex).strandNumber = boundaryBrickSequencesList.get(index).strandNumber;
                                boundaryBrickList.get(fullBrickIndex).plateNumber = boundaryBrickSequencesList.get(index).plateNumber;
                                boundaryBrickList.get(fullBrickIndex).position = boundaryBrickSequencesList.get(index).position;
                                boundaryBrickList.get(fullBrickIndex).isBoundary = true;
                                boundaryBrickList.get(fullBrickIndex).isHeadProtector = boundaryBrickList.get(i).isHeadProtector;
                                boundaryBrickList.get(fullBrickIndex).isTailProtector = boundaryBrickList.get(i).isTailProtector;
                                boundaryBrickList.get(i).isMapped = false;
                                boundaryBrickList.get(i).isRemoved = true;
                                newIndex[x][y][z5] = fullBrickIndex;
                                newIndex[x][y][z6] = fullBrickIndex;

                            }
                            else if (index == -1) {
                                MainFrame.printLog("error: could not find 48nt coordinates [" + h1 + ", " + z1 + ", " + h2 + ", " + z2 + ", " + h3 + ", " + z3 + ", " + h4 + ", " + z4 + ", " +
                                        +h5 + ", " + z5 + ", " + h6 + ", " + z6 + "] in imported file ", Color.pink);
                                if(MainFrame.isZCrystalEnabled && boundaryBrickList.get(fullBrickIndex).isHeadProtector){
                                    final JFrame  replaceSequenceFrame = new JFrame("Sequence not found");
                                     replaceSequenceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                     replaceSequenceFrame.setBackground(Color.DARK_GRAY);
                                     replaceSequenceFrame.setIconImage(MainFrame.imag);

                                    JPanel  replaceSequencePanel = new JPanel();
                                     replaceSequencePanel.setBackground(Color.DARK_GRAY);
                                     replaceSequencePanel.setLayout(new GridBagLayout());
                                    GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
                                    dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);

                                    final JLabel  replaceSequenceLabel = new JLabel("<html><style>h4{color:white;}</style><h4> error: could not find 48nt sequence at coordinates [ " + h1 +  ",  " + z1 +  ",  " + h2 +  ",  " + z2 +   ",   " + h3 +   ",   " + z3 +   ",   " + h4 +   ",   " + z4 +   ",   " +
                                            h5 +   ",   " + z5 +   ",   " + h6 +   ",   " + z6 +   "] in imported file. Would you like to form a new boundary brick in its position? </h4></html>");
                                    JButton yesButton = new JButton("YES");
                                    yesButton.setBackground(Color.DARK_GRAY);
                                    JButton noButton = new JButton("NO");
                                    noButton.setBackground(Color.DARK_GRAY);

                                    dimensionFrameGridBagConstraints.gridx = 0;
                                    dimensionFrameGridBagConstraints.gridy = 0;
                                     replaceSequencePanel.add( replaceSequenceLabel, dimensionFrameGridBagConstraints);
                                    dimensionFrameGridBagConstraints.gridx = 0;
                                    dimensionFrameGridBagConstraints.gridy = 1;
                                     replaceSequencePanel.add(yesButton, dimensionFrameGridBagConstraints);
                                    dimensionFrameGridBagConstraints.gridx = 1;
                                    dimensionFrameGridBagConstraints.gridy = 1;
                                    replaceSequencePanel.add(noButton, dimensionFrameGridBagConstraints);
                                     replaceSequenceFrame.add( replaceSequencePanel, BorderLayout.CENTER);
                                     replaceSequenceFrame.pack();
                                     replaceSequenceFrame.setVisible(true);
                                     replaceSequenceFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

                                    final int finalDomains = domains;
                                    final int finalFullBrickIndex = fullBrickIndex;
                                    final int finalI = i;
                                    final int finalX = x;
                                    final int finalY = y;
                                    final int finalZ5 = z5;
                                    final String finalD1 = d1;
                                    final String finalD2 = d2;
                                    final int finalH5 = h5;
                                    final int finalH6 = h6;
                                    final int finalZ6 = z6;
                                    yesButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            if (finalDomains == 34)
                                                boundaryBrickList.get(finalFullBrickIndex).addDomains(finalX, finalY, finalZ5, finalD1, finalD2, finalH5, finalH6, finalDomains);
                                            else
                                                boundaryBrickList.get(finalFullBrickIndex).addDomains(finalX, finalY, finalZ5, finalD2, finalD1, finalH5, finalH6, finalDomains);

                                            boundaryBrickList.get(finalFullBrickIndex).isBoundary = true;
                                            boundaryBrickList.get(finalFullBrickIndex).isHeadProtector = boundaryBrickList.get(finalI).isHeadProtector;
                                            boundaryBrickList.get(finalFullBrickIndex).isTailProtector = boundaryBrickList.get(finalI).isTailProtector;
                                            boundaryBrickList.get(finalI).isMapped = false;
                                            boundaryBrickList.get(finalI).isRemoved = true;
                                            newIndex[finalX][finalY][finalZ5] = finalFullBrickIndex;
                                            newIndex[finalX][finalY][finalZ6] = finalFullBrickIndex;
                                             replaceSequenceFrame.dispose();
                                        }
                                    });
                                    noButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            replaceSequenceFrame.dispose();
                                        }
                                    });

                                }
                                else {
                                    MainFrame.printLog("error: could not map 48nt coordinates [" + h1 + ", " + z1 + ", " + h2 + ", " + z2 + ", " + h3 + ", " + z3 + ", " + h4 + ", " + z4 + ", " +
                                            +h5 + ", " + z5 + ", " + h6 + ", " + z6 + "] from imported file ", Color.pink);
                                    boundaryBrickList.get(i).isMapped = true;
                                    boundaryBrickList.get(i).isRemoved = false;
                                }
                            }
                            boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                            boundaryBrickList.get(i).isRightmostHalfBrick = false;
                            boundaryBrickList.get(i).isBottommostHalfBrick = false;
                            boundaryBrickList.get(i).isTopmostHalfBrick = false;
                            //System.out.println("BoundaryBrick for min x at" + x + " " + y + " " + z5);
                        }
                    }
                }
            }
        }
        if (MainFrame.isCavityEnabled) {
            for (int i = 0; i < boundaryBrickList.size(); i++) {
                if (boundaryBrickList.get(i).isMapped == true &&
                        boundaryBrickList.get(i).isAdjacentToCavity) {

                    if (boundaryBrickList.get(i).x3 == -1) {
                        x = boundaryBrickList.get(i).x2;
                        y = boundaryBrickList.get(i).y2;
                        z6 = boundaryBrickList.get(i).z1;
                        z5 = boundaryBrickList.get(i).z2;
                        d1 = new String(boundaryBrickList.get(i).Domain1);
                        d2 = new String(boundaryBrickList.get(i).Domain2);
                        h5 = boundaryBrickList.get(i).helix1;
                        h6 = h5;
                        domains = 12;
                    } else {
                        x = boundaryBrickList.get(i).x3;
                        y = boundaryBrickList.get(i).y3;
                        z5 = boundaryBrickList.get(i).z3;
                        z6 = boundaryBrickList.get(i).z4;
                        d1 = new String(boundaryBrickList.get(i).Domain3);
                        d2 = new String(boundaryBrickList.get(i).Domain4);
                        h5 = boundaryBrickList.get(i).helix3;
                        h6 = h5;
                        domains = 34;

                    }

                    if (z5 > 1 && z5 != MainFrame.depth) {//half bricks missing in Yonggang Ke's file

                        fullBrickIndex = newIndex[x][y][z5 - 2];
                        if (fullBrickIndex != -1 && boundaryBrickList.get(fullBrickIndex).isBoundary == false &&
                                boundaryBrickList.get(fullBrickIndex).isDomain1Replaced == false &&
                                boundaryBrickList.get(fullBrickIndex).isDomain2Replaced == false &&
                                boundaryBrickList.get(fullBrickIndex).isDomain3Replaced == false &&
                                boundaryBrickList.get(fullBrickIndex).isDomain4Replaced == false ) {
                            h1 = boundaryBrickList.get(fullBrickIndex).helix1;
                            h2 = h1;
                            h3 = boundaryBrickList.get(fullBrickIndex).helix3;
                            h4 = h3;
                            z1 = boundaryBrickList.get(fullBrickIndex).z1;
                            z2 = boundaryBrickList.get(fullBrickIndex).z2;
                            z3 = boundaryBrickList.get(fullBrickIndex).z3;
                            z4 = boundaryBrickList.get(fullBrickIndex).z4;

                            int index = -1;

                            for (int j = 0; j < boundaryBrickSequencesList.size(); j++) {
                                if (domains == 34 && boundaryBrickSequencesList.get(j).helix1 == h1 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                        boundaryBrickSequencesList.get(j).helix3 == h3 &&
                                        boundaryBrickSequencesList.get(j).helix2 == h2 &&
                                        boundaryBrickSequencesList.get(j).helix4 == h4 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                        boundaryBrickSequencesList.get(j).helix5 == h5 &&
                                        boundaryBrickSequencesList.get(j).helix6 == h6 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                    index = j;
                                }
                                else if (domains == 12 && boundaryBrickSequencesList.get(j).helix1 == h6 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate1 == z6 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate2 == z5 &&
                                        boundaryBrickSequencesList.get(j).helix3 == h1 &&
                                        boundaryBrickSequencesList.get(j).helix2 == h5 &&
                                        boundaryBrickSequencesList.get(j).helix4 == h2 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate3 == z1 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate4 == z2 &&
                                        boundaryBrickSequencesList.get(j).helix5 == h3 &&
                                        boundaryBrickSequencesList.get(j).helix6 == h4 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate5 == z3 &&
                                        boundaryBrickSequencesList.get(j).zCoordinate6 == z4) {
                                    index = j;
                                }
                            }
                            if (index != -1) {
                                if (domains == 34)
                                    boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z5, d1, d2, h5, h6, domains);
                                else
                                    boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z5, d2, d1, h5, h6, domains);

                                boundaryBrickList.get(fullBrickIndex).strandNumber = boundaryBrickSequencesList.get(index).strandNumber;
                                boundaryBrickList.get(fullBrickIndex).plateNumber = boundaryBrickSequencesList.get(index).plateNumber;
                                boundaryBrickList.get(fullBrickIndex).position = boundaryBrickSequencesList.get(index).position;
                                boundaryBrickList.get(fullBrickIndex).isBoundary = true;
                                boundaryBrickList.get(fullBrickIndex).isHeadProtector = boundaryBrickList.get(i).isHeadProtector;
                                boundaryBrickList.get(fullBrickIndex).isTailProtector = boundaryBrickList.get(i).isTailProtector;
                                boundaryBrickList.get(i).isMapped = false;
                                boundaryBrickList.get(i).isRemoved = true;
                                newIndex[x][y][z5] = fullBrickIndex;
                                newIndex[x][y][z6] = fullBrickIndex;


                            } else if (index == -1) {

                                if(MainFrame.isZCrystalEnabled && boundaryBrickList.get(fullBrickIndex).isHeadProtector){
                                    if (domains == 34)
                                        boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z5, d1, d2, h5, h6, domains);
                                    else
                                        boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z5, d2, d1, h5, h6, domains);

                                    boundaryBrickList.get(fullBrickIndex).isBoundary = true;
                                    boundaryBrickList.get(fullBrickIndex).isHeadProtector = boundaryBrickList.get(i).isHeadProtector;
                                    boundaryBrickList.get(fullBrickIndex).isTailProtector = boundaryBrickList.get(i).isTailProtector;
                                    boundaryBrickList.get(i).isMapped = false;
                                    boundaryBrickList.get(i).isRemoved = true;
                                    newIndex[x][y][z5] = fullBrickIndex;
                                    newIndex[x][y][z6] = fullBrickIndex;
                                }
                                else {
                                    MainFrame.printLog("error: could not map 48nt coordinates around cavity[" + h1 + ", " + z1 + ", " + h2 + ", " + z2 + ", " + h3 + ", " + z3 + ", " + h4 + ", " + z4 + ", " +
                                            +h5 + ", " + z5 + ", " + h6 + ", " + z6 + "] from imported file ", Color.pink);
                                    boundaryBrickList.get(i).isMapped = true;
                                    boundaryBrickList.get(i).isRemoved = false;
                                }
                            }
                            boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                            boundaryBrickList.get(i).isRightmostHalfBrick = false;
                            boundaryBrickList.get(i).isBottommostHalfBrick = false;
                            boundaryBrickList.get(i).isTopmostHalfBrick = false;
                            //System.out.println("BoundaryBrick for min x at" + x + " " + y + " " + z5);
                        }
                    }
                }
            }
        }
        int s = boundaryBrickList.size();
        for(int i=0;i<s;i++){
            if(boundaryBrickList.get(i).isRemoved == true){
                boundaryBrickList.remove(i);
                s--;
            }
        }

    }

    public void createCrystal(){
        int x, y, z, tailZ, headZ;
        int domain2Index = -1, domain3Index = -1;
        boolean foundDomains24 =  false;
        boolean foundDomains13 =  false;
        runRandomSequences();

        if (MainFrame.isZCrystalEnabled) {
            for (int i = 0; i < brickList.size(); i++) {
                if (brickList.get(i).isTailProtector && brickList.get(i).isMapped) {
                    if (brickList.get(i).Domain1 != null) {
                        x = brickList.get(i).x1;
                        y = brickList.get(i).y1;
                        tailZ = brickList.get(i).z2; //to see compatibility of bricks for crystal
                        headZ = 0;
                        while ((newIndex[x][y][headZ] == -1 || brickList.get(newIndex[x][y][headZ]).isMapped == false) && headZ < brickList.get(i).z1) {
                            headZ++;
                        }

                        if (brickList.get(newIndex[x][y][headZ]).isHeadProtector &&
                                ((headZ % 4 == 0 && tailZ % 4 == 3) || (headZ % 4 == 1 && tailZ % 4 == 0)
                                        || (headZ % 4 == 2 && tailZ % 4 == 1) || (headZ % 4 == 3 && tailZ % 4 == 2))) {

                            foundDomains13 = true;
                            domain3Index = newIndex[x][y][headZ];
                        }
                    }
                    if (brickList.get(i).Domain4 != null) {
                        x = brickList.get(i).x4;
                        y = brickList.get(i).y4;
                        tailZ = brickList.get(i).z3; //to see compatibility of bricks for crystal
                        headZ = 0;
                        while ((newIndex[x][y][headZ] == -1 || brickList.get(newIndex[x][y][headZ]).isMapped == false) && headZ < brickList.get(i).z4) {
                            headZ++;
                        }

                        if (brickList.get(newIndex[x][y][headZ]).isHeadProtector &&
                                ((headZ % 4 == 0 && tailZ % 4 == 3) || (headZ % 4 == 1 && tailZ % 4 == 0)
                                        || (headZ % 4 == 2 && tailZ % 4 == 1) || (headZ % 4 == 3 && tailZ % 4 == 2))) {

                            foundDomains24 = true;
                            domain2Index = newIndex[x][y][headZ];
                        }
                    }
                    if (foundDomains13) {
                        foundDomains13 = false;
                        if (brickList.get(i).Domain1.equals(poly1T)) {
                            do {
                                brickList.get(i).Domain1 = AssignRandom(1);
                                if (!CoordinatesSequenceMap.checkGCContent(brickList.get(i).Domain1, brickList.get(i).Domain3)) {
                                    sequencesList.get(i1).isUsed = false;
                                }
                            }
                            while (!CoordinatesSequenceMap.checkGCContent(brickList.get(i).Domain1, brickList.get(i).Domain3));
                            brickList.get(i).strandNumber = -1;
                            brickList.get(i).isDomain1Replaced = false; // it is no longer a poly 1T sequence
                        }
                        brickList.get(domain3Index).Domain3 = CoordinatesSequenceMap.negateSeqRev(brickList.get(i).Domain1);
                        brickList.get(domain3Index).strandNumber = -1;
                        brickList.get(domain3Index).isDomain3Replaced = false; // it is no longer a poly 1T sequence
                    }
                    if (foundDomains24) {
                        foundDomains24 = false;
                        if (brickList.get(i).Domain4.equals(poly1T)) {
                            do {
                                brickList.get(i).Domain4 = AssignRandom(4);
                                if (!CoordinatesSequenceMap.checkGCContent(brickList.get(i).Domain2, brickList.get(i).Domain4)) {
                                    sequencesList.get(i4).isUsed = false;
                                }
                            }
                            while (!CoordinatesSequenceMap.checkGCContent(brickList.get(i).Domain2, brickList.get(i).Domain4));
                            brickList.get(i).strandNumber = -1;
                            brickList.get(i).isDomain4Replaced = false; // it is no longer a poly 1T sequence
                        }
                        brickList.get(domain2Index).Domain2 = CoordinatesSequenceMap.negateSeqRev(brickList.get(i).Domain4);
                        brickList.get(domain2Index).strandNumber = -1;
                        brickList.get(domain2Index).isDomain2Replaced = false; // it is no longer a poly 1T sequence
                    }
                }
            }
        }

        if (MainFrame.isXCrystalEnabled){
            for(int i = 0; i < brickList.size(); i++){
                //checking for west half brick
                if (brickList.get(i).isLeftmostHalfBrick && brickList.get(i).z2 % 4 == 1){
                    x = brickList.get(i).x2 + 1; //start searching for rightmost half brick from x + 1
                    y = brickList.get(i).y2;
                    z = brickList.get(i).z2;
                    while ( x < MainFrame.width ){
                        if (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isRightmostHalfBrick)
                            break;
                        else if (newIndex[x][y][z] == -1 || (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isRightmostHalfBrick == false))
                            x++;
                    }
                    if (brickList.get(newIndex[x][y][z]).isRightmostHalfBrick) {
                        brickList.get(i).Domain3 = brickList.get(newIndex[x][y][z]).Domain3;
                        brickList.get(i).Domain4 = brickList.get(newIndex[x][y][z]).Domain4;
                        brickList.get(i).x3 = brickList.get(newIndex[x][y][z]).x3;
                        brickList.get(i).x4 = brickList.get(newIndex[x][y][z]).x4;
                        brickList.get(i).y3 = brickList.get(newIndex[x][y][z]).y3;
                        brickList.get(i).y4 = brickList.get(newIndex[x][y][z]).y4;
                        brickList.get(i).z3 = brickList.get(newIndex[x][y][z]).z3;
                        brickList.get(i).z4 = brickList.get(newIndex[x][y][z]).z4;
                        brickList.get(i).helix3 = brickList.get(newIndex[x][y][z]).helix3;
                        brickList.get(i).helix4 = brickList.get(newIndex[x][y][z]).helix4;

                        brickList.get(i).isLeftmostHalfBrick = false;
                        brickList.get(i).isTopmostHalfBrick = false;
                        brickList.get(i).isBottommostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isRightmostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isMapped = false;
                        brickList.get(i).strandNumber = -1;
                    }
                }

                //checking for east half brick
                if (brickList.get(i).isLeftmostHalfBrick && brickList.get(i).z3 % 4 == 3){
                    x = brickList.get(i).x3 + 1;
                    y = brickList.get(i).y3;
                    z = brickList.get(i).z3;
                    while ( x < MainFrame.width ){
                        if (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isRightmostHalfBrick)
                            break;
                        else if (newIndex[x][y][z] == -1 || (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isRightmostHalfBrick == false))
                            x++;
                    }
                    if (brickList.get(newIndex[x][y][z]).isRightmostHalfBrick) {
                        brickList.get(i).Domain2 = brickList.get(newIndex[x][y][z]).Domain2;
                        brickList.get(i).Domain1 = brickList.get(newIndex[x][y][z]).Domain1;
                        brickList.get(i).x1 = brickList.get(newIndex[x][y][z]).x1;
                        brickList.get(i).x2 = brickList.get(newIndex[x][y][z]).x2;
                        brickList.get(i).y1 = brickList.get(newIndex[x][y][z]).y1;
                        brickList.get(i).y2 = brickList.get(newIndex[x][y][z]).y2;
                        brickList.get(i).z1 = brickList.get(newIndex[x][y][z]).z1;
                        brickList.get(i).z2 = brickList.get(newIndex[x][y][z]).z2;
                        brickList.get(i).helix1 = brickList.get(newIndex[x][y][z]).helix1;
                        brickList.get(i).helix2 = brickList.get(newIndex[x][y][z]).helix2;

                        brickList.get(i).isLeftmostHalfBrick = false;
                        brickList.get(i).isTopmostHalfBrick = false;
                        brickList.get(i).isBottommostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isRightmostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isMapped = false;
                        brickList.get(i).strandNumber = -1;
                    }
                }
            }
        }

        if (MainFrame.isYCrystalEnabled){
            for(int i = 0; i < brickList.size(); i++){
                //checking for north half brick
                if (brickList.get(i).isTopmostHalfBrick && brickList.get(i).z2 % 4 == 0){
                    x = brickList.get(i).x2;
                    y = brickList.get(i).y2 + 1; //start searching for bottommost half brick from y + 1
                    z = brickList.get(i).z2;
                    while ( y < MainFrame.height){
                        if (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isBottommostHalfBrick)
                            break;
                        else if (newIndex[x][y][z] == -1 || (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isBottommostHalfBrick == false))
                            y++;
                    }
                    if (brickList.get(newIndex[x][y][z]).isBottommostHalfBrick) {
                        brickList.get(i).Domain3 = brickList.get(newIndex[x][y][z]).Domain3;
                        brickList.get(i).Domain4 = brickList.get(newIndex[x][y][z]).Domain4;
                        brickList.get(i).x3 = brickList.get(newIndex[x][y][z]).x3;
                        brickList.get(i).x4 = brickList.get(newIndex[x][y][z]).x4;
                        brickList.get(i).y3 = brickList.get(newIndex[x][y][z]).y3;
                        brickList.get(i).y4 = brickList.get(newIndex[x][y][z]).y4;
                        brickList.get(i).z3 = brickList.get(newIndex[x][y][z]).z3;
                        brickList.get(i).z4 = brickList.get(newIndex[x][y][z]).z4;
                        brickList.get(i).helix3 = brickList.get(newIndex[x][y][z]).helix3;
                        brickList.get(i).helix4 = brickList.get(newIndex[x][y][z]).helix4;

                        brickList.get(i).isLeftmostHalfBrick = false;
                        brickList.get(i).isTopmostHalfBrick = false;
                        brickList.get(i).isRightmostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isBottommostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isMapped = false;
                        brickList.get(i).strandNumber = -1;
                    }
                }

                //checking for south half brick
                if (brickList.get(i).isTopmostHalfBrick && brickList.get(i).z3 % 4 == 2){
                    x = brickList.get(i).x3;
                    y = brickList.get(i).y3 + 1;
                    z = brickList.get(i).z3;
                    while ( y < MainFrame.height){
                        if (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isBottommostHalfBrick)
                            break;
                        else if (newIndex[x][y][z] == -1 || (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isBottommostHalfBrick == false))
                            y++;
                    }
                    if (brickList.get(newIndex[x][y][z]).isBottommostHalfBrick) {
                        brickList.get(i).Domain2 = brickList.get(newIndex[x][y][z]).Domain2;
                        brickList.get(i).Domain1 = brickList.get(newIndex[x][y][z]).Domain1;
                        brickList.get(i).x1 = brickList.get(newIndex[x][y][z]).x1;
                        brickList.get(i).x2 = brickList.get(newIndex[x][y][z]).x2;
                        brickList.get(i).y1 = brickList.get(newIndex[x][y][z]).y1;
                        brickList.get(i).y2 = brickList.get(newIndex[x][y][z]).y2;
                        brickList.get(i).z1 = brickList.get(newIndex[x][y][z]).z1;
                        brickList.get(i).z2 = brickList.get(newIndex[x][y][z]).z2;
                        brickList.get(i).helix1 = brickList.get(newIndex[x][y][z]).helix1;
                        brickList.get(i).helix2 = brickList.get(newIndex[x][y][z]).helix2;

                        brickList.get(i).isLeftmostHalfBrick = false;
                        brickList.get(i).isTopmostHalfBrick = false;
                        brickList.get(i).isRightmostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isBottommostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isMapped = false;
                        brickList.get(i).strandNumber = -1;
                    }
                }
            }
        }
    }
    //STEP 1: fetch random sequences from SequencesFile.csv and save to the arraylist - randomSequencesList
    public static void runRandomSequences() {

        String csvFile = "SequencesFile.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                randomSequencesList.add(new DNASequence(line.toString()));
            }
            for(int i=0;i< randomSequencesList.size();i++){
                sequencesList.add(new DNASequence(changesize(randomSequencesList.get(i).getSequence())));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String changesize(String domainSeq) {
        String Seq = "";
        int stringLength = domainSeq.length();

        for (int i = 0; i <8; i++) {
            if (domainSeq.charAt(i) == 'A') {
                Seq += "A";
            } else if (domainSeq.charAt(i) == 'T') {
                Seq += "T";
            } else if (domainSeq.charAt(i) == 'G') {
                Seq += "G";
            } else if (domainSeq.charAt(i) == 'C') {
                Seq += "C";
            }
        }
        //System.out.println(""+Seq);
        return (Seq);
    }
    public static String AssignRandom(int domain){

        Random rand = new Random();
        int randomIndex;

        randomIndex= rand.nextInt(sequencesList.size());
        if(sequencesList.get(randomIndex).isUsed==true)
            while(sequencesList.get(randomIndex).isUsed==true){
                randomIndex= rand.nextInt(sequencesList.size());
            }

        //i1, i2, i3, i4 are used to keep track of the index for setting the isUsed parameter to false,
        // if the sequence does not satisfy the GC constraint.
        if(domain==1) {
            i1 = randomIndex;
        }
        else if(domain==2) {
            i2 = randomIndex;
        }
        else if(domain==3) {
            i3 = randomIndex;
        }
        else {
            i4 = randomIndex;
        }

        sequencesList.get(randomIndex).isUsed=true;
        return(sequencesList.get(randomIndex).getSequence());

    }
    public static boolean checkGCContent(String d1, String d2, String d3, String d4){

        int l1=d1.length();
        int l2=d2.length();
        int l3=d3.length();
        int l4=d4.length();

        int GCcount=0,i;
        double min, max;
        min = 0.4*32;
        max = 0.6*32;
        boolean isGCsatisfied = true;

        for (i=0; i<l1; i++) {
            if ((d1.charAt(i) == 'G')||(d1.charAt(i) == 'C'))
                GCcount+=1;
        }
        for (i=0; i<l2; i++) {
            if ((d2.charAt(i) == 'G')||(d2.charAt(i) == 'C'))
                GCcount+=1;
        }
        for (i=0; i<l3; i++) {
            if ((d3.charAt(i) == 'G')||(d3.charAt(i) == 'C'))
                GCcount+=1;
        }
        for (i=0; i<l4; i++) {
            if ((d4.charAt(i) == 'G')||(d4.charAt(i) == 'C'))
                GCcount+=1;
        }
        System.out.println("gc count:" + GCcount);

        if(!(GCcount<=max && GCcount>=min)){

            isGCsatisfied=false;

        }
        // System.out.println("GC Content:" + GCcount);
        return(isGCsatisfied);

    }
}
