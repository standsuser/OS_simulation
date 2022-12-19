public class GetVariableSemaphore extends CountingSemaphore {

    public GetVariableSemaphore(int count) {
        super(count);
    }

    public void semGetVariableWait() {
        System.out.println("SemWait GetVariable Called");
        count--;
        if (count < 0) {
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

    public void semGetVariablePost() {
        System.out.println("SemPost GetVariable Called");
        count++;
        if (count <= 0) {
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
        }
    }

}