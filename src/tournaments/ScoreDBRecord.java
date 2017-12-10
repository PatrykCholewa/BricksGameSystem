package tournaments;

class ScoreDBRecord implements Comparable<ScoreDBRecord>{

    private String player;
    private int points;

    ScoreDBRecord( String player ){
        this.player = player;
        this.points = 0;
    }

    String getPlayer() {
        return player;
    }

    int getPoints() {
        return points;
    }

    void addOnePoint(){
        points++;
    }

    @Override
    public int compareTo(ScoreDBRecord record) {
        return record.points - this.points;
    }

    @Override
    public boolean equals( Object o ){

        try{
            return this.player == ((ScoreDBRecord) o).player;
        } catch ( ClassCastException e ){
            return false;
        }

    }

}
