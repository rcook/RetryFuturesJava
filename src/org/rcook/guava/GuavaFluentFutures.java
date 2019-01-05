package org.rcook.guava;

import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.rcook.App;
import org.rcook.Session;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;

public final class GuavaFluentFutures {
    private GuavaFluentFutures() {
    }

    public static ListenableFuture<Integer> add(final int x, final int y) {
        final Executor executor = MoreExecutors.directExecutor();
        return FluentFuture
                .from(App.addAsync_Guava(Session.INVALID, x, y))
                .catchingAsync(
                        RuntimeException.class,
                        e -> e instanceof CancellationException
                                ? Futures.immediateCancelledFuture()
                                : FluentFuture
                                .from(App.getSessionIdAsync_Guava(Session.INVALID))
                                .transform(Integer::parseInt, executor)
                                .transform(Session::fromIndex, executor)
                                .transformAsync(session -> App.addAsync_Guava(session, x, y), executor),
                        executor);
    }
}
