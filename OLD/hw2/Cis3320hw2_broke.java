
package cis3320hw2;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Brooklyn College CISC 3320 Homework 2
 * Programming Problem 3.20
 * @author Michael Rocco
 */




public class Cis3320hw2{

    //declares constant values MIN_PID and MAX_PID 
    //MIN_PID is the lowest possible process ID
    //MIN_PID is the highest possible process ID
    static final int MIN_PID=300;
    static final int MAX_PID=5000;
    
    
    private final static ExecutorService scheduler = Executors.newScheduledThreadPool(100);

    
    
    public static void main(String[] args) throws Exception
    {
        int check=0, i, pidtest, pidtest2;
        BitSet pa1=new BitSet();
        Random rnd = new Random();
                
        PrintStream ps=new PrintStream("output.txt");
        
        System.out.println("hello world");
        
        System.out.println("changes here");
     
        //allocates BitSet
        check=allocate_map(pa1);
        ps.printf("check=%d\n", check);
                
        //the rest of the program won't run if check fails
        if(check==-1)
        {
            ps.printf("BitSet pa1 did not allocate, program terminated");
            ps.close();
            System.exit(-1);
        }

        //threading!
     

        
             final Runnable getPID = new RunnableImpl(pa1);
             
             for(i=0; i<100; i++){
             scheduler.execute(getPID);                 
             }

             pidtest=allocate_pid(pa1);
             System.out.println(pidtest+"test at end");
             

             
             TimeUnit.SECONDS.sleep(1);
             pidtest2=allocate_pid(pa1);
             System.out.println(pidtest2+"test 2 at end");
        
        
        
        
        
        
        
        
        ps.close();       
      
    }
    

    /*allocates space for PID manager
    * sets the bit one higher than maximum PID, lets BitSet class handle the rest
    * BitSet values are false by default
    * return 1 if successful, -1 if failure
    */
    public static int allocate_map(BitSet pa)
    {
             pa.set(MAX_PID+1, true);  
             
             if(pa.get((MAX_PID+1))==true)
                 return(1);
             return(-1);
    }
  
    /* allocate_pid
    goes through possible PIDs until an available one is found
    sets that PID as active and returns its value
    returns -1 if all PIDs are in use
    */
    public static int allocate_pid(BitSet pa)
    {
        //search for open PID
        for(int i=MIN_PID; i<MAX_PID; i++)
        {
            if(pa.get(i)==false)
            {
                pa.set(i, true);
                return(i);
            }
        }
      return(-1);  
    }
    
    /* release_pid
    * frees PID for use
    * just sets supplied BitSet array position to false to indicate it is no longer in use
    */
    
    public static void release_pid(int pid, BitSet pa)
    {
        if(pa.get(pid)==true)
        {
            pa.set(pid, false);
            System.out.println(pid+" sucessful release");
        }
        else
            System.out.println("tried to release unused PID");
    }

    /*
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    */

    private static class RunnableImpl implements Runnable {

        private final BitSet pa1;
        private final Object mutex = new Object();
        
        public RunnableImpl(BitSet pa1) {
            this.pa1 = pa1;
        }

        @Override public void run()
        {

            int pid1;
            synchronized (mutex) {
            pid1=allocate_pid(pa1);
            }
            
            System.out.printf("%d PID\n", pid1);
            
            //sleep random time up to 100 milliseconds
            try {
                Thread.sleep((long)(Math.random() * 100));
            } catch (InterruptedException ex) {
                Logger.getLogger(Cis3320hw2.class.getName()).log(Level.SEVERE, null, ex);
            }
            release_pid(pid1, pa1);
            
        }
    }
}
