/** Author: Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */

import java.util.ArrayList;

//This class takes care of all the modifications needed to be made to
// the sequences once the unnecessary voxels have been deleted.
public class SaveFinalSequences {

    static ArrayList<DNASequence> finalSeqList= CoordinatesSequenceMap.sequencesList;
    public static ArrayList<XYZCoordinates> finalCoordinateList= CoordinatesSequenceMap.coordinatesList;
    static ArrayList<VoxelToBrick> finalData=new ArrayList<VoxelToBrick>();

    int[][][] oldIndex = CoordinatesSequenceMap.ivalue;
    int[][][] newIndex = new int[MainFrame.width][MainFrame.height][MainFrame.depth+2];
    int x,y,z,indexfront, indexback;
    String Tsequence="TTTTTTTT";

    //class constructor
    public SaveFinalSequences(){
        finalData.clear();
        MainFrame.enableGraph();
        convertToT();
        removeTVoxels();
        //set all values in newIndex to -1
        for(int i = 0; i<MainFrame.width; i++)
            for(int j = 0; j<MainFrame.height; j++)
                for(int k = 0; k<=MainFrame.depth; k++)
                    newIndex [i][j][k] = -1;
        setBrickList();
        addHelixNumber();

        if(MainFrame.isBoundaryCalled)
            attachBoundaryBricks();
    }

    //This function replaces the remaining complementary strands of the deleted voxels with 8-length Tymidine sequences.
    public void convertToT(){
        for(int i=0;i<finalCoordinateList.size();i++){
            x=finalCoordinateList.get(i).x;
            y=finalCoordinateList.get(i).y;
            z=finalCoordinateList.get(i).z;

            if(finalCoordinateList.get(i).isRemoved==true ){
                System.out.println("Removed found in convert-to-T:"+x+""+y+""+z);
                if(z > 0&& MainFrame.isMinZProtectorPlaneEnabled || z>1&&!MainFrame.isMinZProtectorPlaneEnabled){
                    indexback=oldIndex[x][y][z-1];
                    if(finalCoordinateList.get(indexback).isRemoved == false){
                        System.out.println("Searching in convert-to-T:"+x+""+y+""+(z-1));
                        if(finalCoordinateList.get(i).domain==34)
                            finalCoordinateList.get(indexback).Domain1=finalCoordinateList.
                                   get(indexback).Domain1.replace(finalCoordinateList.get(indexback).Domain1, Tsequence);

                        else
                            finalCoordinateList.get(indexback).Domain4=finalCoordinateList.
                                   get(indexback).Domain4.replace(finalCoordinateList.get(indexback).Domain4, Tsequence);
                    }
                }
                if(z <MainFrame.depth){
                    indexfront=oldIndex[x][y][z+1];
                    if(finalCoordinateList.get(indexfront).isRemoved == false){
                        System.out.println("Searching in convert-to-T:"+x+""+y+""+(z+1));
                        if(finalCoordinateList.get(i).domain==34)
                            finalCoordinateList.get(indexfront).Domain2=finalCoordinateList.get(indexfront).Domain2.replace
                                    (finalCoordinateList.get(indexfront).Domain2, Tsequence);

                        else
                            finalCoordinateList.get(indexfront).Domain3=finalCoordinateList.get(indexfront).Domain3.replace
                                    (finalCoordinateList.get(indexfront).Domain3, Tsequence);
                    }
                }
            }
        }
    }

    //This function identifies and removes those voxels which have d3&d4 OR d1&d2 as T-sequences
    // (i.e., when an entire voxel is composed of only Thymidine).
    public void removeTVoxels(){
        System.out.println("Size of the arraylist: "+finalCoordinateList.size());
        for(int i=0;i<finalCoordinateList.size();i++){

            x=finalCoordinateList.get(i).x;
            y=finalCoordinateList.get(i).y;
            z=finalCoordinateList.get(i).z;
            System.out.println("trying to access "+x+""+y+""+z);
            if(finalCoordinateList.get(i).isRemoved==false){
                if(finalCoordinateList.get(i).domain==34){
                    if(finalCoordinateList.get(i).Domain3.equals(Tsequence)&&
                            finalCoordinateList.get(i).Domain4.equals(Tsequence)){
                        System.out.println("removed in removeTVoxels:"+x+""+y+""+z);
                        finalCoordinateList.get(i).isRemoved=true;
                    }
                }
                else if(finalCoordinateList.get(i).domain==12){
                    if(finalCoordinateList.get(i).Domain1.equals(Tsequence)&&
                            finalCoordinateList.get(i).Domain2.equals(Tsequence)){
                        System.out.println("removed in removeTVoxels:"+x+""+y+""+z);
                        finalCoordinateList.get(i).isRemoved=true;
                    }
                }
            }
        }
    }

