package com.jdc.neptinglibrary;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe de gestion de fil
 */
public class ThreadPoolManager {
    private ExecutorService service;

    private ThreadPoolManager() {
        int num = Runtime.getRuntime().availableProcessors() * 20;
        service = Executors.newFixedThreadPool(num);
    }

    private static final ThreadPoolManager manager = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return manager;
    }

    public void executeTask(Runnable runnable) {
        service.execute(runnable);
    }

}
