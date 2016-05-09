package vmmanager;

import vmsimulation.*;
import java.util.HashMap;

public class PageTable {
	private HashMap<Integer, Integer> pageTable = new HashMap<Integer, Integer>();
	private int pageSize;
	private int tableSize;
	private int frameNumber = -1;

	//Constructor
	public PageTable(int ps, int ts) throws MemoryException {
		this.pageSize = ps;
		this.tableSize = ts;
	}

	//Take in a page number as a parameter and
	//determine what frame it's being put into
	public int update(int pn) {
		frameNumber++; //increment the frame to NOT copy over later
		pageTable.put(pn, frameNumber);
		System.out.println("Bringing page " + pn + " into frame " + frameNumber); //Copying a page to RAM
		return frameNumber;
	}

	//Check if page number is in page table hash map
	public int lookup(int pn) {
		return pageTable.get(pn);
	}

	//Make sure that value at key page number isn't null, since
	//pages with null values are able to be evicted
	public Boolean isValid(int pn) {
		return pageTable.get(pn) != null;
	}

	//Returns the initial tableSize value ts
	public int getSize() {
		return this.tableSize;
	}

	//Alternative to update, a bit simpler
	public void change(int pn, int fn) {
		pageTable.put(pn, fn);
	}

	//Delete a page from the hashmap specified key and its value
	//Reset value
	public void delete(int pn) {
		pageTable.remove(pn);
	}
}
