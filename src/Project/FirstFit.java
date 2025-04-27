package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class FirstFit extends Algorithm{
    public FirstFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue){
        super("FF",procs,waitQueue);
    }

    @Override
    public PCB pickNextProcess(){
        //Check for the first available parition big enough for proccess
        PCB curProc = null;
        for(PCB proc : waitQueue){
            for(PCB hole: holeList){
                if(hole.getSize() >= proc.getSize()){
                    if(hole.getSize() != proc.getSize()){
                        curProc = proc;
                    }
                }
            }
        }
        return curProc;
    }
}
