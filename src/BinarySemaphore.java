import java.util.Queue;

public class BinarySemaphore {
	boolean availability;
	Queue<Process> queue;
	
	public void semPrintWait(Process process) {
		if (availability == true)
			availability = false;
		else {
			queue.add(process);
			process.state = ProcessState.BLOCKED;
			/* place this process in s.queue */
			/* block this process */
		}
	}
	public void semPrintSignal() {
		if (queue.isEmpty())
			availability = true;
		else {
			semPrintWait(queue.peek());
			queue.remove().state = ProcessState.READY;
			/* remove a process P from s.queue */
			/* place process P on ready list */
		}
	}
	public void semAssignWait(Process process) {
		if (availability == true)
			availability = false;
		else {
			queue.add(process);
			process.state = ProcessState.BLOCKED;
			/* place this process in s.queue */
			/* block this process */
		}
	}
	public void semAssignSignal() {
		if (queue.isEmpty())
			availability = true;
		else {
			semAssignWait(queue.peek());
			queue.remove().state = ProcessState.READY;
			/* remove a process P from s.queue */
			/* place process P on ready list */
		}
	}
	public void semWriteWait(Process process) {
		if (availability == true)
			availability = false;
		else {
			queue.add(process);
			process.state = ProcessState.BLOCKED;
			/* place this process in s.queue */
			/* block this process */
		}
	}
	public void semWriteSignal() {
		if (queue.isEmpty())
			availability = true;
		else {
			semWriteWait(queue.peek());
			queue.remove().state = ProcessState.READY;
			/* remove a process P from s.queue */
			/* place process P on ready list */
		}
	}
	public void semReadWait(Process process) {
		if (availability == true)
			availability = false;
		else {
			queue.add(process);
			process.state = ProcessState.BLOCKED;
			/* place this process in s.queue */
			/* block this process */
		}
	}
	public void semReadSignal() {
		if (queue.isEmpty())
			availability = true;
		else {
			semReadWait(queue.peek());
			queue.remove().state = ProcessState.READY;
			/* remove a process P from s.queue */
			/* place process P on ready list */
		}
	}
}
