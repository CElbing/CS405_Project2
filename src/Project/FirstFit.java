package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class FirstFit extends Algorithm {

    public FirstFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue) {
        super("FF", procs, waitQueue);
    }

    @Override
    public void allocateAvailProc() {
        int holeIndexCounter = 0;
        // Check for the first available parition big enough for proccess
        for (int i = 0; i < waitQueue.size(); i++) {
            PCB proc = waitQueue.get(i);
            for (int j = 0; j < holeList.size(); j++) {
                PCB hole = holeList.get(j);
                if (hole.getSize() >= proc.getSize()) {
                    // Available hole is the same size as the proc size
                    if (hole.getSize() == proc.getSize()) {
                        // Set the hole to a new process
                        currentState.set(holeIndexCounter, proc);
                        // Remove proc from holeList
                        holeList.remove(hole);
                    } // Available hole is not the same size as the proc size
                    else {
                        // New size after process takes up some memory
                        int size = hole.getSize() - proc.getSize();
                        // Update size of the hole
                        hole.setSize(size);
                        // Add new proccess
                        currentState.add(proc);
                    }
                    holeIndexCounter += 1;
                }
                // Add proc to procs
                procs.add(proc);
                // Remove proc from waitQueue
                waitQueue.remove(proc);
            }
            holeIndexCounter = 0;
        }
    }
}
