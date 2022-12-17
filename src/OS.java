import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Thread.State;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class OS {

	public static int counter = 0;
	public static String user = "Zeyad Shafik";
	public static String[] variableNames = new String[1000]; // Memory
	public static Object[] variableValue = new Object[1000]; // Memory
	public static ReadSemaphore readSemaphore = new ReadSemaphore(3);
	public static WriteSemaphore writeSemaphore = new WriteSemaphore();
	public static PrintSemaphore printSemaphore = new PrintSemaphore(3);
	public static AssignSemaphore assignSemaphore = new AssignSemaphore();
	public static DeleteSemaphore deleteSemaphore = new DeleteSemaphore();
	public static GetVariableSemaphore getVariableSemaphore = new GetVariableSemaphore(3);
	public static Queue<Process> jobQueue = new LinkedList<Process>();
	public static Queue<Process> readyQueue = new LinkedList<Process>();
	public static Queue<Process> terminatedQueue = new LinkedList<Process>();
	public static Queue<Thread> threadQueue = new LinkedList<Thread>();

	public static void readFile(String fileName) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String st;
			while ((st = br.readLine()) != null)
				System.out.println(st);
		} catch (Exception e) {
			System.out.println("File not found.");
		}
	}

	public static void writeFile(String fileName, String data) {
		Path br = Path.of(fileName); // creates a file if not found
		try {
			Files.writeString(br, data);
		} catch (Exception e) {

		}
	}

	public static void print(String st) {
		boolean flag = false;
		for (int i = 0; i < variableNames.length; i++) {
			if (variableNames[i] != null && variableNames[i].equals(st.split(" ")[0])) {
				System.out.print(variableValue[i]);
				flag = true;
				break;
			}
		}
		if (!flag) {
			System.out.print(st);
		}
	}

	public static void assign(String name) { // st This is a string
		Scanner sc = new Scanner(System.in);
		String value = sc.nextLine();
		variableNames[counter] = name;
		try {
			int x = Integer.parseInt(value);
			variableValue[counter++] = x;
		} catch (Exception e) {
			variableValue[counter++] = value;
		}
	}

	public static void delete(String name) { // st This is a string
		boolean flag = false;
		for (int i = 0; i < variableNames.length; i++) {
			if (variableNames[i] != null && variableNames[i].equals(name)) {
				variableNames[counter] = null;
				variableValue[counter--] = null;
				flag = true;
				break;
			}
		}
		if (!flag) {
			System.out.print("Variable not found");
		}
	}

	public static String getVariable(String name) { // st This is a string
		boolean flag = false;
		String result = "";
		for (int i = 0; i < variableNames.length; i++) {
			if (variableNames[i] != null && variableNames[i].equals(name)) {
				result = (String) variableValue[i];
				flag = true;
			}
		}
		if (!flag) {
			System.out.print("Variable not found");
		}
		return result;
	}

	public static void Scheduler_RR() {
		// move the processes from the jobQueue into the readyQueue
		while (!jobQueue.isEmpty()) {
			readyQueue.add(jobQueue.remove());
		}
		// Do the actual Round-Robin scheduling
		while (!threadQueue.isEmpty()) {
			System.out.println("Ready Queue: " + readyQueue);
			Thread currThread = threadQueue.remove();
			Process currProc = readyQueue.remove();
			if (!currThread.isAlive()) {
				currThread.start();
			} else {
				currThread.resume();
			}
			currProc.state = ProcessState.RUNNING;
			System.out.println(currProc + " is now running");
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
			}
			currThread.suspend();
			currProc.state = ProcessState.READY;
			System.out.println(currProc + " is kicked out of the processor");
			if (currThread.isAlive()) {
				threadQueue.add(currThread);
				readyQueue.add(currProc);
			} else {
				currProc.state = ProcessState.TERMINATED;
				terminatedQueue.add(currProc);
			}
		}
	}

	// MARIAM BEGIN HERE
	public static void Scheduler_MLQS() {

	}

	public static void Scheduler_FCFS() {

		// check whose turn is it.
		// put chosen process in running queue
		// run it
		// bonus if it is preemptive
	}

	// MARIAM END

	// assign read
	public static void processA() {
		Process processA = createProcess();
		Thread a = new Thread(new Runnable() {
			public void run() {
				processA.state = ProcessState.READY;
				printSemaphore.semPrintWait(processA);
				System.out.println("Process A: Enter File Name.");
				printSemaphore.semPrintPost();
				assignSemaphore.semAssignWait(processA);
				assign("file");
				assignSemaphore.semAssignPost();
				processA.state = ProcessState.RUNNING;
				readSemaphore.semReadWait(processA);// zeyad : DEADLOCK MIGHT HAPPEN HEHE
				getVariableSemaphore.semGetVariableWait(processA);
				readFile(getVariable("file"));
				readSemaphore.semReadPost();// zeyad :not sure mafrood abdlha m3a t7tha wla la
				getVariableSemaphore.semGetVariablePost();
				deleteSemaphore.semDeleteWait(processA);
				delete("file");
				deleteSemaphore.semDeletePost();
				System.out.println("Reached A");
				processA.state = ProcessState.TERMINATED;
			}
		});
		threadQueue.add(a);
	}

	// assign write
	public static void processB() {
		Process processB = createProcess();
		Thread a = new Thread(new Runnable() {
			public void run() {
				processB.state = ProcessState.READY;
				printSemaphore.semPrintWait(processB);
				System.out.println("Process B: Enter File Name.");
				printSemaphore.semPrintPost();
				assignSemaphore.semAssignWait(processB);
				assign("file");
				assignSemaphore.semAssignPost();
				printSemaphore.semPrintWait(processB);
				System.out.println("Process B: Enter data.");
				printSemaphore.semPrintPost();
				assignSemaphore.semAssignWait(processB);
				assign("data");
				assignSemaphore.semAssignPost();
				processB.state = ProcessState.RUNNING;
				writeSemaphore.semWriteWait(processB);
				getVariableSemaphore.semGetVariableWait(processB);
				writeFile(getVariable("file"), getVariable("data"));
				writeSemaphore.semWritePost();
				getVariableSemaphore.semGetVariablePost();
				deleteSemaphore.semDeleteWait(processB);
				delete("file");
				delete("data");
				deleteSemaphore.semDeletePost();
				System.out.println("Reached B");
				processB.state = ProcessState.TERMINATED;
			}
		});
		threadQueue.add(a);
	}

	public static void processC() {
		Process processC = createProcess();
		Thread a = new Thread(new Runnable() {
			public void run() {
				System.out.println("Ich leibe meine frau");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
				System.out.println("Ich leibe meine frau");

			}
		});
		threadQueue.add(a);
	}

	public static void processD() {
		Process processD = createProcess();
		Thread a = new Thread(new Runnable() {
			public void run() {
				System.out.println("Du bist mein schatz");
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
				}
				System.out.println("Du bist mein schatz");

			}
		});
		threadQueue.add(a);
	}

	public static Process createProcess() {
		Process process = new Process();
		return process;
	}

	public static void main(String[] args) {
		processC();
		processD();
		Scheduler_RR();
		System.out.println("Process Terminated!");
	}

}