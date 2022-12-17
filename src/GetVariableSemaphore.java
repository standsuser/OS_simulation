public class GetVariableSemaphore extends CountingSemaphore {

    public GetVariableSemaphore(int count) {
        super(count);
        // TODO Auto-generated constructor stub
    }

    public void semGetVariableWait(Process process) {
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

    public void semGetVariablePost() {
        count++;
        if (count <= 0) {
            queue.remove().state = ProcessState.READY;
            // remove a process P from s.queue
            // place process P on ready list
        }
    }

}
