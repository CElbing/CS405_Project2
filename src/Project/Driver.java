package Project;

import java.io.*;
import java.util.*;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException{
        //Scanner for user inputs
		Scanner sc = new Scanner(System.in);
        
        //Getting the file path from user input, storing it, and initializing new file.
		System.out.print("Enter proc file path: ");
        String filePath = sc.nextLine();
        
        //Scanner for parsing file and String represting each line
        Scanner parseFile = new Scanner(new File(filePath));
        
        //Variables for temporarily storing values from configuration file
        String[] line;
        String temp;
        
        //Parse MEMORY_MAX 
        line = parseFile.nextLine().split(" ");
        temp = line[2];
        int MEMORY_MAX = Integer.parseInt(temp);

        //Parse PROC_SIZE_MAX
        line = parseFile.nextLine().split(" ");
        temp = line[2];
        int PROC_SIZE_MAX = Integer.parseInt(temp);

        //Parse NUM_PROC
        line = parseFile.nextLine().split(" ");
        temp = line[2];
        int NUM_PROC = Integer.parseInt(temp);
        
        //Parse MAX_PROC_TIME
        line = parseFile.nextLine().split(" ");
        temp = line[2];
        int MAX_PROC_TIME = Integer.parseInt(temp);
	}
}
