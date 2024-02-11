package project.DataBridge.Services;

import project.DataBridge.Controller.Controller;

public class Timer implements Runnable {
    private volatile boolean threadPermission=true;

//    private final int min=60;
    private int mins=1;

    public void setMins(int mins) {
        this.mins = mins*60;
    }

//    int calls=0;

    @Override
    public  void run(){
//        calls++;
        int min=0;
        while (threadPermission && mins>min++){
            try{
                // 5min in one attmepts
            Thread.sleep(1000);}catch(Exception e){System.out.println("Timer failed!");}
        }
        Controller.setIsAllowed(false);
//        System.out.println("Closing call: "+calls);

    }
    public void setThreadPermission(boolean val){this.threadPermission=val;}
}
