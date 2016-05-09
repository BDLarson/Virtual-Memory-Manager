package vmmanager;

import vmsimulation.*;

public class VirtualMemoryManagerV0 {

	MainMemory memory;
	int bitIndex;

	// Constructor
	public VirtualMemoryManagerV0(MainMemory memory, BackingStore disk, Integer pageSize) throws MemoryException {
		this.memory = memory;
		bitIndex = (int)(Math.log(memory.size()) / Math.log(2) - 1);
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
		memory.writeByte(fourByteBinaryString, value);
		System.out.println("RAM write: " + BitwiseToolbox.getBitString(fourByteBinaryString, bitIndex) + " <-- " + value);
		// TO IMPLEMENT: V0, V1, V2, V3, V4
  		return;
  	}

  	/**
  	 * Reads a byte from a given address - 
  	 * To be implemented in versions V0 and above.
  	 * 
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public Byte readByte(Integer fourByteBinaryString) throws MemoryException {
		byte value = 0;
		value = memory.readByte(fourByteBinaryString);
		System.out.println("RAM read: " + BitwiseToolbox.getBitString(fourByteBinaryString, bitIndex) + " --> " + value);
		// TO IMPLEMENT: V0, V1, V2, V3, V4
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
  		// TO IMPLEMENT: V1, V2, V3, V4
  	}
  	
  	/**
  	 * Returns the number of page faults that have occurred to date - 
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @return Number of page faults
  	 */
  	public int getPageFaultCount() {
		int count = 0;
  		// TO IMPLEMENT: V1, V2, V3, V4
  		return count;
  	}
  	
  	/**
  	 * Returns the number of bytes that have been transfered back and forth between the memory
  	 * and the disk to date -
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @return Number of bytes transferred
  	 */
  	public int getTransferedByteCount() {
		int count = 0;
  		// TO IMPLEMENT: V1, V2, V3, V4
  		return count;
  	}
}
