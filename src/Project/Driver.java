package Project;

import java.io.*;
import java.util.*;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException {
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

        //Represents current amount of space being taken up by processes
        int totalProcSize = 0;
        int curIndex = 2;

        // Randmonly create processes based on values from proc file
        for (int i = 1; i < NUM_PROC + 1; i++) {
            int size = (int) (Math.random() * PROC_SIZE_MAX);
            int lifeTime = (int) (Math.random() * MAX_PROC_TIME) / 1000;
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
        procs.add(remainingMem);

        System.out.println("Which algorithm would you like to use?(FF, BF, WF)");
        String choice = sc.nextLine();



        Algorithm scheduler;
        switch (choice) {
            case "FF":
                scheduler = new FirstFit(procs, waitQueue);
                break;
            case "BF":
                scheduler = new BestFit(procs, waitQueue);
                break;
            case "WF":
                scheduler = new WorstFit(procs, waitQueue);
                break;
            default:
                scheduler = new FirstFit(procs, waitQueue);
                break;
        }

        scheduler.schedule(); 
    }
    
}
