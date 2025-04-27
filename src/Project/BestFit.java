package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class BestFit extends Algorithm{
    public BestFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue){
        super("BF",procs,waitQueue);
    }

    @Override
    public void pickNextProcess(){
        
    }
}
