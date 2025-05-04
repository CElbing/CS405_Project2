package Project;

import java.util.*;

//Algorithm class to create Hash Map
public abstract class Algorithm {

    protected ArrayList<PCB> procs; // list of all processes
    protected ArrayList<PCB> holeList = new ArrayList<>(); // list of holes
    protected ArrayList<PCB> currentState = new ArrayList<>();
    protected LinkedList<PCB> waitQueue; // queue of waiting processes
    protected PCB availProc; // current process
    protected int MAX_MEMORY; // memory max gather from driver
    protected double avgHoleSize; // avg hole size in current hole list
    protected int totalHoleSize; // sum of all hole sizes in the current holeList
    protected double freeMemory; // keeps track of available memory to assign processes
    protected boolean runnable = true; // are the procs finished
    protected String name; // type of algorithm
    protected int fps; // speed of execution
    protected boolean compaction = false; // boolean for whether or not compaction is required
    protected final Object memoryLock = new Object(); // memoryLock to prevent race conditions

    // Algorithm constructor creates algorithm based on proccesses, holelist, max
    // memory, average hole size, total hole size and free memory
    public Algorithm(String name, ArrayList<PCB> procs, LinkedList<PCB> waitQueue) {
        this.procs = procs;
        this.waitQueue = waitQueue;
        this.name = name;
    }

    // Sets the max memory
    public void setMaxMemory(int i) {
        this.MAX_MEMORY = i;
    }

    // Gets the max memory
    public int getMaxMemory() {
        return MAX_MEMORY;
    }

    // Getter for runnable
    public boolean getRunnable() {
        return runnable;
    }

    // Setter for FPS
    public void setFPS(int i) {
        this.fps = i;
    }

    // Setter for compaction, if Y then we want compaction
    public void setCompaction(String x) {
        if (x.equalsIgnoreCase("y")) {
            compaction = true;
        }
    }

    // intitialize the algorithms to create the lists
    public void initialize() throws InterruptedException {
        // lock to prevent race conditions
        synchronized (memoryLock) {
            // loop through all procs from driver
            for (int i = 0; i < procs.size(); i++) {
                PCB proc = procs.get(i);
                // Add all procs and holes to the currentState array
                currentState.add(proc);
                // adds all holes to hole list
                if (proc.getId().equals("Free")) {
                    holeList.add(proc);
                }
            }
            // Removes all holes from the procs list
            for (int i = 0; i < holeList.size(); i++) {
                PCB hole = holeList.get(i);
                procs.remove(hole);
            }
            // Print intial lists
            print();
        }
    }

    // The mind, each alogrithm runs this to decrement the lifetime of processes and
    // manage holes
    public void schedule() throws InterruptedException {
        // lock to prevent race conditions
        synchronized (memoryLock) {
            int procIndex = 0;

            // Determining whether a process should decrement lifetime or become hole
            for (int i = 0; i < currentState.size(); i++) {
                PCB proc = currentState.get(i);
                // Reduce lifetime by one
                if (proc.getLifeTime() > 0) {
                    proc.setLifeTime(proc.getLifeTime() - 1);
                } // Process has run through its lifetime, set the process as a new hole
                else if (proc.getLifeTime() == 0 && procs.contains(proc)) {
                    // Remove process from allocMap and replace as hole in procs
                    PCB newHole = new PCB(proc.getSize());
                    // Hole created in the place of proc
                    currentState.set(procIndex, newHole);
                    // add new hole to hole list
                    holeList.add(newHole);
                    // Remove proc from the list of proccesses
                    procs.remove(proc);
                }
                // Increment index
                procIndex += 1;
            }

            // Memory compaction
            if (compaction) {
                if (currentState.size() > 1) {
                    for (int i = 0; i < currentState.size() - 1; i++) {
                        PCB curProc = currentState.get(i);
                        PCB nextProc = currentState.get(i + 1);
                        // Check if procs next to eachother are holes
                        if (curProc.getId().equals("Free") && nextProc.getId().equals("Free")) {
                            // Merge the sizes of the two procs
                            PCB mergedProc = new PCB(curProc.getSize() + nextProc.getSize());
                            // Set the size of the current hole to the sum of both hole sizes
                            currentState.set(i, mergedProc);
                            // Remove the both holes from holeList and remove the next hole (nextProc) from
                            // currentState
                            currentState.remove(nextProc);
                            holeList.remove(nextProc);
                            holeList.remove(curProc);
                            // Update the hole size and add the merged proc to holeList
                            curProc.setSize(mergedProc.getSize());
                            holeList.add(mergedProc);
                        }
                    }
                }
            }

            // Update holes and allocMap when process can be executed
            allocateAvailProc();

            // Calculate the current totalHoleSize
            for (int i = 0; i < holeList.size(); i++) {
                PCB hole = holeList.get(i);
                totalHoleSize += hole.getSize();
            }

            // When there is one hole with the MAX_MEMORY size end the program. Parameters
            // vary based on use of compaction
            if (compaction) {
                if (totalHoleSize == MAX_MEMORY && holeList.size() == 1) {
                    runnable = false;
                }
            } else {
                if (totalHoleSize == MAX_MEMORY) {
                    runnable = false;
                }
            }

            // Print procs
            print();

            //If there are any procs that didn't get assigned due to no available memory
            if (runnable == false && !waitQueue.isEmpty()) {
                System.out.println("----------SYSTEM-ERROR----------");
                for (PCB proc : waitQueue) {
                    System.out.println("PROCESS: " + proc.getId() + " FAILED TO OBTAIN A PARTITION");
                }
                System.out.println();
                System.out.println("Recommendations: use the memory compaction feature.");
                System.out.println("----------SYSTEM-ERROR----------");
                System.out.println();
            }
            // Reset the total hole size for the next itteration
            totalHoleSize = 0;
        }
    }

    // abstract method defined by BestFit, WorstFit, and FirstFit respectively
    public abstract void allocateAvailProc();

    // prints the current state of execution
    public void print() throws InterruptedException {
        synchronized (memoryLock) {
            System.out.println("========================");
            System.out.println(name);
            System.out.println();

            // Print the processes that fit into the memory
            if (!currentState.isEmpty()) {
                System.out.println("Current State: ");
                for (int i = 0; i < currentState.size(); i++) {
                    PCB proc = currentState.get(i);
                    System.out.print(proc);
                }
            } else {
                System.out.println("Current State: idle");
            }

            // Print line to seperate current proccesses from waiting processes
            System.out.println(" ");

            // Print the processes that are waiting for memory
            if (!waitQueue.isEmpty()) {
                System.out.println("Waiting Procs: ");
                for (int i = 0; i < waitQueue.size(); i++) {
                    PCB proc = waitQueue.get(i);
                    System.out.print(proc);
                }
            } else {
                System.out.println("Waiting Procs: idle");
            }

            System.out.println(" ");

            // Print the number of holes
            System.out.println("Holes: " + holeList.size());

            // Total size of current list of hole
            System.out.println("Total size of holes: " + totalHoleSize);

            // Average size of current list of holes
            if (!holeList.isEmpty()) {
                System.out.println("Average size of holes: " + (totalHoleSize / holeList.size()));
            } else {
                System.out.println("Average size of holes: 0");
            }
            // Percentage of free memory compared to total memory
            int percentFree = (int) (((double) (totalHoleSize) / (double) (MAX_MEMORY)) * 100);
            System.out.println("Percentage of free memory: " + percentFree + "%");
            System.out.println("========================");
            System.out.println();
        }
    }
}
