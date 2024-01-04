package project.DataBridge.Services;

import project.DataBridge.Controller.Controller;

public class Timer implements Runnable {
    public volatile boolean threadPermission=true;
    int maxAttempts=3;

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public  void run(){
        int attmepts=0;
        while (threadPermission && maxAttempts>attmepts++){
            try{
                // 5min in one attmepts
            Thread.sleep(300000);}catch(Exception e){System.out.println("Timer failed!");}
        }
        Controller.setIsAllowed(false);

    }
    public void setThreadPermission(boolean val){this.threadPermission=val;}
}
