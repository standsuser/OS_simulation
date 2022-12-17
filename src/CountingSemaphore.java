import java.util.LinkedList;
import java.util.Queue;

//zeyad: mostafa 2al its wrong but seebh for now sadge :( L RATIO+NO LIFE

public class CountingSemaphore {
	int count;
	Queue<Process> queue;

	public CountingSemaphore(int count) {
		this.count = count;
		queue = new LinkedList<Process>();
	}
}