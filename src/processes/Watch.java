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
        elapsedTime = 0;
    }

    /**
     * Checks current elapsed time.
     */
    private void updateElapsedTime(){
        elapsedTime = (System.nanoTime() - begin);
    }

    /**
     * Checks if the player process init time has passed
     * @return true if the player process init time has passed
     */
    boolean exceededInitTime(){
        updateElapsedTime();
        return elapsedTime > 1000000000;
    }

    /**
     * Checks if the player process move time has passed
     * @return true if the player process move time has passed
     */
    boolean exceededMoveTime(){
        updateElapsedTime();
        return elapsedTime > 500000000;
    }

    /**
     * Waits 2s.
     */
    void waitProcessWakeupTime(){
        initTimer();
        while( elapsedTime < 2000000000){
            waitCheckInterval();
            updateElapsedTime();
        }
    }

    /**
     * Does nothing.
     */
    synchronized void waitCheckInterval(){
        ;
    }
}
