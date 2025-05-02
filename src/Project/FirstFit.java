package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class FirstFit extends Algorithm{
    public FirstFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue){
        super("FF",procs,waitQueue);
    }

    @Override
    public void allocateAvailProc(){
        int holeIndexCounter = 0;
        //Check for the first available parition big enough for proccess
        for(PCB proc : waitQueue){
            for(PCB hole: holeList){
                if(hole.getSize() >= proc.getSize()){
                    //Available hole is the same size as the proc size
                    if(hole.getSize() == proc.getSize()){
                        //Set the hole to a new process
                        currentState.set(holeIndexCounter,proc);
                        //Remove proc from holeList
                        holeMap.remove(hole);
                        holeList.remove(hole);
                    }
                    //Available hole is not the same size as the proc size
                    else{
                        //New size after process takes up some memory
                        int size = hole.getSize() - proc.getSize();
                        //Update size of the hole
                        hole.setSize(size);
                        holeMap.remove(hole);
                        holeMap.put(hole, size);
                        //Add new proccess
                        currentState.add(holeIndexCounter+1,proc);
                    }
                    //Add proc to procs
                    procs.add(proc);
                    //Update allocMap to contain new proccess
                    allocMap.put(proc.getId(), proc.getSize());
                    //Remove proc from waitQueue
                    waitQueue.remove(proc);

                    holeIndexCounter += 1;
                }
            }
            holeIndexCounter = 0;
        }
    }
}
