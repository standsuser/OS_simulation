import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class OperatingSystem {
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
		System.out.println("readFile method called");
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
		System.out.println("writeFile method called");
		Path br = Path.of(fileName); // creates a file if not found
		try {
			Files.writeString(br, data);
		} catch (Exception e) {

		}
	}

	public static void print(String st) {
		System.out.println("print method called");
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
		System.out.println("assign method called");
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
		System.out.println("delete method called");
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
		System.out.println("getVariable method called");
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
		System.out.println("Round Robin Scheduler method called");
		// move the processes from the jobQueue into the readyQueue
		while (!jobQueue.isEmpty()) {
			jobQueue.peek().setProcessState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}

		// Do the actual Round-Robin scheduling
		while (!readyQueue.isEmpty() || !blockedProcesses.isEmpty()) {
			currProc = readyQueue.remove();
			view.queue.setText(
					"<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses + "<br/> Process "
							+ currProc.getProcessID() + " is currently being processed" + "<br/>Terminated Queue "
							+ terminatedQueue + "</html>");
			if (currProc.getProcessState() == ProcessState.READY) {
				if (!currProc.isAlive()) {
					currProc.start();// if it is first time to run
				} else {
					currProc.resume();// if it ran before
				}
				currProc.setProcessState(ProcessState.RUNNING);
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
				currProc.suspend();
			}
			if (currProc.isAlive() && currProc.getProcessState() == ProcessState.RUNNING) {
				currProc.setProcessState(ProcessState.READY);
				readyQueue.add(currProc);
			} else if (!currProc.isAlive()) {
				currProc.setProcessState(ProcessState.TERMINATED);
				System.out.println("Process " + currProc.getProcessID() + " has been terminated");
				terminatedQueue.add(currProc);
			}

			view.queue.setText(
					"<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses + "<br/> Process "
							+ currProc.getProcessID() + " is currently being processed" + "<br/>Terminated Queue "
							+ terminatedQueue + "</html>");
		}
		view.repaint();
		view.revalidate();
		view.queue
				.setText("<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses
						+ "<br/>Terminated Queue "
						+ terminatedQueue + "</html>");
	}

	// MARIAM BEGIN HERE
	public static void Scheduler_MLQS() {
		System.out.println("MLQS Scheduler method called");

		while (!jobQueue.isEmpty()) {
			jobQueue.peek().setProcessState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}

		view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
				+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
				+ "</html>");

		while (!readyQueue.isEmpty() || !blockedProcesses.isEmpty()) {
			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "</html>");

			switch (readyQueue.peek().getProcessPriority()) {
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

		while (!highPriority.isEmpty() || !blockedProcesses.isEmpty()) {

			currProc = highPriority.remove();

			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "<br/>Blocked Processes: " + blockedProcesses + "<br/> Process "
					+ currProc.getProcessID() + " is currently being processed" + "<br/>Terminated Queue "
					+ terminatedQueue
					+ "</html>");

			currProc.start();// if it is first time to run

			currProc.setProcessState(ProcessState.RUNNING);

			while (currProc.isAlive()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			currProc.setProcessState(ProcessState.TERMINATED);
			System.out.println("Process " + currProc.getProcessID() + " has been terminated");
			terminatedQueue.add(currProc);

		}

		view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
				+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
				+ "<br/>Blocked Processes: " + blockedProcesses + "<br/>Terminated Queue " + terminatedQueue
				+ "</html>");

		while (!mediumPriority.isEmpty()) {

			currProc = mediumPriority.remove();

			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "<br/>Blocked Processes: " + blockedProcesses + "<br/> Process "
					+ currProc.getProcessID() + " is currently being processed" + "<br/>Terminated Queue "
					+ terminatedQueue
					+ "</html>");

			currProc.start();// if it is first time to run

			currProc.setProcessState(ProcessState.RUNNING);

			while (currProc.isAlive()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			currProc.setProcessState(ProcessState.TERMINATED);
			System.out.println("Process " + currProc.getProcessID() + " has been terminated");
			terminatedQueue.add(currProc);

		}

		view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
				+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
				+ "<br/>Blocked Processes: " + blockedProcesses + "<br/>Terminated Queue " + terminatedQueue
				+ "</html>");

		while (!lowPriority.isEmpty()) {

			currProc = lowPriority.remove();
			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "<br/>Blocked Processes: " + blockedProcesses + "<br/> Process "
					+ currProc.getProcessID() + " is currently being processed" + "<br/>Terminated Queue "
					+ terminatedQueue
					+ "</html>");

			currProc.start();// if it is first time to run

			currProc.setProcessState(ProcessState.RUNNING);

			while (currProc.isAlive()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			currProc.setProcessState(ProcessState.TERMINATED);
			System.out.println("Process " + currProc.getProcessID() + " has been terminated");
			terminatedQueue.add(currProc);

		}

		view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
				+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
				+ "<br/>Blocked Processes: " + blockedProcesses + "<br/>Terminated Queue " + terminatedQueue
				+ "</html>");

	}

	public static void Scheduler_FCFS() {
		System.out.println("FCFS Scheduler method called");

		while (!jobQueue.isEmpty()) {
			jobQueue.peek().setProcessState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}

		view.queue.setText(
				"<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses + "</html>");
		while (!readyQueue.isEmpty() || !blockedProcesses.isEmpty()) {

			currProc = readyQueue.remove();

			view.queue.setText(
					"<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses + "<br/> Process "
							+ currProc.getProcessID() + " is currently being processed" + "<br/>Terminated Queue "
							+ terminatedQueue + "</html>");

			currProc.start();// if it is first time to run

			currProc.setProcessState(ProcessState.RUNNING);

			while (currProc.isAlive()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			currProc.setProcessState(ProcessState.TERMINATED);
			System.out.println("Process " + currProc.getProcessID() + " has been terminated");
			terminatedQueue.add(currProc);

		}

		view.queue.setText(
				"<html>Ready Queue:" + readyQueue + "<br/>Blocked Processes: " + blockedProcesses
						+ "<br/>Terminated Queue " + terminatedQueue + "</html>");

	}

	public static Process createProcess(char type, Priority prio) {
		System.out.println("createProcess method called");
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
		OperatingSystem.jobQueue.add(process);
		System.out.println("Process added to Job Queue: " + OperatingSystem.jobQueue);// lets not use syso
		return process;
	}

	public static void main(String[] args) {
		createProcess('A', Priority.High);
		createProcess('B', Priority.Low);
		createProcess('A', Priority.Medium);
		createProcess('B', Priority.High);
		// Scheduler_RR();
		// Scheduler_MLQS();
		//Scheduler_FCFS();
		System.out.println("No Processes Left in Queues");
	}

}