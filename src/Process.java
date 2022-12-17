import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Process {
	// event , scheduling information
	public static int ProcessIDCounter = 0; // Process Counter
	public int programCounter;
	public int ProcessID; // ID
	public int ParentProcessID; // ID of parent
	public int userID;
	public String registers;
	public String data;
	public ProcessState state; // State
	public Priority priority; // Priority
	public String event; // Event the process is waiting for before it can run again

	// public String ProcessControlInformation;

	public Process() {
		ProcessID = ProcessIDCounter++;
		programCounter = 0;
		state = ProcessState.NEW;
		priority = Priority.Medium;
		event = "";
		OS.jobQueue.add(this);
		System.out.println("Job Queue: " + OS.jobQueue);
	}

	public String toString() {
		return this.ProcessID + "";
	}

	public int getProgramCounter() {
		return programCounter;
	}

	public void setProgramCounter(int programCounter) {
		this.programCounter = programCounter;
	}

	public int getProcessID() {
		return ProcessID;
	}

	public void setProcessID(int processID) {
		ProcessID = processID;
	}

	public int getParentProcessID() {
		return ParentProcessID;
	}

	public void setParentProcessID(int parentProcessID) {
		ParentProcessID = parentProcessID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getRegisters() {
		return registers;
	}

	public void setRegisters(String registers) {
		this.registers = registers;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ProcessState getState() {
		return state;
	}

	public void setState(ProcessState state) {
		this.state = state;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
