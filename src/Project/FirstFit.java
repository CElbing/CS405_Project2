package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class FirstFit extends Algorithm{
    public FirstFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue){
        super("FF",procs,waitQueue);
    }

    @Override
    public void allocateAvailProc(){
        //Check for the first available parition big enough for proccess
        for(PCB proc : waitQueue){
            for(PCB hole: holeList){
                if(hole.getSize() >= proc.getSize()){
                    //Available hole is the same size as the proc size
                    if(hole.getSize() == proc.getSize()){
                        allocMap.put(proc.getId(), proc.getSize());
                        procs.add(proc);
                        
                        holeList.remove(hole);
                        waitQueue.remove(proc);
                    }
                    //Available hole is not the same size as the proc size
                    else{
                        //New size after process takes up some memory
                        int size = hole.getSize() - proc.getSize();
                        //Update size of the hole
                        hole.setSize(size);
                        //Update allocMap to contain new proccess
                        allocMap.put(proc.getId(), proc.getSize());
                        waitQueue.remove(proc);
                    }
                }
            }
        }
    }
}
