import java.util.Queue;

public class PrintSemaphore extends CountingSemaphore {
    public PrintSemaphore(int count) {
        super(count);
    }

    public void semPrintWait(Process process) {
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
            process.state = ProcessState.RUNNING;
            // place this process in s.queue
            // block this process
        }
    }

    public void semPrintPost() {
        count++;
        if (count <= 0) {
            queue.remove().state = ProcessState.READY;
            // remove a process P from s.queue
            // place process P on ready list
        }
    }

}
