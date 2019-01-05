package org.rcook;

public enum Session {
    INVALID(false),
    VALID(true);

    private final boolean isValid;

    Session(final boolean isValid) {
        this.isValid = isValid;
    }

    public static Session fromIndex(final int index) {
        if (index != 123) {
            System.out.format("[%d] fromIndex failed%n", Thread.currentThread().getId());
            throw new RuntimeException("<invalid-index>");
        }

        System.out.format("[%d] fromIndex succeeded%n", Thread.currentThread().getId());
        return Session.VALID;
    }

    public boolean isValid() {
        return isValid;
    }
}
