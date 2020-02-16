package info.curtbinder.pooptime;

public class PoopType {

    public static final int NORMAL = 0;
    public static final int HARD = 1;
    public static final int LOOSE = 2;

    public static final String NORMAL_TEXT = "Normal";
    public static final String HARD_TEXT = "Hard";
    public static final String LOOSE_TEXT = "Loose";
    public static final String NONE_TEXT = "None";


    public static String getTypeStringFromInt(int type) {
        switch (type) {
            case NORMAL:
                return NORMAL_TEXT;
            case HARD:
                return HARD_TEXT;
            case LOOSE:
                return LOOSE_TEXT;
        }
        return NONE_TEXT;
    }

    public static int getTypeIntFromString(String type) {
        int retVal;
        switch(type) {
            default:
            case NORMAL_TEXT:
                retVal = NORMAL;
                break;
            case HARD_TEXT:
                retVal = HARD;
                break;
            case LOOSE_TEXT:
                retVal = LOOSE;
                break;
        }
        return retVal;
    }
}
