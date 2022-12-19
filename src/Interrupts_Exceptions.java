import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Interrupts_Exceptions {
	static Object[] memory = new Object[10];
	static int freeSpacePtr = 0; // current memory pointer
	static int heapPtr = 5; // end of the heap, and the start of the stack
	static int stackPtr = 9; // end of the stack
	static int actualPrivilegedPtr = 9; // start of the privileged memory
	static int PrivilegedPtr = 10; // end of privileged memory
	static char c; // user key pressed
	static boolean keyFlag = false;
	static String DMAState = "Idle";

	public static void postTheKeyValueIntoASystemAreaNearTheBottomOfMemory() {
		if (freeSpacePtr < heapPtr) { // Using free place in heap
			memory[freeSpacePtr++] = (c);
		} else if (freeSpacePtr >= heapPtr && heapPtr < stackPtr) { // Requesting more heap
			memory[freeSpacePtr++] = (c);
			heapPtr++;
		} else if (freeSpacePtr >= heapPtr && heapPtr >= stackPtr) { // Requesting privileged memory
			freeSpacePtr = 0;
			memory[freeSpacePtr++] = (c);
		}
		System.out.println("Allocated space in the heap");
	}

	public static void InterruptAHandler() {
		System.out.println("Press a key");
		keyFlag = false;
		JFrame f = new JFrame();
		f.setSize(0, 0);
		f.setUndecorated(true);
		f.setVisible(true);
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				c = e.getKeyChar();
				keyFlag = true;
				f.dispose(); // closes the frame
				postTheKeyValueIntoASystemAreaNearTheBottomOfMemory();
			}

			public void keyReleased(KeyEvent e) {
			}
		});
		while (!keyFlag) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void InterruptBHandler() {
		int length = (int) (Math.random() * 7) + 4; // Specify the length of the data.
		int address = (int) (Math.random() * (100 - length)); // Specify the length of the data.
		Disk disk = new Disk(100);
		if (Math.random() * 2 == 1) {
			disk.readFrom(address, length);
			System.out.println("Read from disk successfully");
		} else {
			disk.writeTo(address, "New data");
			System.out.println("Written to disk successfully");
		}
	}

	public static void ExceptionAHandler() {
		if (heapPtr < stackPtr) {
			heapPtr++;
			System.out.println("Pointer: " + freeSpacePtr); // prints the first free
			System.out.println("Heap space increased"); // space in the memory
		} else {
			freeSpacePtr = 0;
			System.out.println("Pointer: " + freeSpacePtr);
			System.out.println("Heap space increased");
		}
	}

	public static void ExceptionBHandler() {
		String state = "Running";
		System.out.println("Can't divide by zero!");
		state = "Terminated";
		System.out.println("Process Terminated!");
		// Change Process State
	}

	public static void ExceptionCHandler() {
		System.out.println("Can't access privileged memory!");
		if (freeSpacePtr < heapPtr) {
			System.out.println(freeSpacePtr + " is free in the heap");
		} else if (heapPtr < stackPtr) {
			heapPtr++;
			System.out.println("Heap space increased");
			System.out.println(freeSpacePtr + " is free in the heap");
		} else {
			System.out.println("Not enough space in the memory");
		}
	}

	public static void execute(Event e) {
		switch (e) {

			case InterruptA: // InteruptA: User presses on a key on the keyboard.
				InterruptAHandler();
				break;

			case InterruptB: // InteruptB: Disk controller finishes reading data.
				InterruptBHandler();
				break;

			case ExceptionA: // ExceptionA: Requesting more heap.
				ExceptionAHandler();
				break;

			case ExceptionB: // ExceptionB: Attempts integer division by zero.
				ExceptionBHandler();
				break;

			case ExceptionC: // ExceptionC: Attempts to access privileged memory.
				ExceptionCHandler();
				break;
		}
		System.out.println();
	}

	public static Event generateRandomEvent() { // Gets a random event
		int x = 4;
		// (int) (Math.random() * 5);
		Event e = null;
		switch (x) {
			case 0:
				e = Event.InterruptA; // InteruptA: User presses on a key on the keyboard.
				break;
			case 1:
				e = Event.InterruptB; // InteruptB: Disk controller finishes reading data.
				break;
			case 2:
				e = Event.ExceptionA; // ExceptionA: Requesting more heap.
				break;
			case 3:
				e = Event.ExceptionB; // ExceptionB: Attempts integer division by zero.
				break;
			case 4:
				e = Event.ExceptionC; // ExceptionC: Attempts to access privileged memory.
				break;
		}
		return e;
	}

	public static void main(String[] args) {
		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// char storedChar = br.read(br,0,1);
		Event e = generateRandomEvent();
		execute(e);
		for (int i = 0; i < 10; i++) {
			System.out.print(memory[i] + " ");
		}
	}

}
