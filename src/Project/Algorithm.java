package Project;

import java.util.*;

//Algorithm class to create Hash Map
public abstract class Algorithm {
    protected HashMap<String, Integer> allocMap = new HashMap<>(); // hash map of all procs and their sizes.
    protected ArrayList<PCB> procs; // list of all processes
    protected ArrayList<PCB> holeList; // list of holes
    protected LinkedList<PCB> waitQueue; // queue of waiting processes
    protected PCB curProcess; // current process
    protected int MAX_MEMORY; // memory max gather from driver
    protected double avgHoleSize; // avg hole size in current hole list
    protected int totalHoleSize; // sum of all hole sizes in the current holeList
    protected double freeMemory; // keeps track of available memory to assign processes

    // Algorithm constructor creates algorithm based on proccesses, holelist, max
    // memory, average hole size, total hole size and free memory
    public Algorithm(ArrayList<PCB> procs, ArrayList<PCB> holeList, LinkedList<PCB> waitQueue, int MAX_MEMORY,
            double avgHoleSize, int totalHoleSize, double freeMemory) {
        this.procs = procs;
        this.holeList = holeList;
        this.waitQueue = waitQueue;
        this.MAX_MEMORY = MAX_MEMORY;
        this.avgHoleSize = avgHoleSize;
        this.totalHoleSize = totalHoleSize;
        this.freeMemory = freeMemory;
    }

    // The mind
    public void schedule() {
        ///Make sure procs isn't empty
        while (!procs.isEmpty() || !waitQueue.isEmpty()) {
            //adds all holes to hole lisy
            for (PCB proc : procs) {
                if (proc.getId().equals("Free")) {
                    holeList.add(proc);
                }
                //Add procs to hash map if they are not already in it
                if(!allocMap.containsKey(proc.getId())) {
                    allocMap.put(proc.getId(), proc.getSize());
                }
            }
        }
    }
}
