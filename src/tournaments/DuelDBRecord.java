package tournaments;

/**
 * @author Patryk Cholewa
 */

class DuelDBRecord {

    final int number;
    final int startingPlayer;
    final int followingPlayer;
    final int winner;

    DuelDBRecord(int number , int startingPlayer , int followingPlayer , int winner ){
        this.number = number;
        this.startingPlayer = startingPlayer;
        this.followingPlayer = followingPlayer;
        this.winner = winner;
    }

}
