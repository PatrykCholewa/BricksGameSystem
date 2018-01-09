package processes;

class DeadlockProtector {

    //DEFINE
    private long maxMillisDelay = 15000; //15s

    private Thread protector;
    private Thread fuse;
    private Boolean deadlockOccurred;
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

        deadlockOccurred = false;

        this.fuse = fuse;


        this.protector = new Thread( ()-> {

            while ( elapsedTime < maxMillisDelay && !stopFlag ) {
                if( elapsedTime%1000 == 0 && elapsedTime > lastSavedElapsedTime ){
                    System.out.println( "Deadlock for " + elapsedTime/1000 + "s!" );
                    lastSavedElapsedTime = elapsedTime;
                }
                elapsedTime = System.currentTimeMillis() - start;
            }

            if( !stopFlag ){
                fuseOut();
            }

        });

        protector.setDaemon( true );
        protector.start();

    }

    private void fuseOut(){

        if( !protector.isInterrupted() ){

            deadlockOccurred = true;
            System.out.println( "FUSE OUT" );
            fuse.interrupt();

           stop();

        }

    }

    /**
     * Stops deadlockProtector, so it won't interrupt the fuse.
     */
    void stop() {

        stopFlag = true;
        fuse = null;
        try {
            protector.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
