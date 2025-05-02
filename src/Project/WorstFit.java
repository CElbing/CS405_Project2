package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class WorstFit extends Algorithm {
    public WorstFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue) {
        super("WF", procs, waitQueue);
    }

    @Override
    public void allocateAvailProc() {
        int holeIndexCounter = 0;
        int maxSizePart = -1;
        int worstHoleIndex = -1;
        PCB worstHole = new PCB(-1);
        // Checks for hole with the greatest difference in size compared to avilable
        // proc in wait queue
        for (PCB proc : waitQueue) {
            for (PCB hole : holeList) {
                if (!holeList.isEmpty() && hole.getSize() - proc.getSize() > maxSizePart) {
                    maxSizePart = hole.getSize() - proc.getSize();
                    worstHole = hole;
                    worstHoleIndex = holeIndexCounter;
                }
                holeIndexCounter += 1;
            }
            // if we have an available hole
            if (worstHole.getSize() != -1) {
                if (worstHole.getSize() == proc.getSize()) {
                    // Set the hole to a new process
                    currentState.set(worstHoleIndex, proc);
                    // Remove proc from holeList
                    holeMap.remove(worstHole);
                    holeList.remove(worstHole);
                }
                // Available hole is not the same size as the proc size
                else {
                    // Update size of the hole
                    worstHole.setSize(maxSizePart);
                    holeMap.remove(worstHole);
                    holeMap.put(worstHole, maxSizePart);
                    // Add new proccess
                    currentState.add(worstHoleIndex + 1, proc);
                }

                 // Add proc to procs
                 procs.add(proc);
                 // Update allocMap to contain new proccess
                 allocMap.put(proc.getId(), proc.getSize());
                 // Remove proc from waitQueue
                 waitQueue.remove(proc);
            }
            worstHole = new PCB(-1);
            holeIndexCounter = 0;
            maxSizePart = -1;
            worstHoleIndex = -1;
        }
    }
}
