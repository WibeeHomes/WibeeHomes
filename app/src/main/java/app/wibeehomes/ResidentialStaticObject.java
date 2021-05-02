package app.wibeehomes;

import java.util.ArrayList;

public class ResidentialStaticObject {
    private static ArrayList<ResidentialFacilities> residentialFacilities = new ArrayList<ResidentialFacilities>();
    public static ArrayList<ResidentialFacilities> getResidentialFacilities(){
        return residentialFacilities;
    }
    public static void addResidentialFacilities(ResidentialFacilities resident){
        residentialFacilities.add(resident);
    }
    public static void clearResidentialFacilities(){
        residentialFacilities.clear();
    }
}
