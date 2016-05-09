package vmmanager;

import vmsimulation.*;

public class VirtualMemoryManagerV1 {

	MainMemory memory;
	int bitIndex;
	PageTable pageTable;
	BackingStore disk;
	int pageSize;
	private int frameNumber = 0;
	int pageFaults = 0;
	int transBytes = 0;

	// Constructor
	public VirtualMemoryManagerV1(MainMemory memory, BackingStore disk, Integer pageSize) throws MemoryException {
		this.memory = memory;
		this.disk = disk;
		this.pageSize = pageSize;
		bitIndex = (int)(Math.log(memory.size()) / Math.log(2) - 1);
		pageTable = new PageTable(pageSize, (memory.size()/pageSize));
		//System
		// TO IMPLEMENT: V0, V1, V2, V3, V4
	}

  	/**
  	 * Writes a value to a byte at a given address -
  	 * To be implemented in versions V0 and above.
  	 *
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public void writeByte(Integer fourByteBinaryString, Byte value) throws MemoryException {
		int pageNumber = fourByteBinaryString/pageSize; //determine the page number
		int offset = fourByteBinaryString % pageSize; //offset is the remainder of page number calc
		byte[] diskData = disk.readPage(pageNumber); //read all values in particular page

		if (pageTable.isValid(pageNumber) == false) {
			frameNumber = pageTable.update(pageNumber); //The frame for which values will be copied to
			pageFaults++;
			for (int i = 0; i < pageSize; i++) { //iterate through values of current page
				memory.writeByte(frameNumber*pageSize + i, diskData[i]); //input all values into ram
			}
		} else {
			frameNumber = pageTable.lookup(pageNumber);
			System.out.println("Page " + pageNumber + " is in memory");
		}
		memory.writeByte(frameNumber*pageSize + offset, value);
		System.out.println("RAM: " + BitwiseToolbox.getBitString((frameNumber*pageSize + offset), bitIndex) + " <-- " + value);
  	}

  	/**
  	 * Reads a byte from a given address -
  	 * To be implemented in versions V0 and above.
  	 *
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public Byte readByte(Integer fourByteBinaryString) throws MemoryException {
		byte value = 0;
		int pageNumber = fourByteBinaryString/pageSize; //determine the page number
		int offset = fourByteBinaryString % pageSize; //offset is the remainder of page number calc
		byte[] diskData = disk.readPage(pageNumber); //read all values in particular page


		if (pageTable.isValid(pageNumber) == false) {
			frameNumber = pageTable.update(pageNumber); //The frame for which values will be copied to
			pageFaults++;
			for (int i = 0; i < pageSize; i++) { //iterate through values of current page
				memory.writeByte(frameNumber*pageSize + i, diskData[i]); //input all values into ram
			}
		} else {
			frameNumber = pageTable.lookup(pageNumber);
			System.out.println("Page " + pageNumber + " is in memory");
		}
		value = memory.readByte(frameNumber*pageSize + offset);
		System.out.println("RAM: " + BitwiseToolbox.getBitString((frameNumber*pageSize + offset), bitIndex) + " --> " + value);
  		return value;
  	}

  	/**
  	 * Prints all memory content -
  	 * To be implemented in versions V0 and above.
  	 *
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public void printMemoryContent() throws MemoryException {
		for (int i = 0; i < memory.size(); i++) {
			System.out.println(BitwiseToolbox.getBitString(i, bitIndex) + ": " + memory.readByte(i));
		}
  	}

  	/**
  	 * Prints all disk content, structured by pages -
  	 * To be implemented in versions V1 and above.
  	 *
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public void printDiskContent() throws MemoryException {
		for (int i = 0; i < (disk.size()/pageSize); i++) { //iterate through table size
			System.out.print("PAGE " + i + ": ");
			byte[] diskContent = disk.readPage(i); //reads all values on a given page
			for (int j = 0; j < pageSize; j++) { //iterate through page size
				System.out.print(diskContent[j]);
				if(j != pageSize-1) //corner case for comma on last value of a page
					System.out.print(",");
			}
			System.out.println();
		}
  		// TO IMPLEMENT: V1, V2, V3, V4
  		return;
  	}

  	/**
  	 * Writes all pages currently in memory frames back to disk -
  	 * To be implemented in versions V1 and above.
  	 *
  	 * @throws MemoryException If there is an invalid access
  	 */
  	public void writeBackAllPagesToDisk() throws MemoryException {
		for (int i = 0; i < (memory.size()/pageSize); i++) { //iterate through pages of RAM
			if (pageTable.isValid(i)) { //Check if current page is a valid one
				int currentFrame = pageTable.lookup(i); //Find frame of the current page
				byte[] temp = new byte[pageSize]; //create byte array to store all values
				for (int j = 0; j < pageSize; j++) { //iterate through individual values in current page
					temp[j] = memory.readByte(currentFrame*pageSize + j); //store value into array
					transBytes++; //increment transfered bytes
				}
			disk.writePage(i,temp); //write the current page to disk
			}
		}
  	}

  	/**
  	 * Returns the number of page faults that have occurred to date -
  	 * To be implemented in versions V1 and above.
  	 *
  	 * @return Number of page faults
  	 */
  	public int getPageFaultCount() {
  		return pageFaults;
  	}

  	/**
  	 * Returns the number of bytes that have been transfered back and forth between the memory
  	 * and the disk to date -
  	 * To be implemented in versions V1 and above.
  	 *
  	 * @return Number of bytes transferred
  	 */
  	public int getTransferedByteCount() {
		transBytes = transBytes + (pageFaults*pageSize);
  		return transBytes;
  	}
}
