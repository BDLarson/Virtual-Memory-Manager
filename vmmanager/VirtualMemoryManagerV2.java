package vmmanager;

import vmsimulation.*;
import java.util.*;

public class VirtualMemoryManagerV2 {

	MainMemory memory;
	BackingStore disk;
	PageTable pageTable;
	int[] pTable;
	int bitIndex;
	int pageSize;
	private int frameNumber = 0;
	int pageFaults = 0;
	int transBytes = 0;
	int uFrame = 0;
	int oFrame = 0;

	// Constructor
	public VirtualMemoryManagerV2(MainMemory memory, BackingStore disk, Integer pageSize) throws MemoryException {
		this.memory = memory;
		this.disk = disk;
		this.pageSize = pageSize;
		bitIndex = (int)(Math.log(memory.size()) / Math.log(2) - 1);
		pageTable = new PageTable(pageSize, (memory.size()/pageSize));
		pTable = new int[memory.size() / pageSize];
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
		int address;
		int newPlaceHolder = 0;

		if (pageTable.isValid(pageNumber) == false) {
			//System.out.println("table length = " + pTable.length);
			//System.out.println("uFrame = " + oFrame);
			if (uFrame == pTable.length) {

				if (oFrame == pTable.length) {
					oFrame = 0;
				}

				System.out.println("Evicting page " + pTable[oFrame]);
				pageTable.delete(pTable[oFrame]);
				int t = pTable[oFrame];
				pTable[oFrame] = pageNumber;
				frameNumber = oFrame;
				address = oFrame * pageSize;
				byte[] diskSubstitute = new byte[pageSize];
				for (int i = 0; i < pageSize; i++) {
					diskSubstitute[i] = memory.readByte(address);
					address++;
					transBytes++;
				}
				disk.writePage(t, diskSubstitute);
				oFrame++;
			}

			if (uFrame < pTable.length) {
				pTable[uFrame] = pageNumber;
				uFrame++;

			}
			System.out.println("Bringing page " + pageNumber + " into frame " + frameNumber);
			pageFaults++;
			address = frameNumber * pageSize;

			for (int i = 0; i < pageSize; i++) { //iterate through values of current page
				memory.writeByte(address, diskData[i]); //input all values into ram
				address++;
			}
			pageTable.change(pageNumber, frameNumber);
			newPlaceHolder = frameNumber;
			frameNumber++;

		} else {
			newPlaceHolder = pageTable.lookup(pageNumber);

			System.out.println("Page " + pageNumber + " is in memory");
		}
		address = newPlaceHolder*pageSize + offset;
		memory.writeByte(address, value);
		System.out.println("RAM: " + BitwiseToolbox.getBitString(address, bitIndex) + " <-- " + value);
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
		int address;
		int newPlaceHolder = 0;

		if (pageTable.isValid(pageNumber) == false) {
			//System.out.println("table length = " + pTable.length);
			//System.out.println("uFrame = " + oFrame);
			if (uFrame == pTable.length) {

				if (oFrame == pTable.length) {
					oFrame = 0;
				}

				System.out.println("Evicting page " + pTable[oFrame]);
				pageTable.delete(pTable[oFrame]);
				int t = pTable[oFrame];
				pTable[oFrame] = pageNumber;
				frameNumber = oFrame;
				address = oFrame * pageSize;
				byte[] diskSubstitute = new byte[pageSize];

				for (int i = 0; i < pageSize; i++) {
					diskSubstitute[i] = memory.readByte(address);
					address++;
					transBytes++;
				}

				disk.writePage(t, diskSubstitute);
				oFrame++;
			}

			if (uFrame < pTable.length) {
				pTable[uFrame] = pageNumber;
				uFrame++;
			}

			System.out.println("Bringing page " + pageNumber + " into frame " + frameNumber);
			pageFaults++;
			address = frameNumber * pageSize;

			for (int i = 0; i < pageSize; i++) { //iterate through values of current page
				memory.writeByte(address, diskData[i]); //input all values into ram
				address++;
			}
			pageTable.change(pageNumber, frameNumber);
			newPlaceHolder = frameNumber;
			frameNumber++;

		} else {
			newPlaceHolder = pageTable.lookup(pageNumber);
			System.out.println("Page " + pageNumber + " is in memory");
		}
		address = newPlaceHolder*pageSize + offset;
		value = memory.readByte(address);
		System.out.println("RAM: " + BitwiseToolbox.getBitString(address, bitIndex) + " --> " + value);
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
		for (int i = 0; i < pTable.length; i++) { //iterate through pages of RAM
			if (pageTable.isValid(pTable[i])) { //Check if current page is a valid one
				int currentFrame = pageTable.lookup(pTable[i]); //Find frame of the current page
				byte[] temp = new byte[pageSize]; //create byte array to store all values
				for (int j = 0; j < pageSize; j++) { //iterate through individual values in current page
					temp[j] = memory.readByte(currentFrame*pageSize + j); //store value into array
					transBytes++; //increment transfered bytes
				}
			disk.writePage(pTable[i],temp); //write the current page to disk
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
