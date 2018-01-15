package enums;

public enum FailureReason {
    NORMAL ('N'),
    INVALIDMOVE ('M'),
    OUTOFTIMEMOVE ('T'),
    CANTSTART ('S'),
    DEADLOCK ('D'),
    PROTOCOLERROR ('P'),
    OUTOFTIMEOK ('K');

    private Character failureReason;

    FailureReason( Character failureReason ){
        this.failureReason = failureReason;
    }

    @Override
    public String toString(){
       return failureReason.toString();
    }

}


