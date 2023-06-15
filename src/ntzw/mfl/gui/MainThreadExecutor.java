package ntzw.mfl.gui;

import java.util.LinkedList;
import java.util.concurrent.Executor;

public class MainThreadExecutor implements Executor, Runnable {

    private final LinkedList<Runnable> taskList;
    private volatile boolean isRunning = false;

    MainThreadExecutor() {
        taskList = new LinkedList<>();
    }

    @Override
    public void execute(Runnable command) {
        synchronized (taskList) {
            taskList.addLast(command);
            taskList.notifyAll();
        }
    }

    public void executeAndWait(Runnable command) throws InterruptedException {
        synchronized (taskList) {
            taskList.addLast(command);
            taskList.notifyAll();
            taskList.wait();
        }
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            Runnable command;
            synchronized (taskList) {
                while (taskList.isEmpty() && isRunning) {
                    taskList.notifyAll();
                    try {
                        taskList.wait();
                    } catch (InterruptedException e) {
                        //
                    }
                }
                command = taskList.poll();
            }
            if(command != null) {
                command.run();
            }
        }
    }

    public void stop() {
        isRunning = false;
        synchronized (taskList) {
            taskList.notifyAll();
        }
    }
}
