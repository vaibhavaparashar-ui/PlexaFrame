package com.vaibhavparashar.plexaframe.thread;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SodiumTaskQueue<T> {
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    public void submit(T task) {
        queue.add(task);
    }

    public T poll() {
        return queue.poll();
    }
}
