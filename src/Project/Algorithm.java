package Project;

import java.util.*;

//Algorithm class to create Hash Map
public abstract class Algorithm {
    protected HashMap<String, Integer> allocMap = new HashMap<>(); // hash map of all procs and their sizes.
    protected ArrayList<PCB> procs; // list of all processes
    protected ArrayList<PCB> holeList; // list of holes
    protected LinkedList<PCB> waitQueue; // queue of waiting processes
    protected PCB availProc; // current process
    protected int MAX_MEMORY; // memory max gather from driver
    protected double avgHoleSize; // avg hole size in current hole list
    protected int totalHoleSize; // sum of all hole sizes in the current holeList
    protected double freeMemory; // keeps track of available memory to assign processes

    // Algorithm constructor creates algorithm based on proccesses, holelist, max
    // memory, average hole size, total hole size and free memory
    public Algorithm(String name, ArrayList<PCB> procs, LinkedList<PCB> waitQueue) {
        this.procs = procs;
        this.waitQueue = waitQueue;
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

            //availProc represents a process is available that can fit inside a hole
            availProc = pickNextProcess();

            //When there is an available process, allocate it to the appropriate hole
            if(availProc != null){
                for(PCB proc : waitQueue){
                    for(PCB hole: holeList){
                        if(hole.getSize() >= proc.getSize()){
                            //Available hole is the same size as the proc size
                            if(hole.getSize() == proc.getSize()){
                                allocMap.put(proc.getId(), proc.getSize());
                                holeList.remove(hole);
                            }
                            //Available hole is not the same size as the proc size
                            else{
                                int size = hole.getSize() - proc.getSize();
                                PCB remainingHole = new PCB(hole.getIndex(), size);
                                allocMap.put(proc.getId(), proc.getSize());
                                hole = remainingHole;
                            }
                        }
                    }
                }
            }
        }


    }

    public abstract PCB pickNextProcess();

    public void print() {
        //Print the processes that fit into the memory
        System.out.println("Current Procs: ");
        for (PCB proc : procs) {
           System.out.println(proc);
        }
   
        //Print line to seperate current proccesses from waiting processes
        System.out.println(" ");
   
        // Print the processes that are waiting for memory
        System.out.println("Wait Queue: ");
        for (PCB proc : waitQueue) {
            System.out.println(proc);
        }

        System.out.println();
    }
}
