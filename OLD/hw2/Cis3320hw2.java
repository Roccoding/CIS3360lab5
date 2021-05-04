
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

//private version, moves bitset thing to protected internal class


public class Cis3320hw2{

    //declares constant values MIN_PID and MAX_PID 
    //MIN_PID is the lowest possible process ID
    //MIN_PID is the highest possible process ID
    static final int MIN_PID=300;
    static final int MAX_PID=5000;
    
    
    //starts thread scheduler from concurrent library's ExecutorService with 100 threads
    private final static ExecutorService scheduler = Executors.newScheduledThreadPool(100);

    
    
    public static void main(String[] args) throws Exception
    {
        int check=0, i, pidtest, pidtest2;

        //allocates BitSet
        check=allocate_map();
        System.out.printf("allocate check=%d\n", check);
                
        //the rest of the program won't run if check fails
        if(check==-1)
        {
            System.out.printf("BitSet did not allocate, program terminated");
            System.exit(-1);
        }



        //uses Runnable for multithreading
        //netbeans doesn't like anonymous inner classes so see RunnableImp1
        final Runnable getPID = new RunnableImpl();
             
        //makes 100 threads that each get a PID, wait a random time, and release it
        for(i=0; i<100; i++){
             scheduler.execute(getPID);                 
        }

        

        //allocates a pid in main
        pidtest=allocate_pid();
        System.out.println(pidtest+" test in main while threads are running");
             

        //wait 101 ms for all threads to finish, allocate one more PID     
        TimeUnit.MILLISECONDS.sleep(101);
        
        System.out.println(pidtest+" ID from main still allocated = "+Bits.et.get(pidtest));
        release_pid(pidtest);
        
        pidtest2=allocate_pid();
        System.out.println(pidtest2+" test all PIDs free after threads ran");
        
      
        
        
        
        //gets ready to end program
        scheduler.shutdown();       
      
      
    }
    
    //protected class that holds the actual BitSet that PIDs are drawn from
    protected static class Bits{
        static BitSet et=new BitSet();
    }

    /*allocates space for PID manager
    * sets the bit one higher than maximum PID, lets BitSet class handle the rest
    * BitSet values are false by default
    * return 1 if successful, -1 if failure
    */
    public static int allocate_map()
    {
             Bits.et.set(MAX_PID+1, true);  
             
             if(Bits.et.get((MAX_PID+1))==true)
                 return(1);
             return(-1);
    }
  
    /* allocate_pid
    goes through possible PIDs until an available one is found
    sets that PID as active and returns its value
    returns -1 if all PIDs are in use
    */
    public static synchronized int allocate_pid()
    {
        //search for open PID
        for(int i=MIN_PID; i<MAX_PID; i++)
        {
            if(Bits.et.get(i)==false)
            {
                Bits.et.set(i, true);
                return(i);
            }
        }
      return(-1);  
    }
    
    /* release_pid
    * frees PID for use
    * just sets supplied BitSet array position to false to indicate it is no longer in use
    * tests to make sure no attempts to release unused PIDs are made, prints to console if it happens
    */  
    public static synchronized void release_pid(int pid)
    {
        if(Bits.et.get(pid)==true)
        {
            Bits.et.set(pid, false);
        }
        else
            System.out.println("tried to release unused PID");
    }



    /* RunnableImp1
    *  separate threads that get a PID, wait up to 100 ms, then release it
    */
    private static class RunnableImpl implements Runnable {

        @Override public void run()
        {
            int pid1;
            
            pid1=allocate_pid();
            System.out.println(pid1+" PID allocated");
      
            //sleep random time up to 100 milliseconds
            try {
                Thread.sleep((long)(Math.random() * 100));
            } catch (InterruptedException ex) {
                Logger.getLogger(Cis3320hw2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            release_pid(pid1);
            System.out.printf("%d PID RELEASED\n", pid1);          
        }
    }
       
}
    

