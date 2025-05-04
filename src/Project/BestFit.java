package Project;

import java.util.ArrayList;
import java.util.LinkedList;

public class BestFit extends Algorithm{
    public BestFit(ArrayList<PCB> procs, LinkedList<PCB> waitQueue){
        super("BF",procs,waitQueue);
    }

    @Override
    public void allocateAvailProc() {
        // Checks for hole with the greatest difference in size compared to avilable
        // proc in wait queue
        for (int i = 0; i < waitQueue.size(); i++) {
            PCB proc = waitQueue.get(i);

            int holeIndexCounter = 0;
            int minSizePart = MAX_MEMORY;
            int bestHoleIndex = -1;
            PCB bestHole = new PCB(-1);

            for (int j = 0; j < holeList.size(); j++) {
                PCB hole = holeList.get(j);
                if (!holeList.isEmpty() && (((hole.getSize() - proc.getSize()) < minSizePart) && ((hole.getSize() - proc.getSize()) >= 0 ))) {
                    minSizePart = hole.getSize() - proc.getSize();
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
                    holeList.remove(bestHole);
                }
                // Available hole is not the same size as the proc size
                else {
                    // Update size of the hole
                    bestHole.setSize(minSizePart);
                    // Add new proccess
                    currentState.add(bestHoleIndex + 1, proc);
                    //
                }
                 // Add proc to procs
                 procs.add(proc);
                 // Remove proc from waitQueue
                 waitQueue.remove(proc);
            }
        }
    }
}
