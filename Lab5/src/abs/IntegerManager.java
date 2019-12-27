package abs;

class IntegerManager {

    private static final Integer NEGATIVE_ONE = -1;

    private static final Integer ZERO = 0;

    private static final Integer ONE = 1;

    private static final Integer TWO = 2;

    private static final Integer THREE = 3;

    private static final Integer FOUR = 4;

    private static final Integer FIVE = 5;

    private static final Integer SIX = 6;

    private static final Integer SEVEN = 7;

    private static final Integer EIGHT = 8;

    private static final Integer NINE = 9;

    public static Integer get(int num) {
        switch (num) {
            case 0:
                return ZERO;
            case 1:
                return ONE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return FIVE;
            case 6:
                return SIX;
            case 7:
                return SEVEN;
            case 8:
                return EIGHT;
            case 9:
                return NINE;
            default:
                return NEGATIVE_ONE;
        }
    }
}
