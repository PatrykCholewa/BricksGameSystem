package processes;

class DeadlockProtector {

    /**
     * @author Patryk Cholewa
     */

    //DEFINE
    private long maxMillisDelay = 15000; //15s

    private Thread protector;
    private Thread fuse;
    private Boolean deadlockOccurred = false;
    private Long start = System.currentTimeMillis();
    private Long elapsedTime = new Long(0);
    private Long lastSavedElapsedTime = new Long(0);
    private Boolean stopFlag = false;

    DeadlockProtector(){
        ;
    }

    boolean isDeadlockOccurred(){
        return deadlockOccurred;
    }

    /**
     * Initializes a special timer ( deadlockProtector ) that finishes interrupts after much time.
     */
    void init( Thread fuse ) {

        this.fuse = fuse;

        this.protector = new Thread( ()-> {

            while ( elapsedTime < maxMillisDelay && !stopFlag ) {
                if( elapsedTime%1000 == 0 && elapsedTime > lastSavedElapsedTime ){
                    System.out.println( "Deadlock for " + elapsedTime/1000 + "s!" );
                    lastSavedElapsedTime = elapsedTime;
                }
                elapsedTime = System.currentTimeMillis() - start;
            }

            fuseOut();

        });

        protector.setDaemon( true );
        protector.start();

    }

    private void fuseOut(){

        if( !protector.isInterrupted() ){

            if( !stopFlag ) {
                deadlockOccurred = true;
                System.out.println("FUSE OUT");
            }

            fuse.interrupt();

        }

    }

    /**
     * Stops deadlockProtector.
     */
    void stop() {

        stopFlag = true;

    }

}
