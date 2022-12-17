import java.util.Queue;

public class AssignSemaphore extends BinarySemaphore {

    public void semAssignWait(Process process) {
        if (availability == true)
            availability = false;
        else {
            process.state = ProcessState.BLOCKED;
            queue.add(process);
            while (process.state == ProcessState.BLOCKED) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            /* place this process in s.queue */
            /* block this process */
        }
    }

    public void semAssignPost() {
        if (queue.isEmpty())
            availability = true;
        else {
            queue.remove().state = ProcessState.READY;
            /*
             * remove a process P from s.queue /
             * / place process P on ready list
             */
        }
    }
}
