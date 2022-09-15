package com.example.addon.Utils;

public class Task {

    private boolean called;

    public Task() {
        this.called = false;
    }

    public void run(Runnable task) {
        if (!isCalled()) {
            task.run();
            setCalled();
        }
    }

    public void run(Runnable task, int times) {
        if (!isCalled()) {
            int i;

            for (i = 0; i < times; i++) {
                task.run();
            }

            if (i >= times) setCalled();
        }
    }

    public void reset() {
        this.called = false;
    }

    public boolean isCalled() {
        return this.called;
    }

    public void setCalled() {
        this.called = true;
    }
}
