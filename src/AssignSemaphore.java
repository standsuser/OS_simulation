import java.util.Queue;

public class AssignSemaphore extends BinarySemaphore {

    public void semAssignWait() {
        if (availability == true)
            availability = false;
        else {
            Process currProcess = ((Process) Thread.currentThread());
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

    public void semAssignPost() {
        if (queue.isEmpty())
            availability = true;
        else {
            for (Process t : OS.blockedProcesses) {
                if (t.getProcessID() == queue.peek()) {
                    OS.blockedProcesses.remove(t);
                    OS.readyQueue.add(t);
                    t.setProcessState(ProcessState.READY);
                    queue.remove();
                    break;
                }
            }
            /*
             * remove a process P from s.queue /
             * / place process P on ready list
             */
        }
    }
}
