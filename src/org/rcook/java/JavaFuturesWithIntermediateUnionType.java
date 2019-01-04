package org.rcook.java;

import org.rcook.App;
import org.rcook.Session;

import java.util.concurrent.CompletableFuture;

enum ValueOrExceptionType {
    VALUE,
    EXCEPTION
}

final class ValueOrException<V> {
    private final ValueOrExceptionType type;
    private final V value;
    private final Throwable exception;

    public ValueOrException(final V value) {
        this.type = ValueOrExceptionType.VALUE;
        this.value = value;
        this.exception = null;
    }

    public ValueOrException(final Throwable exception) {
        this.type = ValueOrExceptionType.EXCEPTION;
        this.value = null;
        this.exception = exception;
    }

    public ValueOrExceptionType getType() {
        return type;
    }

    public V getValue() {
        if (type != ValueOrExceptionType.VALUE) {
            throw new IllegalStateException();
        }

        return value;
    }

    public Throwable getException() {
        if (type != ValueOrExceptionType.EXCEPTION) {
            throw new IllegalStateException();
        }

        return exception;
    }
}

public final class JavaFuturesWithIntermediateUnionType {
    private JavaFuturesWithIntermediateUnionType() {
    }

    public static CompletableFuture<Integer> add(final int x, final int y) {
        return App.add(Session.INVALID, x, y)
                .thenApply(ValueOrException::new)
                .exceptionally(ValueOrException::new)
                .thenCompose(valueOrException -> valueOrException.getType() == ValueOrExceptionType.EXCEPTION
                        ?
                        App.getSessionId(Session.INVALID)
                                .thenApply(Integer::parseInt)
                                .thenApply(JavaFuturesWithIntermediateUnionType::getSession)
                                .thenCompose(session -> App.add(session, x, y))
                        :
                        CompletableFuture.completedFuture(valueOrException.getValue()));
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
