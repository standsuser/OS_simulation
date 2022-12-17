import java.util.Queue;

public class ReadSemaphore extends CountingSemaphore {
    public ReadSemaphore(int count) {
        super(count);
        // TODO Auto-generated constructor stub
    }

    public void semReadWait(Process process) {
        count--;
        if (count < 0) {
            process.state = ProcessState.BLOCKED;
            queue.add(process);
            while (process.state == ProcessState.BLOCKED) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            // place this process in s.queue
            // block this process
        }
    }

    public void semReadPost() {
        count++;
        if (count <= 0) {
            queue.remove().state = ProcessState.READY;
            // remove a process P from s.queue
            // place process P on ready list
        }
    }

}
