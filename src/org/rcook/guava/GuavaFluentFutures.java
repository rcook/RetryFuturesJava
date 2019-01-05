package org.rcook.guava;

import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.rcook.App;
import org.rcook.Session;

import java.util.concurrent.Executor;

public final class GuavaFluentFutures {
    private GuavaFluentFutures() {
    }

    public static ListenableFuture<Integer> add(final int x, final int y) {
        final Executor executor = MoreExecutors.directExecutor();
        return FluentFuture
                .from(App.add2(Session.INVALID, x, y))
                .catchingAsync(
                        RuntimeException.class,
                        e -> FluentFuture
                                .from(App.getSessionId2(Session.INVALID))
                                .transform(Integer::parseInt, executor)
                                .transform(GuavaFluentFutures::getSession, executor)
                                .transformAsync(session -> App.add2(session, x, y), executor),
                        executor);
    }

    private static Session getSession(final int index) {
        if (index != 123) {
            System.out.format("[%d] getSession failed%n", Thread.currentThread().getId());
            throw new RuntimeException("<invalid-index>");
        }

        System.out.format("[%d] getSession succeeded%n", Thread.currentThread().getId());
        return Session.VALID;
    }
}
