package Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContigousMemoryAllocator {
	private int size;    // maximum memory size in bytes (B)
	private Map<String, Partition> allocMap;   // map process to partition
	private List<Partition> partList;          // list of memory partitions
	// constructor
	public ContigousMemoryAllocator(int size) {
		this.size = size;
		this.allocMap = new HashMap<>();
		this.partList = new ArrayList<>();
		//add the first hole, which is the whole memory at start-up
		this.partList.add(new Partition(0, size)); 
	}

	// Get the total allocated memory size
	private int allocated_memory() {
		int allocated = 0;
		for(Partition part : partList)	
			if(!part.isFree())	allocated += part.getLength();
		return allocated;
	}

	// Get the total free memory size
	private int free_memory() {
		int free = 0;
		for(Partition part : partList)	
			if(part.isFree())	free += part.getLength();
		return free;
	}

	// Sort the list of partitions in ascending order of base addresses
	private void order_partitions() {
		Collections.sort(partList, Comparator.comparingInt(Partition::getBase));
	}

	// prints the allocation map in ascending order of base addresses
	public void print_status() {
		//TODO: add code below
		order_partitions(); //reorder in ascending order of their base addresses
		System.out.println("Allocated: " + allocated_memory() + " B, Free: " + free_memory() + " B");
		//print the partition list
		for(Partition part : partList) {
			System.out.printf("Address [%d:%d] %s (%d B)\n",
					part.getBase(), part.getBase()+part.getLength()-1,
					part.isFree() ? "Free" : part.getProcess(), part.getLength());
		}
	}

	// Implements the first fit memory allocation algorithm
	public int first_fit(String process, int size) {
		//TODO: add code below
		//return if the process has been allocated before
		if(allocMap.containsKey(process)) 
			return -1;
		int index = 0, alloc = -1;
		while(index < partList.size()) {
			Partition part = partList.get(index);
			if(part.isFree() && part.getLength() >= size) 
			{
				Partition newPart = new Partition(part.getBase(), size);
				newPart.setFree(false);
				newPart.setProcess(process);
				partList.add(index, newPart);
				allocMap.put(process, newPart);
				//fix the previous free memory hole
				part.setBase(part.getBase()+size);
				part.setLength(part.getLength()-size);
				if(part.getLength() == 0) //remove memory hole with 0 size
					partList.remove(part);
				alloc = size;
				break; //find and allocate the memory partition to the requested process
			} else {
				index++;
			}
		}
		return alloc; //-1 means failed allocation > 0 means allocation
	}

	// Release the allocated memory of a process
	public int release(String process) {
		int free = -1; // denote how much memory to be deallocated
		for(Partition part : partList) {
			if(!part.isFree() && part.getProcess().equalsIgnoreCase(process)) {
				part.setFree(true);  //make this partition free or available
				part.setProcess(null);  // clear attached process
				free = part.getLength();
				allocMap.remove(process);  // clear the mapping
				break;
			}
		}
		merge_holes();  //merge adjacent free memory holes
		return free; //-1 means failed to deallocate
	}      

	// Procedure to merge adjacent holes
	private void merge_holes() {
		order_partitions(); //sort the partition list in ascending order of base addresses
		int i = 0;
		while(i < partList.size()) {
			Partition part = partList.get(i);
			if(part.isFree()) //try to merge with the next one
			{
				int j = i+1; // the next adjacent partition
				int endI = part.getBase()+part.getLength();
				while(j < partList.size() && partList.get(j).isFree()) {
					//merge partition i an j together
					if(endI == partList.get(j).getBase()) {
						part.setLength(part.getLength()+partList.get(j).getLength());
						partList.remove(j); //remove partition from list
					} else {
						break;
					}
				}
				i++; // try with the next partition
			} else {
				i++; //try with the next partition if part i is not free
			}
		}
	}
}
