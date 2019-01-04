package org.rcook.guava;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.rcook.App;
import org.rcook.ListenableFutureHelper;
import org.rcook.Session;

public final class GuavaFutures {
    private GuavaFutures() {
    }

    public static ListenableFuture<Integer> add(final int x, final int y) {
        final ListenableFutureHelper h = new ListenableFutureHelper(MoreExecutors.directExecutor());
        return h.catchingAsync(
                App.add2(Session.INVALID, x, y),
                e ->
                        h.transformAsync(
                                h.transform(
                                        h.transform(
                                                App.getSessionId2(Session.INVALID),
                                                Integer::parseInt),
                                        GuavaFutures::getSession),
                                session -> App.add2(session, x, y)));
    }

    private static Session getSession(final int index) {
        if (index != 123) {
            System.out.format("[%d] getSession failed%n", Thread.currentThread().getId());
            throw new RuntimeException("<invalid-token-version>");
        }

        System.out.format("[%d] getSession succeeded%n", Thread.currentThread().getId());
        return Session.VALID;
    }
}
