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
        return elapsedTime > 1000000000;
    }

    /**
     * Checks if the player process move time has passed
     * @return true if the player process move time has passed
     */
    boolean exceededMoveTime(){
        elapsedTime = (System.nanoTime() - begin);
        return elapsedTime > 500000000;
    }

    /**
     * Waits.
     */
    synchronized void waitCheckInterval(){
        ;
    }
}
