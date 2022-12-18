import java.util.Queue;

public class PrintSemaphore extends CountingSemaphore {
    public PrintSemaphore(int count) {
        super(count);
    }

    public void semPrintWait() {
        count--;
        if (count < 0) {
        	Process currProcess = ((Process)Thread.currentThread());
        	currProcess.state = ProcessState.BLOCKED;
        	OS.blockedProcesses.add(currProcess);
            queue.add(currProcess.getProcessID());
            while (currProcess.state == ProcessState.BLOCKED) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            /* place this process in s.queue */
            /* block this process */
        }
    }

    public void semPrintPost() {
        count++;
        if (count <= 0) {
            for (Process t : OS.blockedProcesses) {
        		if (t.getProcessID()==queue.peek()) {
        			OS.blockedProcesses.remove(t);
        			OS.readyQueue.add(t);
        			t.setProcessState(ProcessState.READY);
        			queue.remove();
        			break;
        		}
        	}
        }
    }

}