    //This function combines voxels to form bricks of size=2 voxels and modifies them as full bricks in the final-coordinate-list.
    //This function combines voxels to form bricks of size=2 voxels and modifies them as full bricks in the final-coordinate-list.
    public void setBrickList(){
        System.out.println("SetbrickList called");
        int x,y,z;
        int brickCounter = 0;
        for(int i=0;i<finalCoordinateList.size();i++){

            if(finalCoordinateList.get(i).isRemoved==false){

                x = finalCoordinateList.get(i).x;
                y = finalCoordinateList.get(i).y;
                z = finalCoordinateList.get(i).z;

                //Adding half-bricks with domains 3 and 4 to the final-coordinate-list.
                if(finalCoordinateList.get(i).isHalfBrick){
                    if(finalCoordinateList.get(i).domain==34) {
                        finalData.add(new VoxelToBrick(-1, -1, -1, x, y, z, null, null,
                                finalCoordinateList.get(i).Domain3, finalCoordinateList.get(i).Domain4));
                    }
                    else {
                        finalData.add(new VoxelToBrick(x, y, z, -1, -1, -1, finalCoordinateList.get(i).Domain1,
                                finalCoordinateList.get(i).Domain2, null, null));
                    }
                    finalData.get(brickCounter).isBottommostHalfBrick = finalCoordinateList.get(i).isBottommostBrick;
                    finalData.get(brickCounter).isTopmostHalfBrick = finalCoordinateList.get(i).isTopmostBrick;
                    finalData.get(brickCounter).isLeftmostHalfBrick = finalCoordinateList.get(i).isLeftmostBrick;
                    finalData.get(brickCounter).isRightmostHalfBrick = finalCoordinateList.get(i).isRightmostBrick;
                    System.out.println("%%%%%% half brick identified at ["+finalData.get(brickCounter).x2+" "
                            +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                            +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                            +finalData.get(brickCounter).z3+"] %%%%%%%");
                }
                else{
                    //Identifying full and half north-bricks and adding them to the final-coordinate-list.
                    if(finalCoordinateList.get(i).orientation=='n'){
                        if(finalCoordinateList.get(i).domain==34){
                            if(finalCoordinateList.get(oldIndex[x][y+1][z]).isRemoved==false){

                                finalData.add(new VoxelToBrick(x,y+1,z,x,y,z,
                                        finalCoordinateList.get(oldIndex[x][y+1][z]).Domain1,
                                        finalCoordinateList.get(oldIndex[x][y+1][z]).Domain2,
                                        finalCoordinateList.get(i).Domain3, finalCoordinateList.get(i).Domain4));

                                finalCoordinateList.get(oldIndex[x][y+1][z]).isRemoved=true;
                                newIndex[x][y+1][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        finalCoordinateList.get(i).Domain3, finalCoordinateList.get(i).Domain4));
                                System.out.println("??????not half brick identified at ["+finalData.get(brickCounter).x2+" "
                                        +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                                        +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                                        +finalData.get(brickCounter).z3+"] ??????");
                            }
                        }
                        else{
                            if(finalCoordinateList.get(oldIndex[x][y-1][z]).isRemoved==false){

                                finalData.add(new VoxelToBrick(x,y,z,x,y-1,z,
                                        finalCoordinateList.get(i).Domain1,
                                        finalCoordinateList.get(i).Domain2,
                                        finalCoordinateList.get(oldIndex[x][y-1][z]).Domain3,
                                        finalCoordinateList.get(oldIndex[x][y-1][z]).Domain4));

                                finalCoordinateList.get(oldIndex[x][y-1][z]).isRemoved=true;
                                newIndex[x][y-1][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,finalCoordinateList.get(i).Domain1,
                                        finalCoordinateList.get(i).Domain2, null, null));
                                System.out.println("??????not half brick identified at ["+finalData.get(brickCounter).x2+" "
                                        +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                                        +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                                        +finalData.get(brickCounter).z3+"] ??????");
                            }

                        }
                    }

                    //Identifying full and half west-bricks and adding them to the final-coordinate-list.
                    if(finalCoordinateList.get(i).orientation=='w'){
                        if(finalCoordinateList.get(i).domain==34){
                            if(finalCoordinateList.get(oldIndex[x+1][y][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x+1,y,z,x,y,z,
                                        finalCoordinateList.get(oldIndex[x+1][y][z]).Domain1,
                                        finalCoordinateList.get(oldIndex[x+1][y][z]).Domain2,
                                        finalCoordinateList.get(i).Domain3, finalCoordinateList.get(i).Domain4));

                                finalCoordinateList.get(oldIndex[x+1][y][z]).isRemoved=true;
                                newIndex[x+1][y][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        finalCoordinateList.get(i).Domain3, finalCoordinateList.get(i).Domain4));
                                System.out.println("??????not half brick identified at ["+finalData.get(brickCounter).x2+" "
                                        +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                                        +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                                        +finalData.get(brickCounter).z3+"] ??????");
                            }
                        }
                        else{
                            if(finalCoordinateList.get(oldIndex[x-1][y][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x,y,z,x-1,y,z,
                                        finalCoordinateList.get(i).Domain1,
                                        finalCoordinateList.get(i).Domain2,
                                        finalCoordinateList.get(oldIndex[x-1][y][z]).Domain3,
                                        finalCoordinateList.get(oldIndex[x-1][y][z]).Domain4));

                                finalCoordinateList.get(oldIndex[x-1][y][z]).isRemoved=true;
                                newIndex[x-1][y][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,finalCoordinateList.get(i).Domain1,
                                        finalCoordinateList.get(i).Domain2, null, null));
                            }
                        }
                    }

