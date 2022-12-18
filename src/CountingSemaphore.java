import java.util.LinkedList;
import java.util.Queue;

public class CountingSemaphore {
	int count;
	Queue<Integer> queue;

	public CountingSemaphore(int count) {
		this.count = count;
		queue = new LinkedList<Integer>();
	}
}