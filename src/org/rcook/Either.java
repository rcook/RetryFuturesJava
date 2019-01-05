package org.rcook;

public final class Either<V> {
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
