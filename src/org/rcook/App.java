package org.rcook;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.CompletableFuture;

public final class App {
    private static final ListeningExecutorService EXECUTOR_SERVICE = MoreExecutors.newDirectExecutorService();

    private App() {
    }

    public static CompletableFuture<Integer> add(final Session session, final int x, final int y) {
        return CompletableFuture.supplyAsync(() -> addSync(session, x, y));
    }

    public static ListenableFuture<Integer> add2(final Session session, final int x, final int y) {
        return EXECUTOR_SERVICE.submit(() -> addSync(session, x, y));
    }

    public static CompletableFuture<String> getSessionId(final Session session) {
        return CompletableFuture.supplyAsync(() -> getSessionIdSync(session));
    }

    public static ListenableFuture<String> getSessionId2(final Session session) {
        return EXECUTOR_SERVICE.submit(() -> getSessionIdSync(session));
    }

    private static Integer addSync(final Session session, final int x, final int y) {
        if (!session.isValid()) {
            System.out.format("[%d] addSync failed%n", Thread.currentThread().getId());
            throw new RuntimeException("<invalid-session>");
        }

        System.out.format("[%d] addSync succeeded%n", Thread.currentThread().getId());
        return x + y;
    }

    private static String getSessionIdSync(final Session session) {
        if (session.isValid()) {
            System.out.format("[%d] getSessionIdSync failed%n", Thread.currentThread().getId());
            throw new RuntimeException("<invalid-session>");
        }

        System.out.format("[%d] getSessionIdSync succeeded%n", Thread.currentThread().getId());
        return "123";
    }
}
