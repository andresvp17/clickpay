package lib;

import java.util.List;

public class Utility {
    public static <T extends Identifiable> int getMaxID(List<T> utilityList) {
        if (utilityList == null || utilityList.isEmpty()) {
            return 0;
        }

        int currentMaxID = utilityList.get(0).getID();

        for (int i = 0; i < utilityList.size(); i++) {
            if (utilityList.get(i).getID() > currentMaxID) {
                currentMaxID = utilityList.get(i).getID();
            }
        }

        return currentMaxID;
    }
}
