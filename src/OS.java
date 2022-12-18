import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Thread.State;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class OS {
	public static View view = new View();
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
	public static ArrayList<Process> blockedProcesses = new ArrayList<Process>();
	public static Queue<Process> terminatedQueue = new LinkedList<Process>();
	public static Queue<Process> highPriority = new LinkedList<Process>();
	public static Queue<Process> mediumPriority = new LinkedList<Process>();
	public static Queue<Process> lowPriority = new LinkedList<Process>();
	public static Process currProc;

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
		System.out.print("PID" + currProc.getProcessID() + ": ");
		for (int i = 0; i < variableNames.length; i++) {
			if (variableNames[i] != null && variableNames[i].equals(st.split(" ")[0])) {
				System.out.println(variableValue[i]);
				flag = true;
				break;
			}
		}
		if (!flag) {
			System.out.println(st);
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
			jobQueue.peek().setProcessState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}

		// Do the actual Round-Robin scheduling
		while (!readyQueue.isEmpty() || !blockedProcesses.isEmpty()) {
			view.queue.setText(
					"<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses + "</html>");
			currProc = readyQueue.remove();
			if (currProc.state == ProcessState.READY) {
				if (!currProc.isAlive()) {
					currProc.start();// if it is first time to run
				} else {
					currProc.resume();// if it ran before
				}
				currProc.state = ProcessState.RUNNING;
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
				currProc.suspend();
			}
			if (currProc.isAlive() && currProc.state == ProcessState.RUNNING) {
				currProc.state = ProcessState.READY;
				readyQueue.add(currProc);
			} else if (!currProc.isAlive()) {
				currProc.state = ProcessState.TERMINATED;
				terminatedQueue.add(currProc);
			}
			while (!jobQueue.isEmpty()) {
				jobQueue.peek().setProcessState(ProcessState.READY);
				readyQueue.add(jobQueue.remove());
			}
			view.queue.setText(
					"<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses + "</html>");
		}
		view.repaint();
		view.revalidate();
		view.queue
				.setText("<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses + "</html>");
	}

	// MARIAM BEGIN HERE
	public static void Scheduler_MLQS() {

		while (!jobQueue.isEmpty()) {
			jobQueue.peek().setProcessState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}

		while (!readyQueue.isEmpty()) {
			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "</html>");

			switch (readyQueue.peek().priority) {
				case Low:
					lowPriority.add(readyQueue.remove());
					break;
				case Medium:
					mediumPriority.add(readyQueue.remove());
					break;
				case High:
					highPriority.add(readyQueue.remove());
					break;
				default:
					mediumPriority.add(readyQueue.remove());

			}

		}

		highHelper();
		mediumHelper();
		lowHelper();

		// maybe add if thread queue not empty run it in high
		// preemption to be added

		view.repaint();
		view.revalidate();
		view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
				+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
				+ "</html>");
	}

	public static void lowHelper() {

		while (!lowPriority.isEmpty()) {
			System.out.println("(Emptying Low Priority) | Ready Queue: " + readyQueue + " | high priority Queue: "
					+ highPriority
					+ " | medium priority Queue: " + mediumPriority + " | low priority Queue: " + lowPriority + " |");
			if (!highPriority.isEmpty())
				highHelper();
			if (!mediumPriority.isEmpty())
				mediumHelper();

			currProc = lowPriority.remove();
			if (currProc.state == ProcessState.READY) {

				currProc.start();// if it is first time to run

				if (currProc.state == ProcessState.RUNNING)
					currProc.resume();
			} else {
				currProc.state = ProcessState.TERMINATED;
				terminatedQueue.add(currProc);
			}
			view.repaint();
			view.revalidate();
			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "</html>");

		}
	}

	public static void mediumHelper() {
		while (!mediumPriority.isEmpty()) {
			System.out.println("(Emptying Medium Priority) | Ready Queue: " + readyQueue + " | high priority Queue: "
					+ highPriority
					+ " | medium priority Queue: " + mediumPriority + " | low priority Queue: " + lowPriority + " |");

			if (!highPriority.isEmpty())
				highHelper();

			currProc = mediumPriority.remove();
			if (currProc.state == ProcessState.READY) {

				currProc.start();// if it is first time to run

				if (currProc.state == ProcessState.RUNNING)
					currProc.resume();
			} else {
				currProc.state = ProcessState.TERMINATED;
				terminatedQueue.add(currProc);
			}
			view.repaint();
			view.revalidate();
			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "</html>");

		}
	}

	public static void highHelper() {

		while (!highPriority.isEmpty()) {
			System.out.println("(Emptying High Priority) | Ready Queue: " + readyQueue + " | high priority Queue: "
					+ highPriority
					+ " | medium priority Queue: " + mediumPriority + " | low priority Queue: " + lowPriority + " |");
			currProc = highPriority.remove();
			if (currProc.state == ProcessState.READY) {

				currProc.start();// if it is first time to run

				if (currProc.state == ProcessState.RUNNING)
					currProc.resume();
			} else {
				currProc.state = ProcessState.TERMINATED;
				terminatedQueue.add(currProc);
			}
			view.repaint();
			view.revalidate();
			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "</html>");
		}
	}

	public static void Scheduler_FCFS() {


		while (!jobQueue.isEmpty()) {
			jobQueue.peek().setProcessState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}
		view.queue.setText("Ready Queue: " + readyQueue);

		while (!readyQueue.isEmpty()) {

			currProc = readyQueue.remove();
			if (currProc.state == ProcessState.READY) {

				currProc.start();// if it is first time to run

				if (currProc.state == ProcessState.RUNNING)
					currProc.resume();
			} else {
				currProc.state = ProcessState.TERMINATED;
				terminatedQueue.add(currProc);
			}
			view.repaint();
			view.revalidate();
			view.queue.setText("Ready Queue: " + readyQueue);

		}

		// check whose turn is it.
		// put chosen process in running queue
		// run it
		// bonus if it is preemptive
	}

	// MARIAM END

	public static Process createProcess(char type, Priority prio) {
		Process process = null;
		switch (type) {
			case 'A':
				process = Process.processA();
				break;
			case 'B':
				process = Process.processB();
				break;
		}
		process.setProcessPriority(prio);
		OS.jobQueue.add(process);
		System.out.println("Process added to Job Queue: " + OS.jobQueue);// lets not use syso
		return process;
	}

	public static void main(String[] args) {
		createProcess('A', Priority.High);
		createProcess('B', Priority.Low);
		createProcess('A', Priority.Medium);
		createProcess('B', Priority.High);
		// Scheduler_RR();
		//Scheduler_MLQS();
		 Scheduler_FCFS();
		System.out.println("No Processes Left in Queues");
	}

}