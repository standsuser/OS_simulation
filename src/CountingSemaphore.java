import java.util.Queue;

//zeyad: mostafa 2al its wrong but seebh for now sadge :( L RATIO+NO LIFE

public class CountingSemaphore {
	int count;
	Queue<Process> queue;
	
	public void semPrintWait(Process process) {
		count--;
		if(count<0){
			queue.add(process);
			process.state = ProcessState.BLOCKED;
			//  place this process in s.queue 
			//  block this process 
		}
			
		}
	
	
	public void semPrintSignal() {
		count++;
		if(count>0){
			semPrintWait(queue.peek());
			queue.remove().state = ProcessState.READY;
			//  remove a process P from s.queue 
			//  place process P on ready list 
		}
		}
	

	public void semAssignWait(Process process) {
		count--;
		if(count<0){
			queue.add(process);
			process.state = ProcessState.BLOCKED;
			//  place this process in s.queue 
			//  block this process 
		}
	}

	public void semAssignSignal() {
		count++;
		if(count>0){
			semAssignWait(queue.peek());
			queue.remove().state = ProcessState.READY;
			//  remove a process P from s.queue 
			//  place process P on ready list 
		}
	}

	public void semWriteWait(Process process) {
		count--;
		if(count<0){
			queue.add(process);
			process.state = ProcessState.BLOCKED;
			//  place this process in s.queue 
			//  block this process 
		}
	}

	public void semWriteSignal() {
		count++;
		if(count>0){
			semWriteWait(queue.peek());
			queue.remove().state = ProcessState.READY;
			//  remove a process P from s.queue 
			//  place process P on ready list 
		}
	}

	public void semReadWait(Process process) {
		count--;
		if(count<0){
			queue.add(process);
			process.state = ProcessState.BLOCKED;
			//  place this process in s.queue 
			//  block this process 
		}
	}

	public void semReadSignal() {
		count++;
		if(count>0){
			semReadWait(queue.peek());
			queue.remove().state = ProcessState.READY;
			//  remove a process P from s.queue 
			//  place process P on ready list 
		}
	}
}


