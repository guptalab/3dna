/**
 * Created by foram.joshi on 28.08.2014.
 */
public class HelixAndCoordinates {

    public int helix1, helix2, helix3, helix4, helix5, helix6;
    public int zCoordinate1, zCoordinate2, zCoordinate3, zCoordinate4, zCoordinate5, zCoordinate6;
    public int strandNumber, plateNumber, position;
    public boolean isDomain1Replaced, isDomain2Replaced, isDomain3Replaced, isDomain4Replaced;

    public String domain1;
    public String domain2;
    public String domain3;
    public String domain4;
    public String domain5;
    public String domain6;

    public HelixAndCoordinates( int h1, int z1, int h2, int z2, int h3, int z3, int h4, int z4, int h5, int z5, int h6, int z6, int replacement1,
                                int replacement2, int replacement3, int replacement4){
        this.helix1 = h1;
        this.helix2 = h2;
        this.helix3 = h3;
        this.helix4 = h4;
        this.helix5 = h5;
        this.helix6 = h6;
        this.zCoordinate1 = z1;
        this.zCoordinate2 = z2;
        this.zCoordinate3 = z3;
        this.zCoordinate4 = z4;
        this.zCoordinate5 = z5;
        this.zCoordinate6 = z6;
        this.isDomain1Replaced = (replacement1 == 0 ? false : true);
        this.isDomain2Replaced = (replacement2 == 0 ? false : true);
        this.isDomain3Replaced = (replacement3 == 0 ? false : true);
        this.isDomain4Replaced = (replacement4 == 0 ? false : true);
    }

    public void addData (int strand, int plate, int p, String d1, String d2, String d3, String d4, String d5, String d6){
        this.strandNumber = strand;
        this.plateNumber = plate;
        this.position = p;
        this.domain1 = d1;
        this.domain2 = d2;
        this.domain3 = d3;
        this.domain4 = d4;
        this.domain5 = d5;
        this.domain6 = d6;
    }
}
