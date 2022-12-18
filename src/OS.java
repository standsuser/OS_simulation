import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Thread.State;
import java.nio.file.Files;
import java.nio.file.Path;
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
	public static Queue<Process> terminatedQueue = new LinkedList<Process>();
	public static Queue<Thread> threadQueue = new LinkedList<Thread>();
	public static Queue<Process> highPriority = new LinkedList<Process>();
	public static Queue<Process> mediumPriority = new LinkedList<Process>();
	public static Queue<Process> lowPriority = new LinkedList<Process>();

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
			jobQueue.peek().setState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}

		// Do the actual Round-Robin scheduling
		while (!threadQueue.isEmpty()) {
			view.queue.setText("Ready Queue: " + readyQueue);
			Thread currThread = threadQueue.remove();
			Process currProc = readyQueue.remove();
			if (currProc.state == ProcessState.READY) {
				if (!currThread.isAlive()) {
					currThread.start();// if it is first time to run
				} else {
					currThread.resume();// if it ran before
				}
				currProc.state = ProcessState.RUNNING;
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
				currThread.suspend();
			}
			if (currProc.state == ProcessState.RUNNING)
				currProc.state = ProcessState.READY;
			if (currThread.isAlive()) {
				threadQueue.add(currThread);
				readyQueue.add(currProc);
			} else {
				currProc.state = ProcessState.TERMINATED;
				terminatedQueue.add(currProc);
			}
		}
		view.repaint();
		view.revalidate();
		view.queue.setText("Ready Queue: " + readyQueue);
	}

	// MARIAM BEGIN HERE
	public static void Scheduler_MLQS() {

		Process currProc;
		Thread currThread;
		while (!jobQueue.isEmpty()) {
			jobQueue.peek().setState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}
		// should we loop on ready queue or jobqueue

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
					highPriority.add(readyQueue.remove()); // should there be default or no if so highpriority or low

			}

		}

		while (!threadQueue.isEmpty()) {
			view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
					+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
					+ "</html>");

			while (!highPriority.isEmpty()) {
				currThread = threadQueue.remove();
				currProc = highPriority.remove();
				if (currProc.state == ProcessState.READY) {

					currThread.run();// if it is first time to run

					if (currProc.state == ProcessState.RUNNING)
						currThread.resume();
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

			while (!mediumPriority.isEmpty()) {
				currThread = threadQueue.remove();

				currProc = mediumPriority.remove();
				if (currProc.state == ProcessState.READY) {

					currThread.run();// if it is first time to run

					if (currProc.state == ProcessState.RUNNING)
						currThread.resume();
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
			while (!lowPriority.isEmpty()) {
				currThread = threadQueue.remove();

				currProc = lowPriority.remove();
				if (currProc.state == ProcessState.READY) {

					currThread.run();// if it is first time to run

					if (currProc.state == ProcessState.RUNNING)
						currThread.resume();
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

		// maybe add if thread queue not empty run it in high
		// preemption to be added

		view.repaint();
		view.revalidate();
		view.queue.setText("<html>Ready Queue:" + readyQueue + "<br/>high priority Queue: " + highPriority
				+ "<br/>medium priority Queue: " + mediumPriority + "<br/>low priority Queue: " + lowPriority
				+ "</html>");
	}

	public static void Scheduler_FCFS() {

		Process currProc;
		Thread currThread;

		while (!jobQueue.isEmpty()) {
			jobQueue.peek().setState(ProcessState.READY);
			readyQueue.add(jobQueue.remove());
		}
		view.queue.setText("Ready Queue: " + readyQueue);

		while (!readyQueue.isEmpty()) {
			currThread = threadQueue.remove();

			currProc = readyQueue.remove();
			if (currProc.state == ProcessState.READY) {

				currThread.run();// if it is first time to run

				if (currProc.state == ProcessState.RUNNING)
					currThread.resume();
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

	public static Process createProcess() { // caroline said will take attributes a or b and priority
		Process process = new Process();
		return process;
	}

	public static void main(String[] args) {
		Process.processA();

		Process.processB();

		Process.processC();
		Process.processD();
		// Scheduler_RR();
		// Scheduler_MLQS();
		Scheduler_FCFS();
		System.out.println("Process Terminated!");
	}

}