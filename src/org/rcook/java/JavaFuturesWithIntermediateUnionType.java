package org.rcook.java;

import org.rcook.App;
import org.rcook.Either;
import org.rcook.EitherType;
import org.rcook.Session;

import java.util.concurrent.CompletableFuture;

public final class JavaFuturesWithIntermediateUnionType {
    private JavaFuturesWithIntermediateUnionType() {
    }

    public static CompletableFuture<Integer> add(final int x, final int y) {
        return App.addAsync_Java(Session.INVALID, x, y)
                .thenApply(Either::ofValue)
                .exceptionally(Either::ofThrown) // How to propagate cancellation?
                .thenCompose(e -> e.getType() == EitherType.THROWN
                        ?
                        App.getSessionIdAsync_Java(Session.INVALID)
                                .thenApply(Integer::parseInt)
                                .thenApply(Session::fromIndex)
                                .thenCompose(session -> App.addAsync_Java(session, x, y))
                        :
                        CompletableFuture.completedFuture(e.getValue()));
    }
}
