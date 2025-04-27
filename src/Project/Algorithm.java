package Project;

import java.util.*;

//Algorithm class to create Hash Map
public abstract class Algorithm {
    protected HashMap<String, Integer> allocMap = new HashMap<>(); // hash map of all procs and their sizes.
    protected ArrayList<PCB> procs; // list of all processes
    protected ArrayList<PCB> holeList = new ArrayList<>(); // list of holes
    protected ArrayList<PCB> currentState = new ArrayList<>();
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
    public void schedule(){
        //Initialize lists
        for(PCB proc : procs) {
            //adds all holes to hole list
            if (proc.getId().equals("Free")) {
                holeList.add(proc);
            }
            //Add procs to hash map if they are not already in it
            if(!allocMap.containsKey(proc.getId())) {
                allocMap.put(proc.getId(), proc.getSize());
            }
            //Add all procs to the currentState array
            currentState.add(proc);
        }

        //Print intial lists
        print();

        //Make sure procs isn't empty
        while (!procs.isEmpty()) {
            int procIndex = 0;

            //Update holes and allocMap when process can be executed
            allocateAvailProc();

            //Determining whether a process should decrement lifetime or become hole
            for (PCB proc : currentState){
            //Reduce lifetime by one
                if(proc.getLifeTime() > 0){
                    proc.setLifeTime(proc.getLifeTime() - 1);
                }

                //Process has run through its lifetime, set the process as a new hole
                else if(proc.getLifeTime() == 0){
                    //Remove process from allocMap and replace as hole in procs
                    PCB newHole = new PCB(proc.getSize());
                    //Hole created in the place of proc
                    currentState.set(procIndex, newHole);
                    //add new hole to hole list
                    holeList.add(newHole);
                    //Remove proc from the list of proccesses
                    procs.remove(proc);
                }
                procIndex += 1;
            }

            //Print procs
            print();
        }
    }

    public abstract void allocateAvailProc();
    
    public void print() {
        //Print the processes that fit into the memory
        System.out.println("Current State: ");
        for (PCB proc : currentState) {
           System.out.println(proc);
        }
   
        //Print line to seperate current proccesses from waiting processes
        System.out.println(" ");
   
        // Print the processes that are waiting for memory
        System.out.println("Waiting Procs: ");
        for (PCB proc : waitQueue) {
            System.out.println(proc);
        }

        System.out.println();
    }
}
