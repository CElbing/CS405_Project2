package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class WorstFit extends Algorithm{
    public WorstFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue){
        super("WF",procs,waitQueue);
    }
}
