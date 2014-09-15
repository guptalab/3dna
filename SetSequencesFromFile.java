
/**
 * Created by foram.joshi on 28.08.2014.
 */
import java.util.ArrayList;

public class SetSequencesFromFile {
    static ArrayList<HelixAndCoordinates> finalFullBrickSequencesList= ImportSequencesCoordinatesMap.fullBrickSequencesList;
    static ArrayList<HelixAndCoordinates> finalHalfBrickSequencesList= ImportSequencesCoordinatesMap.halfBrickSequencesList;
    static ArrayList<HelixAndCoordinates> finalBoundaryBrickSequencesList= ImportSequencesCoordinatesMap.boundaryBrickSequencesList;
    public static ArrayList<XYZCoordinates> finalCoordinateList= ImportSequencesCoordinatesMap.coordinatesList;
    static ArrayList<VoxelToBrick> finalData=new ArrayList<VoxelToBrick>();

    int[][][] oldIndex = ImportSequencesCoordinatesMap.ivalue;
    int[][][] newIndex = new int[MainFrame.width][MainFrame.height][MainFrame.depth+2];
    int x,y,z,indexfront, indexback;
    String Tsequence="TTTTTTTT";

    //class constructor
    public SetSequencesFromFile(){
        finalData.clear();
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
        if(MainFrame.isBoundaryCalled)
            attachBoundaryBricks();

    }

    //This function replaces the remaining complementary strands of the deleted voxels with 8-length Thymine sequences.
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
                            finalCoordinateList.get(indexback).isDomain1Replaced = true;

