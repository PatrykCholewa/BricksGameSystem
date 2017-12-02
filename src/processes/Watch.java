package processes;

/**
 * @author Paweł Zych
 */


class Watch {
    private long begin;
    private long elapsedTime;

    /**
     * Starts or restarts timer
     */
    void initTimer(){
        begin = System.nanoTime();
    }

    /**
     * Checks if the player process init time has passed
     * @return true if the player process init time has passed
     */
    boolean exceededInitTime(){
        elapsedTime = (System.nanoTime() - begin);
        if (elapsedTime < 1000000000)
            return true;
        else
            return false;
    }

    /**
     * Checks if the player process move time has passed
     * @return true if the player process move time has passed
     */
    boolean exceededMoveTime(){
        elapsedTime = (System.nanoTime() - begin);
        if (elapsedTime < 500000000)
            return true;
        else
            return false;
    }

    /**
     * Waits.
     */

    synchronized void waitCheckInterval(){
        try {
            wait( 10 );
        } catch (InterruptedException e) {
            ;
        }
    }
}
