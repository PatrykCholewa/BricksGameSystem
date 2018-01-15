package processes;

/**
 * @author Paweł Zych
 * @author Patryk Cholewa
 */


class Watch {

    //DEFINE
    private long maxInitTime = 1000000000;
    private long maxMoveTime = 500000000;
    private long bufferTime  = 0;
    private long wakeUpTime  = 0;

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
        return elapsedTime > maxInitTime + bufferTime;
    }

    /**
     * Checks if the player process move time has passed
     * @return true if the player process move time has passed
     */
    boolean exceededMoveTime(){
        updateElapsedTime();
        return elapsedTime > maxMoveTime + bufferTime;
    }

    /**
     * Sets time between waking up process and the actual output to it.
     * @param wakeUpTimeInSeconds time in seconds
     */
    void setWakeUpTime( double wakeUpTimeInSeconds ){
        Double wakeUpTimeInNanos = wakeUpTimeInSeconds;
        wakeUpTimeInNanos *= 1000000000;
        wakeUpTime = wakeUpTimeInNanos.longValue();
    }

    /**
     * Sets time added to each player's allowed move time.
     * @param bufferTimeInSeconds time in seconds
     */
    void setBufferTime( double bufferTimeInSeconds ){
        Double bufferTimeInNanos = bufferTimeInSeconds;
        bufferTimeInNanos *= 1000000000;
        bufferTime = bufferTimeInNanos.longValue();
    }

    /**
     * Waits 2s.
     */
    void waitProcessWakeupTime(){
        initTimer();
        while( elapsedTime < wakeUpTime){
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
