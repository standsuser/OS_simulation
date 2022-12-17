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


	public static void processA() {
		Process processA = OS.createProcess();
		Thread a = new Thread(new Runnable() {
			public void run() {
				OS.assignSemaphore.semAssignWait(processA);
				OS.printSemaphore.semPrintWait(processA);
				OS.print("PID" + processA.getProcessID() + ": Enter File Name for Read.");
				OS.printSemaphore.semPrintPost();
				OS.assign("file");
				OS.assignSemaphore.semAssignPost();
				processA.state = ProcessState.RUNNING;
				OS.readSemaphore.semReadWait(processA);// zeyad : DEADLOCK MIGHT HAPPEN HEHE
				OS.getVariableSemaphore.semGetVariableWait(processA);
				OS.readFile(OS.getVariable("file"));
				OS.readSemaphore.semReadPost();// zeyad :not sure mafrood abdlha m3a t7tha wla la
				OS.getVariableSemaphore.semGetVariablePost();
				OS.deleteSemaphore.semDeleteWait(processA);
				OS.delete("file");
				OS.deleteSemaphore.semDeletePost();
				processA.state = ProcessState.TERMINATED;
			}
		});
		OS.threadQueue.add(a);
	}

	// assign write
	public static void processB() {
		Process processB = OS.createProcess();
		Thread a = new Thread(new Runnable() {
			public void run() {
				OS.assignSemaphore.semAssignWait(processB);
				OS.printSemaphore.semPrintWait(processB);
				OS.print("PID" + processB.getProcessID() + ": Enter File Name for Write.");
				OS.printSemaphore.semPrintPost();
				OS.assign("file");
				OS.assignSemaphore.semAssignPost();
				OS.assignSemaphore.semAssignWait(processB);
				OS.printSemaphore.semPrintWait(processB);
				OS.print("PID" + processB.getProcessID() + ": Enter data for Write.");
				OS.printSemaphore.semPrintPost();
				OS.assign("data");
				OS.assignSemaphore.semAssignPost();
				processB.state = ProcessState.RUNNING;
				OS.writeSemaphore.semWriteWait(processB);
				OS.getVariableSemaphore.semGetVariableWait(processB);
				OS.writeFile(OS.getVariable("file"), OS.getVariable("data"));
				OS.writeSemaphore.semWritePost();
				OS.getVariableSemaphore.semGetVariablePost();
				OS.deleteSemaphore.semDeleteWait(processB);
				OS.delete("file");
				OS.delete("data");
				OS.deleteSemaphore.semDeletePost();
				processB.state = ProcessState.TERMINATED;
			}
		});
		OS.threadQueue.add(a);
	}

	public static void processC() {
		Process processC = OS.createProcess();
		Thread a = new Thread(new Runnable() {
			public void run() {
				System.out.println("Ich leibe meine frau");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}

			}
		});
		OS.threadQueue.add(a);
	}

	public static void processD() {
		Process processD = OS.createProcess();
		Thread a = new Thread(new Runnable() {
			public void run() {
				System.out.println("Du bist mein schatz");
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
				}

			}
		});
		OS.threadQueue.add(a);
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
