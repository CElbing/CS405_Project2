package Project;

import java.util.*;

//Algorithm class to create Hash Map
public abstract class Algorithm {
    protected HashMap<String,Integer> sizeMap = new HashMap<>(); //hash map of all procs and their sizes.
    protected ArrayList<PCB> procs; //list of all processes
    protected ArrayList<PCB> holeList; //list of holes 
    protected PCB curProcess; //current process
    protected int MAX_MEMORY; //memory max gather from driver
    protected double avgHoleSize; //avg hole size in current hole list
    protected int totalHoleSize; //sum of all hole sizes in the current holeList
    protected double freeMemory; //keeps track of available memory to assign processes

    //Algorithm constructor creates algorithm based on proccesses, holelist, max memory, average hole size, total hole size and free memory
    public Algorithm(ArrayList<PCB> procs, ArrayList<PCB> holeList, int MAX_MEMORY, double avgHoleSize, int totalHoleSize, double freeMemory){
        this.procs = procs;
        this.holeList = holeList;
        this.MAX_MEMORY = MAX_MEMORY;
        this.avgHoleSize = avgHoleSize;
        this.totalHoleSize = totalHoleSize;
        this.freeMemory = freeMemory;
    }

    //Assign eachs generated process to the hash table
    public void listToHash(ArrayList<PCB> procs){
        for(PCB proc : procs){
            sizeMap.put(proc.getId(), proc.getSize());
        }
    }

    //The mind
    public void schedule(){
        
    }
}
