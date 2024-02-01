package project.DataBridge.Services;

import project.DataBridge.Controller.Controller;

public class Timer implements Runnable {
    private volatile boolean threadPermission=true;

    private final int min=60*1000;
    private int maxAttempts=3*min;

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts*this.min;
    }

    int calls=0;

    @Override
    public  void run(){
        calls++;
        int attmepts=0;
        while (threadPermission && maxAttempts>attmepts++){
            try{
                // 5min in one attmepts
            Thread.sleep(1000);}catch(Exception e){System.out.println("Timer failed!");}
        }
        Controller.setIsAllowed(false);
//        System.out.println("Closing call: "+calls);

    }
    public void setThreadPermission(boolean val){this.threadPermission=val;}
}
