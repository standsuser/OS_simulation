
public class Process extends Thread {
	// event , scheduling information
	private static int ProcessIDCounter = 0; // Process Counter
	private int programCounter;
	private int ProcessID; // ID
	private int ParentProcessID; // ID of parent
	private int userID;
	private String registers;
	private String data;
	private ProcessState state; // State
	private Priority priority; // Priority
	private String event; // Event the process is waiting for before it can run again

	// public String ProcessControlInformation;

	public Process(Runnable r) {
		super(r);
		ProcessID = ProcessIDCounter++;
		programCounter = 0;
		state = ProcessState.NEW;
		priority = Priority.Medium;
		event = "";
	}

	public static Process processA() {
		System.out.println("Process Type A created");
		Process processA = new Process(new Runnable() {
			public void run() {
				OS.assignSemaphore.semAssignWait();
				OS.printSemaphore.semPrintWait();
				OS.print("Enter File Name for Read.");
				OS.printSemaphore.semPrintPost();
				OS.assign("file");
				OS.assignSemaphore.semAssignPost();
				OS.readSemaphore.semReadWait();
				OS.getVariableSemaphore.semGetVariableWait();
				OS.readFile(OS.getVariable("file"));
				OS.getVariableSemaphore.semGetVariablePost();
				OS.readSemaphore.semReadPost();
				OS.deleteSemaphore.semDeleteWait();
				OS.delete("file");
				OS.deleteSemaphore.semDeletePost();
			}
		});
		return processA;
	}

	// assign write
	public static Process processB() {
		System.out.println("Process Type B created");
		Process processB = new Process(new Runnable() {
			public void run() {
				OS.assignSemaphore.semAssignWait();
				OS.printSemaphore.semPrintWait();
				OS.print("Enter File Name for Write.");
				OS.printSemaphore.semPrintPost();
				OS.assign("file");
				OS.assignSemaphore.semAssignPost();
				OS.assignSemaphore.semAssignWait();
				OS.printSemaphore.semPrintWait();
				OS.print("Enter data for Write.");
				OS.printSemaphore.semPrintPost();
				OS.assign("data");
				OS.assignSemaphore.semAssignPost();
				OS.writeSemaphore.semWriteWait();
				OS.getVariableSemaphore.semGetVariableWait();
				OS.writeFile(OS.getVariable("file"), OS.getVariable("data"));
				OS.writeSemaphore.semWritePost();
				OS.getVariableSemaphore.semGetVariablePost();
				OS.deleteSemaphore.semDeleteWait();
				OS.delete("file");
				OS.delete("data");
				OS.deleteSemaphore.semDeletePost();
			}
		});
		return processB;
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

	public ProcessState getProcessState() {
		return state;
	}

	public void setProcessState(ProcessState state) {
		this.state = state;
	}

	public Priority getProcessPriority() {
		return priority;
	}

	public void setProcessPriority(Priority priority) {
		this.priority = priority;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
