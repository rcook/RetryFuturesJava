package org.rcook;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.CompletableFuture;

public final class App {
    private static final ListeningExecutorService EXECUTOR_SERVICE = MoreExecutors.newDirectExecutorService();

    private App() {
    }

    public static CompletableFuture<Integer> addAsync_Java(final Session session, final int x, final int y) {
        return CompletableFuture.supplyAsync(() -> add(session, x, y));
    }

    public static ListenableFuture<Integer> addAsync_Guava(final Session session, final int x, final int y) {
        return EXECUTOR_SERVICE.submit(() -> add(session, x, y));
    }

    public static CompletableFuture<String> getSessionIdAsync_Java(final Session session) {
        return CompletableFuture.supplyAsync(() -> getSessionId(session));
    }

    public static ListenableFuture<String> getSessionIdAsync_Guava(final Session session) {
        return EXECUTOR_SERVICE.submit(() -> getSessionId(session));
    }

    private static Integer add(final Session session, final int x, final int y) {
        if (!session.isValid()) {
            System.out.format("[%d] add failed%n", Thread.currentThread().getId());
            throw new RuntimeException("<invalid-session>");
        }

        System.out.format("[%d] add succeeded%n", Thread.currentThread().getId());
        return x + y;
    }

    private static String getSessionId(final Session session) {
        if (session.isValid()) {
            System.out.format("[%d] getSessionId failed%n", Thread.currentThread().getId());
            throw new RuntimeException("<invalid-session>");
        }

        System.out.format("[%d] getSessionId succeeded%n", Thread.currentThread().getId());
        return "123";
    }
}
