package org.rcook.java;

import org.rcook.App;
import org.rcook.Either;
import org.rcook.Session;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class JavaFuturesWithEither {
    private JavaFuturesWithEither() {
    }

    public static CompletableFuture<Integer> add(final int x, final int y) {
        return App.addAsync_Java(Session.INVALID, x, y)
                .thenApply(Either::ofValue)
                .exceptionally(Either::ofThrown)
                .thenCompose(e -> ifNotCancelledAsync(e, () -> e.isThrown()
                        ?
                        App.getSessionIdAsync_Java(Session.INVALID)
                                .thenApply(Integer::parseInt)
                                .thenApply(Session::fromIndex)
                                .thenCompose(session -> App.addAsync_Java(session, x, y))
                        :
                        CompletableFuture.completedFuture(e.getValue())));
    }

    private static <T, U> CompletableFuture<U> ifNotCancelledAsync(
            final Either<T> e,
            final Supplier<CompletableFuture<U>> cont) {
        return e.isThrownAssignableTo(CancellationException.class)
                ? CompletableFuture.failedFuture(e.getThrown())
                : cont.get();
    }
}
