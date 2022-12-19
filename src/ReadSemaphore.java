import java.util.Queue;

public class ReadSemaphore extends CountingSemaphore {
    public ReadSemaphore(int count) {
        super(count);
        // TODO Auto-generated constructor stub
    }

    public void semReadWait() {
        count--;
        if (count < 0) {
        	Process currProcess = ((Process)Thread.currentThread());
        	currProcess.setProcessState(ProcessState.BLOCKED);
        	OS.blockedProcesses.add(currProcess);
            queue.add(currProcess.getProcessID());
            while (currProcess.getProcessState() == ProcessState.BLOCKED) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            /* place this process in s.queue */
            /* block this process */
        }
    }

    public void semReadPost() {
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
