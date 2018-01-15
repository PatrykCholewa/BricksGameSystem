package enums;

public enum FailureReason {
    NORMAL (0),
    INVALIDMOVE (1),
    OUTOFTIMEMOVE (2),
    CANTSTART (3),
    DEADLOCK (4),
    PROTOCOLERROR (5),
    OUTOFTIMEOK (6);

    private int failureReason;

    FailureReason( int failureReason ){
        this.failureReason = failureReason;
    }

    @Override
    public String toString(){
        switch ( failureReason ){
            case 0 : return "N";
            case 1 : return "M";
            case 2 : return "T";
            case 3 : return "S";
            case 4 : return "D";
            case 5 : return "P";
            case 6 : return "K";
            default: return "-NaN-";
        }

    }

}