                        else
                            finalCoordinateList.get(indexback).isDomain4Replaced = true;
                    }
                }
                if(z <MainFrame.depth){
                    indexfront=oldIndex[x][y][z+1];
                    if(finalCoordinateList.get(indexfront).isRemoved == false){
                        System.out.println("Searching in convert-to-T:"+x+""+y+""+(z+1));
                        if(finalCoordinateList.get(i).domain==34)
                            finalCoordinateList.get(indexfront).isDomain2Replaced = true;

                        else
                            finalCoordinateList.get(indexfront).isDomain3Replaced = true;
                    }
                }
            }
        }
    }

    //This function identifies and removes those voxels which have d3&d4 OR d1&d2 as T-sequences
    // (i.e., when an entire voxel is composed of only Thymidine).
    public void removeTVoxels(){
        for(int i=0;i<finalCoordinateList.size();i++){

            x=finalCoordinateList.get(i).x;
            y=finalCoordinateList.get(i).y;
            z=finalCoordinateList.get(i).z;

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
                        finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(i).isDomain3Replaced;
                        finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(i).isDomain4Replaced;
                    }
                    else {
                        finalData.add(new VoxelToBrick(x, y, z, -1, -1, -1, finalCoordinateList.get(i).Domain1,
                                finalCoordinateList.get(i).Domain2, null, null));
                        finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(i).isDomain1Replaced;
                        finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(i).isDomain2Replaced;
                    }
                    finalData.get(brickCounter).isBottommostHalfBrick = finalCoordinateList.get(i).isBottommostBrick;
                    finalData.get(brickCounter).isTopmostHalfBrick = finalCoordinateList.get(i).isTopmostBrick;
                    finalData.get(brickCounter).isLeftmostHalfBrick = finalCoordinateList.get(i).isLeftmostBrick;
                    finalData.get(brickCounter).isRightmostHalfBrick = finalCoordinateList.get(i).isRightmostBrick;
                    /*System.out.println("%%%%%% half brick identified at ["+finalData.get(brickCounter).x2+" "
                            +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                            +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                            +finalData.get(brickCounter).z3+"] %%%%%%%");*/
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
                                finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(oldIndex[x][y+1][z]).isDomain1Replaced;
                                finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(oldIndex[x][y+1][z]).isDomain2Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        finalCoordinateList.get(i).Domain3, finalCoordinateList.get(i).Domain4));
                            }
                            finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(i).isDomain3Replaced;
                            finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(i).isDomain4Replaced;
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
                                finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(oldIndex[x][y-1][z]).isDomain3Replaced;
                                finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(oldIndex[x][y-1][z]).isDomain4Replaced;

                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,finalCoordinateList.get(i).Domain1,
                                        finalCoordinateList.get(i).Domain2, null, null));
                            }

                            finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(i).isDomain1Replaced;
                            finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(i).isDomain2Replaced;

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
                                finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(oldIndex[x+1][y][z]).isDomain1Replaced;
                                finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(oldIndex[x+1][y][z]).isDomain2Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        finalCoordinateList.get(i).Domain3, finalCoordinateList.get(i).Domain4));
                            }
                            finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(i).isDomain3Replaced;
                            finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(i).isDomain4Replaced;
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
                                finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(oldIndex[x-1][y][z]).isDomain3Replaced;
                                finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(oldIndex[x-1][y][z]).isDomain4Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,finalCoordinateList.get(i).Domain1,
                                        finalCoordinateList.get(i).Domain2, null, null));
                            }
                            finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(i).isDomain1Replaced;
                            finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(i).isDomain2Replaced;
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
                                finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(oldIndex[x][y+1][z]).isDomain3Replaced;
                                finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(oldIndex[x][y+1][z]).isDomain4Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        finalCoordinateList.get(i).Domain1, finalCoordinateList.get(i).Domain2));
                            }
                            finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(i).isDomain1Replaced;
                            finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(i).isDomain2Replaced;
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
                                finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(oldIndex[x][y-1][z]).isDomain1Replaced;
                                finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(oldIndex[x][y-1][z]).isDomain2Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z, null, null,finalCoordinateList.get(i).Domain3,
                                        finalCoordinateList.get(i).Domain4));
                            }
                            finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(i).isDomain3Replaced;
                            finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(i).isDomain4Replaced;
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
                                finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(oldIndex[x+1][y][z]).isDomain3Replaced;
                                finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(oldIndex[x+1][y][z]).isDomain4Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,null, null,
                                        finalCoordinateList.get(i).Domain1, finalCoordinateList.get(i).Domain2));
                            }
                            finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(i).isDomain1Replaced;
                            finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(i).isDomain2Replaced;

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
                                finalData.get(brickCounter).isDomain1Replaced = finalCoordinateList.get(oldIndex[x-1][y][z]).isDomain1Replaced;
                                finalData.get(brickCounter).isDomain2Replaced = finalCoordinateList.get(oldIndex[x-1][y][z]).isDomain2Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,finalCoordinateList.get(i).Domain3,
                                        finalCoordinateList.get(i).Domain4));
                            }
                            finalData.get(brickCounter).isDomain3Replaced = finalCoordinateList.get(i).isDomain3Replaced;
                            finalData.get(brickCounter).isDomain4Replaced = finalCoordinateList.get(i).isDomain4Replaced;

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
    public void mapSequencesFromList(){
        int index = -1;
        int h1, h2, h3, h4, z1, z2, z3, z4;
        boolean domain1Replacement, domain2Replacement, domain3Replacement, domain4Replacement;

        for(int i = 0; i<finalData.size(); i++){
            index = -1;
            //identifying half-bricks having domains 34
            if(finalData.get(i).x2==-1){
                h3 = finalData.get(i).helix3;
                h4 = h3;
                z3 = finalData.get(i).z3;
                z4 = z3+1;

                for(int j = 0; j<finalHalfBrickSequencesList.size(); j++){
                    if (finalHalfBrickSequencesList.get(j).helix1 == h3&&
                            finalHalfBrickSequencesList.get(j).zCoordinate1 == z3&&
                            finalHalfBrickSequencesList.get(j).zCoordinate2 == z4&&
                            finalHalfBrickSequencesList.get(j).helix2 == h3) {
                        index = j;

                        finalData.get(i).Domain3 = finalHalfBrickSequencesList.get(index).domain1;
                        finalData.get(i).Domain4 = finalHalfBrickSequencesList.get(index).domain2;
                        finalData.get(i).strandNumber = finalHalfBrickSequencesList.get(index).strandNumber;
                        finalData.get(i).plateNumber = finalHalfBrickSequencesList.get(index).plateNumber;
                        finalData.get(i).position = finalHalfBrickSequencesList.get(index).position;

                        //System.out.println("halfbrick data added for domains 3 and 4 "+h3+" "+z3+" "+h4+" "+z4+" "+
                        //        finalData.get(i).Domain3+" "+finalData.get(i).Domain4);
                    }
                }
                if(index==-1) {
                    System.out.println("############ not found for " + -1 + " " + -1 + " " + -1 + " " + -1 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + "?" + index);
                    finalData.get(i).isMapped = false;
                }
                else {
                    //System.out.println("*************found for "+-1+" "+-1+" "+-1+" "+-1+" "+h3+" "+z3+" "+h4+" "+z4+"?"+index);
                }
            }
            //identifying half bricks having domains 1 and 2
            else if(finalData.get(i).x3==-1){
                h1 = finalData.get(i).helix1;
                h2 = h1;
                z1 = finalData.get(i).z1;
                z2 = finalData.get(i).z2;

                for(int j = 0; j<finalHalfBrickSequencesList.size(); j++){
                    if (finalHalfBrickSequencesList.get(j).helix2 == h1&&
                            finalHalfBrickSequencesList.get(j).zCoordinate1 == z2&&
                            finalHalfBrickSequencesList.get(j).zCoordinate2 == z1&&
                            finalHalfBrickSequencesList.get(j).helix1 == h2) {
                        index = j;
                        String d1 = new StringBuilder(finalHalfBrickSequencesList.get(index).domain2).reverse().toString();
                        String d2 = new StringBuilder(finalHalfBrickSequencesList.get(index).domain1).reverse().toString();
                        finalData.get(i).Domain1 = d1;
                        finalData.get(i).Domain2 = d2;
                        finalData.get(i).strandNumber = finalHalfBrickSequencesList.get(index).strandNumber;
                        finalData.get(i).plateNumber = finalHalfBrickSequencesList.get(index).plateNumber;
                        finalData.get(i).position = finalHalfBrickSequencesList.get(index).position;

                        //System.out.println("halfbrick data added for doimans 1 and 2 "+h1+" "+z1+" "+h2+" "+z2+" "+
                        //        finalData.get(i).Domain1+" "+finalData.get(i).Domain2);
                    }
                }
                if(index==-1) {
                    System.out.println("############ not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + -1 + " " + -1 + " " + -1 + " " + -1 + "?" + index);
                    finalData.get(i).isMapped = false;
                }
                else{
                    //System.out.println("*************found for "+h1+" "+z1+" "+h2+" "+z2+" "+-1+" "+-1+" "+-1+" "+-1+"?"+index);
                }
            }
            //identifying full bricks
            else if((finalData.get(i).x2!=-1)&&(finalData.get(i).x3!=-1)){
                h1 = finalData.get(i).helix1;
                h2 = h1;
                h3 = finalData.get(i).helix3;
                h4 = h3;
                z1 = finalData.get(i).z1;
                z2 = z1 - 1;
                z4 = finalData.get(i).z4;
                z3 = z4 - 1;
                domain1Replacement = finalData.get(i).isDomain1Replaced ;
                domain2Replacement = finalData.get(i).isDomain2Replaced ;
                domain3Replacement = finalData.get(i).isDomain3Replaced ;
                domain4Replacement = finalData.get(i).isDomain4Replaced ;

                for(int j = 0; j<finalFullBrickSequencesList.size(); j++){
                    if (finalFullBrickSequencesList.get(j).helix1 == h1 &&
                            finalFullBrickSequencesList.get(j).zCoordinate1 == z1 &&
                            finalFullBrickSequencesList.get(j).zCoordinate2 == z2 &&
                            finalFullBrickSequencesList.get(j).helix3 == h3 &&
                            finalFullBrickSequencesList.get(j).helix2 == h2 &&
                            finalFullBrickSequencesList.get(j).helix4 == h4 &&
                            finalFullBrickSequencesList.get(j).zCoordinate3 == z3 &&
                            finalFullBrickSequencesList.get(j).zCoordinate4 == z4 &&
                            domain1Replacement == finalFullBrickSequencesList.get(j).isDomain1Replaced &&
                            domain2Replacement == finalFullBrickSequencesList.get(j).isDomain2Replaced &&
                            domain3Replacement == finalFullBrickSequencesList.get(j).isDomain3Replaced &&
                            domain4Replacement == finalFullBrickSequencesList.get(j).isDomain4Replaced ) {
                        index = j;
                    }
                }
                if(index!=-1){
                    finalData.get(i).Domain1 = finalFullBrickSequencesList.get(index).domain1;
                    finalData.get(i).Domain2 = finalFullBrickSequencesList.get(index).domain2;
                    finalData.get(i).Domain3 = finalFullBrickSequencesList.get(index).domain3;
                    finalData.get(i).Domain4 = finalFullBrickSequencesList.get(index).domain4;
                    finalData.get(i).strandNumber = finalFullBrickSequencesList.get(index).strandNumber;
                    finalData.get(i).plateNumber = finalFullBrickSequencesList.get(index).plateNumber;
                    finalData.get(i).position = finalFullBrickSequencesList.get(index).position;

                    /*System.out.println("fullbrick data added "+h1+" "+z1+" "+h2+" "+z2+" "+h3+" "+z3+" "+h4+" "+z4+" "+
                            finalData.get(i).Domain1+" "+finalData.get(i).Domain2
                            +" "+
                            finalData.get(i).Domain3+" "+finalData.get(i).Domain4);*/
                }
                if(index==-1) {
                    System.out.println("############ not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + "?" + index);
                    finalData.get(i).isMapped = false;
                }
                else {
                    // System.out.println("*************found for "+h1+" "+z1+" "+h2+" "+z2+" "+h3+" "+z3+" "+h4+" "+z4+"?"+index);
                }
            }
        }
    }

    //Discuss this first!!!!
    public void attachBoundaryBricks(){
        int x, y, z1, z2, z3, z4, z5, z6, h1, h2, h3, h4, h5, h6;
        int fullBrickIndex;
        String d1,d2;

        for(int i = 0; i < finalData.size() ; i++){
            if(finalData.get(i).isMapped == true &&
                    (finalData.get(i).isLeftmostHalfBrick ||
                    finalData.get(i).isBottommostHalfBrick ||
                    finalData.get(i).isTopmostHalfBrick ||
                    finalData.get(i).isRightmostHalfBrick)) {
                if (finalData.get(i).x3 == -1) {
                    x = finalData.get(i).x2;
                    y = finalData.get(i).y2;
                    z6 = finalData.get(i).z1;
                    z5 = z6 - 1;
                    d1 = new String(finalData.get(i).Domain1);
                    d2 = new String(finalData.get(i).Domain2);
                    h5 = finalData.get(i).helix1;
                    h6 = h5;
                } else {
                    x = finalData.get(i).x3;
                    y = finalData.get(i).y3;
                    z5 = finalData.get(i).y3;
                    z6 = z5 + 1;
                    d1 = new String(finalData.get(i).Domain3);
                    d2 = new String(finalData.get(i).Domain4);
                    h5 = finalData.get(i).helix3;
                    h6 = h5;


                }

                if (MainFrame.isMinXBoundaryPlaneEnabled &&
                        finalData.get(i).isLeftmostHalfBrick &&
                        z5 > 1 &&
                        z5 != MainFrame.depth){//half bricks missing in Yonggang Ke's file

                    fullBrickIndex = newIndex[x][y][z5 - 2];
                    if (fullBrickIndex != -1) {
                        h1 = finalData.get(fullBrickIndex).helix1;
                        h2 = h1;
                        h3 = finalData.get(fullBrickIndex).helix3;
                        h4 = h3;
                        z1 = finalData.get(fullBrickIndex).z1;
                        z2 = z1 - 1;
                        z3 = finalData.get(fullBrickIndex).z3;
                        z4 = z3 + 1;

                        int index = -1;

                        for (int j = 0; j < finalBoundaryBrickSequencesList.size(); j++) {
                            if (finalBoundaryBrickSequencesList.get(j).helix1 == h1 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                    finalBoundaryBrickSequencesList.get(j).helix3 == h3 &&
                                    finalBoundaryBrickSequencesList.get(j).helix2 == h2 &&
                                    finalBoundaryBrickSequencesList.get(j).helix4 == h4 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                    finalBoundaryBrickSequencesList.get(j).helix5 == h5 &&
                                    finalBoundaryBrickSequencesList.get(j).helix6 == h6 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                index = j;
                            }
                        }
                        if (index != -1) {
                            finalData.get(fullBrickIndex).Domain1 = finalBoundaryBrickSequencesList.get(index).domain1;
                            finalData.get(fullBrickIndex).Domain2 = finalBoundaryBrickSequencesList.get(index).domain2;
                            finalData.get(fullBrickIndex).Domain3 = finalBoundaryBrickSequencesList.get(index).domain3;
                            finalData.get(fullBrickIndex).Domain4 = finalBoundaryBrickSequencesList.get(index).domain4;
                            finalData.get(fullBrickIndex).Domain5 = finalBoundaryBrickSequencesList.get(index).domain5;
                            finalData.get(fullBrickIndex).Domain6 = finalBoundaryBrickSequencesList.get(index).domain6;
                            finalData.get(fullBrickIndex).helix5 = h5;
                            finalData.get(fullBrickIndex).helix6 = h6;
                            finalData.get(fullBrickIndex).strandNumber = finalBoundaryBrickSequencesList.get(index).strandNumber;
                            finalData.get(fullBrickIndex).plateNumber = finalBoundaryBrickSequencesList.get(index).plateNumber;
                            finalData.get(fullBrickIndex).position = finalBoundaryBrickSequencesList.get(index).position;
                            finalData.get(fullBrickIndex).isBoundary = true;
                            finalData.get(i).isMapped = false;

                            //System.out.println("Boundary brick data added " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + " ");
                        }
                        if (index == -1) {
                            System.out.println("############ Boundary brick not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                                    +h5 + " " + z5 + " " + h6 + " " + z6 + "?" + index);
                            finalData.get(i).isMapped = false;
                        } else {
                            //System.out.println("*************found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " + h5 + " " + z5 + " " + h6 + " " + z6 + "?" + index);
                        }
                        finalData.get(i).isRemovedAsBoundary = true;
                        finalData.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for min x at" + x + " " + y + " " + z5);
                    }
                }
                if (MainFrame.isMaxXBoundaryPlaneEnabled &&
                        finalData.get(i).isRightmostHalfBrick &&
                        z5 > 1 &&
                        //half bricks missing in Yonggang Ke's file
                        z5 != MainFrame.depth ) {
                    fullBrickIndex = newIndex[x][y][z5 - 2];
                    if (fullBrickIndex != -1) {
                        h1 = finalData.get(fullBrickIndex).helix1;
                        h2 = h1;
                        h3 = finalData.get(fullBrickIndex).helix3;
                        h4 = h3;
                        z1 = finalData.get(fullBrickIndex).z1;
                        z2 = z1 - 1;
                        z3 = finalData.get(fullBrickIndex).z3;
                        z4 = z3 + 1;

                        int index = -1;

                        for (int j = 0; j < finalBoundaryBrickSequencesList.size(); j++) {
                            if (finalBoundaryBrickSequencesList.get(j).helix1 == h1 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                    finalBoundaryBrickSequencesList.get(j).helix3 == h3 &&
                                    finalBoundaryBrickSequencesList.get(j).helix2 == h2 &&
                                    finalBoundaryBrickSequencesList.get(j).helix4 == h4 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                    finalBoundaryBrickSequencesList.get(j).helix5 == h5 &&
                                    finalBoundaryBrickSequencesList.get(j).helix6 == h6 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                index = j;
                            }
                        }
                        if (index != -1) {
                            finalData.get(fullBrickIndex).Domain1 = finalBoundaryBrickSequencesList.get(index).domain1;
                            finalData.get(fullBrickIndex).Domain2 = finalBoundaryBrickSequencesList.get(index).domain2;
                            finalData.get(fullBrickIndex).Domain3 = finalBoundaryBrickSequencesList.get(index).domain3;
                            finalData.get(fullBrickIndex).Domain4 = finalBoundaryBrickSequencesList.get(index).domain4;
                            finalData.get(fullBrickIndex).Domain5 = finalBoundaryBrickSequencesList.get(index).domain5;
                            finalData.get(fullBrickIndex).Domain6 = finalBoundaryBrickSequencesList.get(index).domain6;
                            finalData.get(fullBrickIndex).helix5 = h5;
                            finalData.get(fullBrickIndex).helix6 = h6;
                            finalData.get(fullBrickIndex).strandNumber = finalBoundaryBrickSequencesList.get(index).strandNumber;
                            finalData.get(fullBrickIndex).plateNumber = finalBoundaryBrickSequencesList.get(index).plateNumber;
                            finalData.get(fullBrickIndex).position = finalBoundaryBrickSequencesList.get(index).position;
                            finalData.get(fullBrickIndex).isBoundary = true;
                            finalData.get(i).isMapped = false;

                            //System.out.println("Boundary brick data added " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + " ");
                        }
                        if (index == -1) {
                            System.out.println("############ Boundary brick not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                                    +h5 + " " + z5 + " " + h6 + " " + z6 + "?" + index);
                            finalData.get(i).isMapped = false;
                        } else {
                            //System.out.println("*************found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " + h5 + " " + z5 + " " + h6 + " " + z6 + "?" + index);
                        }
                        finalData.get(i).isRemovedAsBoundary = true;
                        finalData.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for max x at" + x + " " + y + " " + z2);
                    }
                }
                if (MainFrame.isMinYBoundaryPlaneEnabled && finalData.get(i).isTopmostHalfBrick &&
                        z5 > 1 &&
                        //half bricks missing in Yonggang Ke's file
                        z5 != MainFrame.depth ) {

                    fullBrickIndex = newIndex[x][y][z5 - 2];
                    if (fullBrickIndex != -1) {
                        h1 = finalData.get(fullBrickIndex).helix1;
                        h2 = h1;
                        h3 = finalData.get(fullBrickIndex).helix3;
                        h4 = h3;
                        z1 = finalData.get(fullBrickIndex).z1;
                        z2 = z1 - 1;
                        z3 = finalData.get(fullBrickIndex).z3;
                        z4 = z3 + 1;

                        int index = -1;

                        for (int j = 0; j < finalBoundaryBrickSequencesList.size(); j++) {
                            if (finalBoundaryBrickSequencesList.get(j).helix1 == h1 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                    finalBoundaryBrickSequencesList.get(j).helix3 == h3 &&
                                    finalBoundaryBrickSequencesList.get(j).helix2 == h2 &&
                                    finalBoundaryBrickSequencesList.get(j).helix4 == h4 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                    finalBoundaryBrickSequencesList.get(j).helix5 == h5 &&
                                    finalBoundaryBrickSequencesList.get(j).helix6 == h6 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                index = j;
                            }
                        }
                        if (index != -1) {
                            finalData.get(fullBrickIndex).Domain1 = finalBoundaryBrickSequencesList.get(index).domain1;
                            finalData.get(fullBrickIndex).Domain2 = finalBoundaryBrickSequencesList.get(index).domain2;
                            finalData.get(fullBrickIndex).Domain3 = finalBoundaryBrickSequencesList.get(index).domain3;
                            finalData.get(fullBrickIndex).Domain4 = finalBoundaryBrickSequencesList.get(index).domain4;
                            finalData.get(fullBrickIndex).Domain5 = finalBoundaryBrickSequencesList.get(index).domain5;
                            finalData.get(fullBrickIndex).Domain6 = finalBoundaryBrickSequencesList.get(index).domain6;
                            finalData.get(fullBrickIndex).helix5 = h5;
                            finalData.get(fullBrickIndex).helix6 = h6;
                            finalData.get(fullBrickIndex).strandNumber = finalBoundaryBrickSequencesList.get(index).strandNumber;
                            finalData.get(fullBrickIndex).plateNumber = finalBoundaryBrickSequencesList.get(index).plateNumber;
                            finalData.get(fullBrickIndex).position = finalBoundaryBrickSequencesList.get(index).position;
                            finalData.get(fullBrickIndex).isBoundary = true;
                            finalData.get(i).isMapped = false;

                            //System.out.println("Boundary brick data added " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + " ");
                        }
                        if (index == -1) {
                            //System.out.println("############ Boundary brick not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + "?" + index);
                            finalData.get(i).isMapped = false;
                        }
                        else {
                            System.out.println("*************found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " + h5 + " " + z5 + " " + h6 + " " + z6 + "?" + index);
                        }
                        finalData.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for min y at" + x + " " + y + " " + z2);
                    }
                }
                if (MainFrame.isMaxYBoundaryPlaneEnabled && finalData.get(i).isBottommostHalfBrick &&
                        z5 > 1 &&
                        //half bricks missing in Yonggang Ke's file
                        z5 != MainFrame.depth ) {

                    fullBrickIndex = newIndex[x][y][z5 - 2];
                    if (fullBrickIndex != -1) {
                        h1 = finalData.get(fullBrickIndex).helix1;
                        h2 = h1;
                        h3 = finalData.get(fullBrickIndex).helix3;
                        h4 = h3;
                        z1 = finalData.get(fullBrickIndex).z1;
                        z2 = z1 - 1;
                        z3 = finalData.get(fullBrickIndex).z3;
                        z4 = z3 + 1;

                        int index = -1;

                        for (int j = 0; j < finalBoundaryBrickSequencesList.size(); j++) {
                            if (finalBoundaryBrickSequencesList.get(j).helix1 == h1 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                    finalBoundaryBrickSequencesList.get(j).helix3 == h3 &&
                                    finalBoundaryBrickSequencesList.get(j).helix2 == h2 &&
                                    finalBoundaryBrickSequencesList.get(j).helix4 == h4 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                    finalBoundaryBrickSequencesList.get(j).helix5 == h5 &&
                                    finalBoundaryBrickSequencesList.get(j).helix6 == h6 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                    finalBoundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                index = j;
                            }
                        }
                        if (index != -1) {
                            finalData.get(fullBrickIndex).Domain1 = finalBoundaryBrickSequencesList.get(index).domain1;
                            finalData.get(fullBrickIndex).Domain2 = finalBoundaryBrickSequencesList.get(index).domain2;
                            finalData.get(fullBrickIndex).Domain3 = finalBoundaryBrickSequencesList.get(index).domain3;
                            finalData.get(fullBrickIndex).Domain4 = finalBoundaryBrickSequencesList.get(index).domain4;
                            finalData.get(fullBrickIndex).Domain5 = finalBoundaryBrickSequencesList.get(index).domain5;
                            finalData.get(fullBrickIndex).Domain6 = finalBoundaryBrickSequencesList.get(index).domain6;
                            finalData.get(fullBrickIndex).helix5 = h5;
                            finalData.get(fullBrickIndex).helix6 = h6;
                            finalData.get(fullBrickIndex).strandNumber = finalBoundaryBrickSequencesList.get(index).strandNumber;
                            finalData.get(fullBrickIndex).plateNumber = finalBoundaryBrickSequencesList.get(index).plateNumber;
                            finalData.get(fullBrickIndex).position = finalBoundaryBrickSequencesList.get(index).position;
                            finalData.get(fullBrickIndex).isBoundary = true;
                            finalData.get(i).isMapped = false;

                            //System.out.println("Boundary brick data added " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + " ");
                        }
                        if (index == -1) {
                            System.out.println("############ Boundary brick not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                                    +h5 + " " + z5 + " " + h6 + " " + z6 + "?" + index);
                            finalData.get(i).isMapped = false;
                        } else {
                            //System.out.println("*************found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " + h5 + " " + z5 + " " + h6 + " " + z6 + "?" + index);
                        }
                        finalData.get(i).isLeftmostHalfBrick = false;
                        System.out.println("BoundaryBrick for max y at" + x + " " + y + " " + z2);
                    }
                }
            }
        }

        /*
        int s = finalData.size();
        for(int i=0;i<s;i++){
            if(finalData.get(i).isRemovedAsBoundary == true  || finalData.get(i).isMapped == false){
                finalData.remove(i);
                s--;
            }
        }
        */
    }
}
