
package cis3320hw1;

import java.util.*;
import java.io.*;

/**
 * Brooklyn College CISC 3320 Homework 1
 * Programming Problem 3.20
 * @author Michael Rocco
 */


public class Cis3320hw1 {

    //declares constant values MIN_PID and MAX_PID 
    //MIN_PID is the lowest possible process ID
    //MIN_PID is the highest possible process ID
    static final int MIN_PID=300;
    static final int MAX_PID=5000;
    
    public static void main(String[] args) throws Exception
    {
        int check=0, i, first, first2, second, fail, third;
        BitSet pa1=new BitSet();
        
        PrintStream ps=new PrintStream("output.txt");
     
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

        
        //test Allocate PID
        first=allocate_pid(pa1);
        ps.printf("first=%d\n", first);

        //test Allocate PID works on next value
        second=allocate_pid(pa1);
        ps.printf("second=%d\n", second);
        
        //test release_pid
        release_pid(first, pa1);
        ps.printf("first=%b\n", pa1.get(first));
        
        //test allocate_pid will reuse PIDs
        first2=allocate_pid(pa1);
        ps.printf("first2=%d\n", first2);
        
        //set all PIDs as active to test allocate_pid will return -1
        pa1.flip(302, MAX_PID);
        
        fail=allocate_pid(pa1);
        ps.printf("failure test=%d\n", fail);
    
        //tests allocate_pid will work again when new PIDs are available
        for(i=400; i<=410; i++)
            release_pid(i, pa1);
        
        third=allocate_pid(pa1);
        ps.printf("third=%d\n", third);
        
        
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
        pa.set(pid, false);
    }
}
