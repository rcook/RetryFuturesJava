package org.rcook;

public enum Session {
    INVALID(false),
    VALID(true);

    private final boolean isValid;

    Session(final boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }
}
