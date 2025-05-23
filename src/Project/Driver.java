package Project;

import java.io.*;
import java.util.*;

public class Driver {

    //Creates a deep copy of the procs list to enure no race conditions
    private static ArrayList<PCB> deepCopyList(ArrayList<PCB> original) {
        ArrayList<PCB> copy = new ArrayList<>();
        for (PCB p : original) {
            copy.add(new PCB(p.getId(), p.getSize(), p.getLifeTime()));
        }
        return copy;
    }

    //Creates a deep copy of the waitQueue to enure no race conditions
    private static LinkedList<PCB> deepCopyQueue(LinkedList<PCB> original) {
        LinkedList<PCB> copy = new LinkedList<>();
        for (PCB p : original) {
            copy.add(new PCB(p.getId(), p.getSize(), p.getLifeTime()));
        }
        return copy;
    }

    public static void main(String[] args) throws Exception {
        // Scanner for user inputs
        Scanner sc = new Scanner(System.in);

        // Getting the file path from user input, storing it, and initializing new file.
        System.out.print("Enter proc file path: ");
        String filePath = sc.nextLine();

        // Scanner for parsing file and String represting each line
        Scanner parseFile = new Scanner(new File(filePath));

        // Variables for temporarily storing values from configuration file
        String[] line;
        String temp;

        // Parse MEMORY_MAX
        line = parseFile.nextLine().split(" ");
        temp = line[2];
        int MEMORY_MAX = Integer.parseInt(temp);

        // Parse PROC_SIZE_MAX
        line = parseFile.nextLine().split(" ");
        temp = line[2];
        int PROC_SIZE_MAX = Integer.parseInt(temp);

        // Parse NUM_PROC
        line = parseFile.nextLine().split(" ");
        temp = line[2];
        int NUM_PROC = Integer.parseInt(temp);

        // Parse MAX_PROC_TIME
        line = parseFile.nextLine().split(" ");
        temp = line[2];
        int MAX_PROC_TIME = Integer.parseInt(temp);

        // ArrayList to hold all procs generated
        ArrayList<PCB> procs = new ArrayList<>();

        // Linked list to hold waiting processes
        LinkedList<PCB> waitQueue = new LinkedList<>();

        // Represents current amount of space being taken up by processes
        int totalProcSize = 0;
        // Randmonly create processes based on values from proc file
        for (int i = 1; i < NUM_PROC + 1; i++) {
            int size = (int) (Math.random() * PROC_SIZE_MAX);
            //Adding delta smoothing to ensure procs do not have a lifetime less than 1.
            int lifeTime = ((int) (Math.random() * MAX_PROC_TIME) / 1000) + 1;
            // Convert value of i to string to be used for id
            String id = "P" + String.valueOf(i);

            // Ceate a new proc with a random size and lifetime
            PCB curProc = new PCB(id, size, lifeTime);

            // Add curProc to procs
            if (size < MEMORY_MAX - totalProcSize) {
                procs.add(curProc);
                totalProcSize += size;
            } else { // Add to waiting queue because memory is full
                waitQueue.add(curProc);
            }
        }

        // Creating hole from remaining memory
        PCB remainingMem = new PCB(MEMORY_MAX - totalProcSize);
        //Add a hole to represent remaining memory
        procs.add(remainingMem);

        //Choose how quickly statements execute
        System.out.print("Desired FPS [1, 10, 100]?: ");
        String choice = sc.nextLine();
        int fps;
        
        //Choose whether or not to compact memory
        System.out.print("Memory Compaction? [Y/N]");
        String compaction = sc.nextLine();

        // Switch case for the different FPS choices
        switch (choice) {
            case "1":
                fps = 1000;
                break;
            case "10":
                fps = 100;
                break;
            case "100":
                fps = 10;
                break;
            default:
                fps = 1000;
                break;
        }

        // Create separate memory and wait queue copies for each algorithm (avoid shared data)
        ArrayList<PCB> procs1 = deepCopyList(procs);
        ArrayList<PCB> procs2 = deepCopyList(procs);
        ArrayList<PCB> procs3 = deepCopyList(procs);

        LinkedList<PCB> wait1 = deepCopyQueue(waitQueue);
        LinkedList<PCB> wait2 = deepCopyQueue(waitQueue);
        LinkedList<PCB> wait3 = deepCopyQueue(waitQueue);

        // Create and configure algorithms as well as array of algorithms
        ArrayList<Algorithm> algos = new ArrayList<>();

        Algorithm FirstFit = new FirstFit(procs1, wait1);
        algos.add(FirstFit);
        Algorithm BestFit = new BestFit(procs2, wait2);
        algos.add(BestFit);
        Algorithm WorstFit = new WorstFit(procs3, wait3);
        algos.add(WorstFit);

        // Assigns the correct inputs from driver for all the algorithms
        for(Algorithm algo : algos){
            algo.setMaxMemory(MEMORY_MAX);
            algo.setFPS(fps);
            algo.setCompaction(compaction);
            algo.initialize();
        }

        // Runs while there are still procs to execute or memory needing compaction
        //depends on compaction input from user
        while (BestFit.getRunnable() || FirstFit.getRunnable() || WorstFit.getRunnable()) {
            //execute and print out the algorithms at every step, contiguously
            FirstFit.schedule();
            BestFit.schedule();
            WorstFit.schedule();
            //Sleep for fps provided by user
            Thread.sleep(fps);
        }

        sc.close();
    }
}
