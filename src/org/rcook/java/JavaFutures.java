package org.rcook.java;

import org.rcook.App;
import org.rcook.Session;

import java.util.concurrent.CompletableFuture;

public final class JavaFutures {
    private JavaFutures() {
    }

    public static CompletableFuture<Integer> add(final int x, final int y) {
        return App.addAsync_Java(Session.INVALID, x, y)
                .exceptionally(e -> null)
                .thenCompose(response -> response == null
                        ?
                        App.getSessionIdAsync_Java(Session.INVALID)
                                .thenApply(Integer::parseInt)
                                .thenApply(Session::fromIndex)
                                .thenCompose(session -> App.addAsync_Java(session, x, y))
                        :
                        CompletableFuture.completedFuture(response));
    }
}
