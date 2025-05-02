package Project;

import java.util.*;

//Algorithm class to create Hash Map
public abstract class Algorithm {
    protected HashMap<String, Integer> allocMap = new HashMap<>(); // hash map of all procs and their sizes.
    protected HashMap<PCB, Integer> holeMap = new HashMap<>(); // hash map of all holes and their sizes
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
    public Algorithm(String name, ArrayList<PCB> procs, LinkedList<PCB> waitQueue){
        this.procs = procs;
        this.waitQueue = waitQueue;
    }

    // Sets the max memory
    public void setMaxMemory(int i) {
        this.MAX_MEMORY = i;
    }

    // Gets the max memory
    public int getMaxMemory() {
        return MAX_MEMORY;
    }

    // The mind
    public void schedule() {
        // Initialize lists
        for (PCB proc : procs) {
            // Add all procs and holes to the currentState array
            currentState.add(proc);
            // adds all holes to hole list
            if (proc.getId().equals("Free")) {
                holeList.add(proc);
                holeMap.put(proc, proc.getSize());
            }
            // Add procs to hash map
            else {
                allocMap.put(proc.getId(), proc.getSize());
            }
        }

        // Cannot remove in the middle of iteration of procs so it is done here
        // Removes all holes from the procs list
        for (PCB hole : holeList) {
            procs.remove(hole);
        }
        // Print intial lists
        print();

        // Make sure procs isn't empty
        while (!procs.isEmpty()) {
            int procIndex = 0;

            // Determining whether a process should decrement lifetime or become hole
            for (PCB proc : currentState) {
                // Reduce lifetime by one
                if (proc.getLifeTime() > 0) {
                    proc.setLifeTime(proc.getLifeTime() - 1);
                }

                // Process has run through its lifetime, set the process as a new hole
                else if (proc.getLifeTime() == 0 && procs.contains(proc)) {
                    // Remove process from allocMap and replace as hole in procs
                    PCB newHole = new PCB(proc.getIndex(), proc.getSize());

                    // Hole created in the place of proc
                    currentState.set(procIndex, newHole);
                    // add new hole to hole list
                    holeList.add(newHole);
                    holeMap.put(proc, proc.getSize());
                    // Remove proc from the list of proccesses
                    procs.remove(proc);
                }
                // Increment index
                procIndex += 1;
            }

            // Memory compaction
            if (currentState.size() > 1) {
                for (int i = 0; i < currentState.size() - 1; i++) {
                    PCB curProc = currentState.get(i);
                    PCB nextProc = currentState.get(i + 1);
                    // Check if procs next to eachother are holes
                    if (curProc.getId().equals("Free") && nextProc.getId().equals("Free")) {
                        // Merge the sizes of the two procs
                        PCB mergedProc = new PCB(curProc.getIndex(),curProc.getSize() + nextProc.getSize());
                        // Set the size of the current hole to the sum of both hole sizes
                        currentState.set(i, mergedProc);
                        // Update the hole size in holeList
                        holeList.get(curProc.getIndex()).setSize(mergedProc.getSize());
                        // Remove the next hole from current state, holeList, and holeMap
                        currentState.remove(nextProc);
                        holeList.remove(nextProc);
                        holeMap.remove(nextProc);
                    }

                }
            }
            // Update holes and allocMap when process can be executed
            allocateAvailProc();

            // Print procs
            print();

            //Thread.sleep(1000);
        }
    }

    // absctract method defined by BestFit, WorstFit, and FirstFit respectively
    public abstract void allocateAvailProc();

    public void print() {
        // Print the processes that fit into the memory
        System.out.println("Current State: ");
        for (PCB proc : currentState) {
            System.out.println(proc);
        }

        // Print line to seperate current proccesses from waiting processes
        System.out.println(" ");

        // Print the processes that are waiting for memory
        System.out.println("Waiting Procs: ");
        for (PCB proc : waitQueue) {
            System.out.println(proc);
        }

        System.out.println();

        // Print the number of holes
        System.out.println("Holes: " + holeList.size());

        System.out.println();
    
        // Total size of current list of holes
        int count = 0;
        totalHoleSize = 0;
        for (PCB hole : holeList) {
            System.out.println(hole.getSize());
            totalHoleSize += hole.getSize();
            count += 1;
        }
        System.out.println("Total size of holes: " + totalHoleSize);

        System.out.println();

        // Average size of current list of holes
        System.out.println("Average size of holes: " + (totalHoleSize / count));

        System.out.println();

        // Percentage of free memory compared to total memory
        double percentFree = ((double)(totalHoleSize) / (double)(MAX_MEMORY)) * 100;
        System.out.println("Percentage of free memory: " + percentFree + "%");

    }
}
