import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by foram.joshi on 17.10.2014.
 */
public class IdentifyBoundaryAndProtectorBricks {

    public static ArrayList<XYZCoordinates> voxelList = new ArrayList<XYZCoordinates>();
    static ArrayList<VoxelToBrick> brickList=new ArrayList<VoxelToBrick>();
    static ArrayList<VoxelToBrick> boundaryBrickList=new ArrayList<VoxelToBrick>();
    //using a 3d array to store the position for each voxel in the arraylist coordinate-list
    static int[][][] ivalue = new int[20][20][20];
    int[][][] newIndex = new int[MainFrame.width][MainFrame.height][MainFrame.depth+2];
    int x,y,z,indexfront, indexback;
    String poly1T="TTTTTTTT";

    //constructor for the class
    public IdentifyBoundaryAndProtectorBricks(char c) {
        //clear all arraylists
        voxelList.clear();
        for (int k = 0; k <= MainFrame.depth; k++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int i = 0; i < MainFrame.width; i++)
                    ivalue[i][j][k] = -1;

        addCoordinates();
        if (c == 'b') {
            identifyBoundaryVoxels();
            assignOrientation();
            assignDomain();
            assignNullSequences();
            brickList.clear();
            convertToT();
            //removeTVoxels();
            //set all values in newIndex to -1
            for (int i = 0; i < MainFrame.width; i++)
                for (int j = 0; j < MainFrame.height; j++)
                    for (int k = 0; k <= MainFrame.depth; k++)
                        newIndex[i][j][k] = -1;
            setBrickList();
            addHelixNumber();
            attachBoundaryBricks();
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
                    MainFrame.printLog("TailBrick added at " + i + " " + j + " " + z, Color.white);
                }
            }
        //adding the protector bricks for the head and tail bricks
        int s = voxelList.size();
        //adding head brick
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
                        MainFrame.printLog("headBrick added at " + x + " " + y + " " + z , Color.white);
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
                else if (((voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 0) &&
                        (voxelList.get(i).y != MainFrame.height - 1)) ||
                        (voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 1) &&
                                (voxelList.get(i).y != MainFrame.height - 1)) {
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
                else if (((voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 1) &&
                        (voxelList.get(i).y != MainFrame.height - 1)) ||
                        (voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 0) &&
                                (voxelList.get(i).y != MainFrame.height - 1)) {
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
                else if (((voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 1) &&
                        (voxelList.get(i).x != MainFrame.width - 1)) ||
                        (voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 0) &&
                                (voxelList.get(i).x != MainFrame.width - 1)) {
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
                MainFrame.printLog("voxel removed at " + x + " " + y + " " + z , Color.pink);
                if(z > 0&& MainFrame.isMinZProtectorPlaneEnabled || z>1&&!MainFrame.isMinZProtectorPlaneEnabled){
                    indexback=ivalue[x][y][z-1];
                    if (voxelList.get(indexback).isRemoved == false && voxelList.get(indexback).isHeadBrick == false && voxelList.get(indexback).isTailBrick == false) {
                        if(MainFrame.isCavityEnabled) {
                            voxelList.get(indexback).isAdjacentToCavity = true;
                        }
                        else{
                            MainFrame.printLog("voxel at z -1 of removed voxel  at " + x + " " + y + " " + (z - 1) , Color.magenta);
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
                            MainFrame.printLog("voxel at z + 1 of removed voxel  at " + x + " " + y + " " + (z - 1) , Color.magenta);
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
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
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


    //STEP 14: If set boundary bricks option is enabled, combine bricks to form 48-nt boundary bricks
    // and re-map the coordinates to DNA sequence fetched from the imported file
    public void attachBoundaryBricks(){

        int fullBrickIndex;
        boundaryBrickList = brickList;

        for (int i = 0; i < MainFrame.width; i++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int k = 0; k <= MainFrame.depth; k++)
                if (newIndex[i][j][k] == -1){
                    MainFrame.printLog("new index value at " + i + " " + j + " " + k + " = " + newIndex[i][j][k], Color.pink);
                }

        for (int i = 0; i < boundaryBrickList.size(); i++) {
            if (boundaryBrickList.get(i).isLeftmostHalfBrick ||
                    boundaryBrickList.get(i).isBottommostHalfBrick ||
                    boundaryBrickList.get(i).isTopmostHalfBrick ||
                    boundaryBrickList.get(i).isRightmostHalfBrick) {
                if(boundaryBrickList.get(i).z2 != -1){
                    if((boundaryBrickList.get(i).z2 < 2 && MainFrame.isMinZProtectorPlaneEnabled) ||
                            (boundaryBrickList.get(i).z2 < 3 && !MainFrame.isMinZProtectorPlaneEnabled )){
                        boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                        boundaryBrickList.get(i).isBottommostHalfBrick = false;
                        boundaryBrickList.get(i).isTopmostHalfBrick = false;
                        boundaryBrickList.get(i).isRightmostHalfBrick = false;

                        //MainFrame.printLog(" 1 no half boundary brick at " + boundaryBrickList.get(i).x2 + " " + boundaryBrickList.get(i).y2 + " " + boundaryBrickList.get(i).z2, Color.magenta);
                    }
                    else {
                        fullBrickIndex = newIndex[boundaryBrickList.get(i).x2][boundaryBrickList.get(i).y2][boundaryBrickList.get(i).z2 - 2];
                        if (fullBrickIndex == -1)
                        /*|| boundaryBrickList.get(fullBrickIndex).isHalfBrick() ||
                                (boundaryBrickList.get(fullBrickIndex).isRemoved == false &&
                                        (boundaryBrickList.get(fullBrickIndex).isDomain1Replaced == true ||
                                                boundaryBrickList.get(fullBrickIndex).isDomain2Replaced == true ||
                                                boundaryBrickList.get(fullBrickIndex).isDomain3Replaced == true ||
                                                boundaryBrickList.get(fullBrickIndex).isDomain4Replaced == true)))*/ {
                            boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                            boundaryBrickList.get(i).isBottommostHalfBrick = false;
                            boundaryBrickList.get(i).isTopmostHalfBrick = false;
                            boundaryBrickList.get(i).isRightmostHalfBrick = false;
                            //MainFrame.printLog("2 no half boundary brick at " + boundaryBrickList.get(i).x2 + " " + boundaryBrickList.get(i).y2 + " " + boundaryBrickList.get(i).z2, Color.magenta);
                        }
                        else {
                            boundaryBrickList.get(fullBrickIndex).isBoundary = true; // so that cavity boundary do not join with 48nt bricks
                            //MainFrame.printLog("3 half boundary brick at " + boundaryBrickList.get(i).x2 + " " + boundaryBrickList.get(i).y2 + " " + boundaryBrickList.get(i).z2, Color.white);
                        }
                    }
                }

                if(boundaryBrickList.get(i).z3 != -1){
                    if((boundaryBrickList.get(i).z3 < 2 && MainFrame.isMinZProtectorPlaneEnabled) ||
                            (boundaryBrickList.get(i).z3 < 3 && !MainFrame.isMinZProtectorPlaneEnabled )){
                        boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                        boundaryBrickList.get(i).isBottommostHalfBrick = false;
                        boundaryBrickList.get(i).isTopmostHalfBrick = false;
                        boundaryBrickList.get(i).isRightmostHalfBrick = false;
                        //MainFrame.printLog("1 no half boundary brick at " + boundaryBrickList.get(i).x3 + " " + boundaryBrickList.get(i).y3 + " " + boundaryBrickList.get(i).z3, Color.magenta);
                    }
                    else {
                        fullBrickIndex = newIndex[boundaryBrickList.get(i).x3][boundaryBrickList.get(i).y3][boundaryBrickList.get(i).z3 - 2];
                        if (fullBrickIndex == -1)

                        /*|| boundaryBrickList.get(fullBrickIndex).isHalfBrick() ||
                                (boundaryBrickList.get(fullBrickIndex).isRemoved == false &&
                                        (boundaryBrickList.get(fullBrickIndex).isDomain1Replaced == true ||
                                                boundaryBrickList.get(fullBrickIndex).isDomain2Replaced == true ||
                                                boundaryBrickList.get(fullBrickIndex).isDomain3Replaced == true ||
                                                boundaryBrickList.get(fullBrickIndex).isDomain4Replaced == true)))
                                                */{
                            boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                            boundaryBrickList.get(i).isBottommostHalfBrick = false;
                            boundaryBrickList.get(i).isTopmostHalfBrick = false;
                            boundaryBrickList.get(i).isRightmostHalfBrick = false;
                            //MainFrame.printLog("2 no half boundary brick at " + boundaryBrickList.get(i).x3 + " " + boundaryBrickList.get(i).y3 + " " + boundaryBrickList.get(i).z3, Color.magenta);
                        }
                        else {
                            boundaryBrickList.get(fullBrickIndex).isBoundary = true; // so that cavity boundary do not join with 48nt bricks
                            //MainFrame.printLog("3 half boundary brick at " + boundaryBrickList.get(i).x3 + " " + boundaryBrickList.get(i).y3 + " " + boundaryBrickList.get(i).z3, Color.white);
                        }
                    }
                }


            }
        }

        if (MainFrame.isCavityEnabled) {
            for (int i = 0; i < boundaryBrickList.size(); i++) {
                if (boundaryBrickList.get(i).isAdjacentToCavity) {
                        if(boundaryBrickList.get(i).z2 != -1){
                            fullBrickIndex = newIndex [boundaryBrickList.get(i).x2][boundaryBrickList.get(i).y2][boundaryBrickList.get(i).z2-2];
                            if(fullBrickIndex == -1 || boundaryBrickList.get(fullBrickIndex).isRemoved || boundaryBrickList.get(fullBrickIndex).isHalfBrick() || boundaryBrickList.get(fullBrickIndex).isBoundary ||
                                    ( boundaryBrickList.get(fullBrickIndex).isRemoved == false &&
                                            ( boundaryBrickList.get(fullBrickIndex).isDomain1Replaced == true ||
                                                    boundaryBrickList.get(fullBrickIndex).isDomain2Replaced == true ||
                                                    boundaryBrickList.get(fullBrickIndex).isDomain3Replaced == true ||
                                                    boundaryBrickList.get(fullBrickIndex).isDomain4Replaced == true))){
                                boundaryBrickList.get(i).isAdjacentToCavity = false;
                            }
                            else
                                boundaryBrickList.get(fullBrickIndex).isBoundary = true; // so that cavity boundary do not join with 48nt bricks
                        }

                        else if(boundaryBrickList.get(i).z3 != -1 && boundaryBrickList.get(i).z3 > 1){
                            fullBrickIndex = newIndex [boundaryBrickList.get(i).x3][boundaryBrickList.get(i).y3][boundaryBrickList.get(i).z3-2];
                            if(fullBrickIndex == -1 || boundaryBrickList.get(fullBrickIndex).isRemoved || boundaryBrickList.get(fullBrickIndex).isHalfBrick() || boundaryBrickList.get(fullBrickIndex).isBoundary ||
                                    ( boundaryBrickList.get(fullBrickIndex).isRemoved == false &&
                                            ( boundaryBrickList.get(fullBrickIndex).isDomain1Replaced == true ||
                                                    boundaryBrickList.get(fullBrickIndex).isDomain2Replaced == true ||
                                                    boundaryBrickList.get(fullBrickIndex).isDomain3Replaced == true ||
                                                    boundaryBrickList.get(fullBrickIndex).isDomain4Replaced == true))){
                                boundaryBrickList.get(i).isAdjacentToCavity = false;
                            }
                            else
                                boundaryBrickList.get(fullBrickIndex).isBoundary = true; // so that cavity boundary do not join with 48nt bricks
                        }

                    }
                }

            }
        }
    }
