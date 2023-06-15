package ntzw.mfl.launch;

import java.util.concurrent.TimeUnit;

public class ProcessMonitor {

    private Process process;

    public ProcessMonitor(Process process) {
        this.process = process;
    }

    public boolean isRunning() {
        return process.isAlive();
    }

    public Integer exitCode() {
        try {
            return process.exitValue();
        } catch (IllegalThreadStateException e) {
            return null;
        }
    }

    public boolean wait(int secs) throws InterruptedException {
        return !process.waitFor(secs, TimeUnit.SECONDS);
    }

    public void waitInfinitely() throws InterruptedException {
        process.waitFor();
    }
}
