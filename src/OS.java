import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class OS {
	
	public static int counter = 0;
	public static String user = "Mostafa Ramadan";
	public static String[] variableNames = new String[1000]; //Memory
	public static Object[] variableValue = new Object[1000]; //Memory
	public static BinarySemaphore readSemaphore = new BinarySemaphore();
	public static BinarySemaphore writeSemaphore = new BinarySemaphore();
	public static BinarySemaphore printSemaphore = new BinarySemaphore();
	public static BinarySemaphore assignSemaphore = new BinarySemaphore();

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
		Path br = Path.of(fileName); //creates a file if not found
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
	

	
	public static Process createProcess() {
		Process process = new Process();
		//MUST SET ATTRIBUTES
		return process;
	}

	public static void main(String[] args) {
		String s = "";
		//create process A and B object
		processB();
		processA();
		System.out.println("Process Terminated!");
	}
	
}
