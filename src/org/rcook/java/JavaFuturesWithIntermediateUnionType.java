package org.rcook.java;

import org.rcook.App;
import org.rcook.Session;

import java.util.concurrent.CompletableFuture;

enum EitherType {
    VALUE,
    THROWN
}

final class Either<V> {
    private final EitherType type;
    private final V value;
    private final Throwable thrown;

    private Either(final EitherType type, final V value, final Throwable thrown) {
        this.type = type;
        this.value = value;
        this.thrown = thrown;
    }

    public static <U> Either<U> ofValue(final U value) {
        return new Either<>(EitherType.VALUE, value, null);
    }

    public static <U> Either<U> ofThrown(final Throwable thrown) {
        return new Either<>(EitherType.THROWN, null, thrown);
    }

    public EitherType getType() {
        return type;
    }

    public V getValue() {
        if (type != EitherType.VALUE) {
            throw new IllegalStateException();
        }

        return value;
    }

    public Throwable getThrown() {
        if (type != EitherType.THROWN) {
            throw new IllegalStateException();
        }

        return thrown;
    }
}

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
