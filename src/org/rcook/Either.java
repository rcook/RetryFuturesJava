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
        if (thrown == null) {
            throw new IllegalArgumentException();
        }

        return new Either<>(EitherType.THROWN, null, thrown);
    }

    @Override
    public String toString() {
        switch (type) {
            case VALUE:
                return String.format("[Value %s]", value);
            case THROWN:
                return String.format("[Thrown %s]", thrown);
            default:
                throw new IllegalStateException();
        }
    }

    public EitherType getType() {
        return type;
    }

    public V getValue() {
        if (!isValue()) {
            throw new IllegalStateException();
        }

        return value;
    }

    public Throwable getThrown() {
        if (!isThrown()) {
            throw new IllegalStateException();
        }

        return thrown;
    }

    public boolean isValue() {
        return type == EitherType.VALUE;
    }

    public boolean isThrown() {
        return type == EitherType.THROWN;
    }

    public <T> boolean isThrownAssignableTo(final Class<T> cls) {
        return isThrown() && cls.isAssignableFrom(thrown.getClass());
    }
}
