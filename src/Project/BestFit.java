package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class BestFit extends Algorithm{
    public BestFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue){
        super("BF",procs,waitQueue);
    }

    @Override
    public void allocateAvailProc() {
        int holeIndexCounter = 0;
        int minSizePart = MAX_MEMORY;
        int bestHoleIndex = -1;
        PCB bestHole = new PCB(-1);
        // Checks for hole with the greatest difference in size compared to avilable
        // proc in wait queue
        for (PCB proc : waitQueue) {
            for (PCB hole : holeList) {
                if (!holeMap.isEmpty() && holeMap.get(hole) - proc.getSize() < minSizePart && holeMap.get(hole) - proc.getSize() >= 0 ) {
                    minSizePart = holeMap.get(hole);
                    bestHole = hole;
                    bestHoleIndex = holeIndexCounter;
                }
                holeIndexCounter += 1;
            }
            // if we have an available hole
            if (bestHole.getSize() != -1) {
                if (bestHole.getSize() == proc.getSize()) {
                    // Set the hole to a new process
                    currentState.set(bestHoleIndex, proc);
                    // Remove proc from holeList
                    holeMap.remove(bestHole);
                    holeList.remove(bestHole);
                }
                // Available hole is not the same size as the proc size
                else {
                    // New size after process takes up some memory
                    int size = bestHole.getSize() - proc.getSize();
                    // Update size of the hole
                    bestHole.setSize(size);
                    holeMap.remove(bestHole);
                    holeMap.put(bestHole, size);
                    // Add new proccess
                    currentState.add(bestHoleIndex + 1, proc);
                }

                 // Add proc to procs
                 procs.add(proc);
                 // Update allocMap to contain new proccess
                 allocMap.put(proc.getId(), proc.getSize());
                 // Remove proc from waitQueue
                 waitQueue.remove(proc);
            }
            bestHole = new PCB(-1);
            proc = new PCB(null, -1, -1);
            holeIndexCounter = 0;
            minSizePart = -1;
            bestHoleIndex = -1;
        }
    }
}
