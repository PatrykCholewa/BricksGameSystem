package tournaments;

import enums.FailureReason;

/**
 * @author Patryk Cholewa
 */

class ScoreDBRecord implements Comparable<ScoreDBRecord>{

    private String player;
    private Integer winPointsAll;
    private Integer failuresAll;
    private Integer errorFailures;
    private Integer normalWins;
    private String errorList;

    ScoreDBRecord( String player ){
        this.player = player;
        this.winPointsAll = 0;
        this.failuresAll = 0;
        this.errorFailures = 0;
        this.normalWins = 0;
        this.errorList = "";
    }

    String getPlayer() {
        return player;
    }

    void addOnePointToAllWins(){
        winPointsAll++;
    }

    void addOnePointToAllLoses(){
        failuresAll++;
    }

    void addOnePointToNormalWins(){
        normalWins++;
    }

    void addErrorFailure( FailureReason failureReason ){
        errorFailures++;
        errorList += failureReason.toString();
    }

    @Override
    public int compareTo(ScoreDBRecord record) {
        int res = record.winPointsAll - this.winPointsAll;
        if( res == 0 ){
            res = record.normalWins - this.normalWins;
            if( res == 0 ){
                res = this.errorFailures - record.errorFailures;
            }
        }

        return res;

    }

    @Override
    public boolean equals( Object o ){

        try{
            return this.player.equals( ((ScoreDBRecord) o) .player );
        } catch ( ClassCastException e ){
            return false;
        }

    }

    @Override
    public String toString(){
        return player + " \t"
                + winPointsAll + "/" + failuresAll + " \t"
                + normalWins + "/" + errorFailures + "\t"
                + errorList;
    }

}
