package org.rcook.java;

import org.rcook.App;
import org.rcook.Session;

import java.util.concurrent.CompletableFuture;

public final class JavaFutures {
    private JavaFutures() {
    }

    public static CompletableFuture<Integer> add(final int x, final int y) {
        return App.add(Session.INVALID, x, y)
                .exceptionally(e -> null)
                .thenCompose(response -> response == null
                        ?
                        App.getSessionId(Session.INVALID)
                                .thenApply(Integer::parseInt)
                                .thenApply(JavaFutures::getSession)
                                .thenCompose(session -> App.add(session, x, y))
                        :
                        CompletableFuture.completedFuture(response));
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
