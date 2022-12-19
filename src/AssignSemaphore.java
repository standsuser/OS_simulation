
public class AssignSemaphore extends BinarySemaphore {

    public void semAssignWait() {
        System.out.println("SemWait Assign Called");

        if (availability == true)
            availability = false;
        else {
            Process currProcess = ((Process) Thread.currentThread());
            queue.add(currProcess.getProcessID());
            OS.blockedProcesses.add(currProcess);
            System.out.println("Process" + OS.currProc.getProcessID() + " has been Blocked");
            currProcess.setProcessState(ProcessState.BLOCKED);
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
        System.out.println("SemPost Assign Called");

        if (queue.isEmpty())
            availability = true;
        else {
            for (Process t : OS.blockedProcesses) {
                if (t.getProcessID() == queue.peek()) {
                    OS.blockedProcesses.remove(t);
                    OS.readyQueue.add(t);
                    System.out.println("Process" + t.getProcessID() + " has been Unblocked");
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
