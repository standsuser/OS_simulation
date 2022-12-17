import java.util.LinkedList;
import java.util.Queue;

public class BinarySemaphore {
	boolean availability;// true = 1 , false = 0
	Queue<Process> queue;

	public BinarySemaphore() {
		availability = true;
		queue = new LinkedList<Process>();
	}
}