                    //Identifying full and half south-bricks and adding them to the final-coordinate-list.
                    if(finalCoordinateList.get(i).orientation=='s'){
                        if(finalCoordinateList.get(i).domain==12){
                            if(finalCoordinateList.get(oldIndex[x][y+1][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x,y,z,x,y+1,z,
                                        finalCoordinateList.get(i).Domain1, finalCoordinateList.get(i).Domain2,
                                        finalCoordinateList.get(oldIndex[x][y+1][z]).Domain3,
                                        finalCoordinateList.get(oldIndex[x][y+1][z]).Domain4
                                ));

                                finalCoordinateList.get(oldIndex[x][y+1][z]).isRemoved=true;
                                newIndex[x][y+1][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        finalCoordinateList.get(i).Domain1, finalCoordinateList.get(i).Domain2));
                            }
                        }
                        else{
                            if(finalCoordinateList.get(oldIndex[x][y-1][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x,y-1,z,x,y,z,
                                        finalCoordinateList.get(oldIndex[x][y-1][z]).Domain1,
                                        finalCoordinateList.get(oldIndex[x][y-1][z]).Domain2,
                                        finalCoordinateList.get(i).Domain3,
                                        finalCoordinateList.get(i).Domain4
                                ));

                                finalCoordinateList.get(oldIndex[x][y-1][z]).isRemoved=true;
                                newIndex[x][y-1][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z, null, null,finalCoordinateList.get(i).Domain3,
                                        finalCoordinateList.get(i).Domain4));
                            }
                        }
                    }
                    //Identifying full and half east-bricks and adding them to the final-coordinate-list.
                    if(finalCoordinateList.get(i).orientation=='e'){
                        if(finalCoordinateList.get(i).domain==12){
                            if(finalCoordinateList.get(oldIndex[x+1][y][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x,y,z,x+1,y,z,
                                        finalCoordinateList.get(i).Domain1, finalCoordinateList.get(i).Domain2,
                                        finalCoordinateList.get(oldIndex[x+1][y][z]).Domain3,
                                        finalCoordinateList.get(oldIndex[x+1][y][z]).Domain4
                                ));

                                finalCoordinateList.get(oldIndex[x+1][y][z]).isRemoved=true;
                                newIndex[x+1][y][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,null, null,
                                        finalCoordinateList.get(i).Domain1, finalCoordinateList.get(i).Domain2));
                            }
                        }
                        else{
                            if(finalCoordinateList.get(oldIndex[x-1][y][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x-1,y,z,x,y,z,
                                        finalCoordinateList.get(oldIndex[x-1][y][z]).Domain1,
                                        finalCoordinateList.get(oldIndex[x-1][y][z]).Domain2,
                                        finalCoordinateList.get(i).Domain3,
                                        finalCoordinateList.get(i).Domain4
                                ));

                                finalCoordinateList.get(oldIndex[x-1][y][z]).isRemoved=true;
                                newIndex[x-1][y][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,finalCoordinateList.get(i).Domain3,
                                        finalCoordinateList.get(i).Domain4));
                            }

                        }
                    }
                }
                //Remove the individual voxel after combining it as a full brick in the final-coordinate-list.
                finalCoordinateList.get(i).isRemoved=true;
                newIndex[x][y][z] = brickCounter;
                brickCounter++;
            }

        }
    }
    public void addHelixNumber(){
        int x2, x3, y2, y3;

        for (int i = 0; i<finalData.size(); i++) {
            x2= finalData.get(i).x2;
            x3= finalData.get(i).x3;
            y2= finalData.get(i).y2;
            y3= finalData.get(i).y3;
            if (x2!=-1) {
                if(y2==0) {
                    finalData.get(i).helix1 = x2;
                    finalData.get(i).helix2 = x2;
                }
                else if (y2%2==1) {
                    finalData.get(i).helix1 = (y2 + 1) * MainFrame.width - (x2 + 1);
                    finalData.get(i).helix2 = finalData.get(i).helix1;

                }
                else {
                    finalData.get(i).helix1 = MainFrame.width * y2 + x2;
                    finalData.get(i).helix2 = finalData.get(i).helix1;
                }
            }
            if (x3!=-1){
                if(y3==0) {
                    finalData.get(i).helix3 = x3;
                    finalData.get(i).helix4 = finalData.get(i).helix3;
                }
                else if (y3%2==1) {
                    finalData.get(i).helix3 = (y3 + 1) * MainFrame.width - (x3 + 1);
                    finalData.get(i).helix4 = finalData.get(i).helix3;
                }
                else {
                    finalData.get(i).helix3 = MainFrame.width * y3 + x3;
                    finalData.get(i).helix4 = finalData.get(i).helix3;
                }
            }
        }
    }


    //Discuss this first!!!!
    public void attachBoundaryBricks(){
        System.out.println("Calling function attachBoundaryBricks()");
        int x,y,z;
        int fullBrickIndex;
        String d1,d2;
        int h5, h6;

        for(int i = 0; i < finalData.size(); i++){
            if(finalData.get(i).isLeftmostHalfBrick || finalData.get(i).isBottommostHalfBrick || finalData.get(i).isTopmostHalfBrick ||
                    finalData.get(i).isRightmostHalfBrick) {
                if (finalData.get(i).x2 != -1) {
                    x = finalData.get(i).x2;
                    y = finalData.get(i).y2;
                    z = finalData.get(i).z2;
                    System.out.println(x + " " + y + " " + z + "  " + finalData.get(i).Domain1);
                    d1 = new String(finalData.get(i).Domain1);
                    d2 = new String(finalData.get(i).Domain2);
                    h5 = finalData.get(i).helix2;
                    h6 = finalData.get(i).helix1;
                } else {
                    x = finalData.get(i).x3;
                    y = finalData.get(i).y3;
                    z = finalData.get(i).z3;
                    d1 = new String(finalData.get(i).Domain3);
                    d2 = new String(finalData.get(i).Domain4);
                    h5 = finalData.get(i).helix3;
                    h6 = finalData.get(i).helix4;
                }
                if (MainFrame.isMinXBoundaryPlaneEnabled && finalData.get(i).isLeftmostHalfBrick && z != 1 && finalData.get(i).isRemovedAsBoundary == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {

                        if (finalData.get(fullBrickIndex).x1 == x && finalData.get(fullBrickIndex).y1 == y)
                            finalData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            finalData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);

                        finalData.get(fullBrickIndex).isBoundary = true;
                        finalData.get(i).isRemovedAsBoundary = true;
                        finalData.get(i).isLeftmostHalfBrick = false;
                        System.out.println("BoundaryBrick for min x at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMaxXBoundaryPlaneEnabled && finalData.get(i).isRightmostHalfBrick && z != 1 && finalData.get(i).isRemovedAsBoundary == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (finalData.get(fullBrickIndex).x1 == x && finalData.get(fullBrickIndex).y1 == y)
                            finalData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            finalData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        finalData.get(fullBrickIndex).isBoundary = true;
                        finalData.get(i).isRemovedAsBoundary = true;
                        finalData.get(i).isLeftmostHalfBrick = false;
                        System.out.println("BoundaryBrick for max x at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMinYBoundaryPlaneEnabled && finalData.get(i).isTopmostHalfBrick && z != 1 && finalData.get(i).isRemovedAsBoundary == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (finalData.get(fullBrickIndex).x1 == x && finalData.get(fullBrickIndex).y1 == y)
                            finalData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            finalData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        finalData.get(fullBrickIndex).isBoundary = true;
                        finalData.get(i).isRemovedAsBoundary = true;
                        finalData.get(i).isLeftmostHalfBrick = false;
                        System.out.println("BoundaryBrick for min y at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMaxYBoundaryPlaneEnabled && finalData.get(i).isBottommostHalfBrick && z != 1 && finalData.get(i).isRemovedAsBoundary == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (finalData.get(fullBrickIndex).x1 == x && finalData.get(fullBrickIndex).y1 == y)
                            finalData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            finalData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        finalData.get(fullBrickIndex).isBoundary = true;
                        finalData.get(i).isRemovedAsBoundary = true;
                        finalData.get(i).isLeftmostHalfBrick = false;
                        System.out.println("BoundaryBrick for max y at" + x + " " + y + " " + z);
                    }
                }
            }
        }

        int s = finalData.size();
        for(int i=0;i<s;i++){
            if(finalData.get(i).isRemovedAsBoundary == true){
                finalData.remove(i);
                s--;
            }
        }
    }
}
