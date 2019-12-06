package ru.urfu.museum.utils;

import android.content.Context;
import android.os.Handler;

public abstract class DelayedTask {

    private Handler h;
    private Runnable r;
    private int delay = 0;
    private boolean complete = true;

    public DelayedTask(final Context context, int timeoutResId) {
        if (context != null) {
            h = new Handler();
            r = new Runnable() {
                public void run() {
                    if (context != null) {
                        execute();
                        complete = true;
                    }
                }
            };
            delay = Integer.parseInt(context.getResources().getString(timeoutResId));
        }
    }

    public DelayedTask(int timeout) {
        h = new Handler();
        r = new Runnable() {
            public void run() {
                execute();
                complete = true;
            }
        };
        delay = timeout;
    }

    public void start() {
        synchronized (h) {
            if (h != null && r != null) {
                complete = false;
                h.postDelayed(r, delay);
            }
        }
    }

    public void resetDelay() {
        synchronized (h) {
            if (h != null && r != null) {
                h.removeCallbacks(r);
                h.postDelayed(r, delay);
            }
        }
    }

    public void stop() {
        synchronized (h) {
            if (h != null && r != null) {
                h.removeCallbacks(r);
                complete = true;
            }
        }
    }

    public boolean isComplete() {
        return complete;
    }

    public abstract void execute();

}
