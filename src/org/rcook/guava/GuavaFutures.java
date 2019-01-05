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
                App.addAsync_Guava(Session.INVALID, x, y),
                e ->
                        h.transformAsync(
                                h.transform(
                                        h.transform(
                                                App.getSessionIdAsync_Guava(Session.INVALID),
                                                Integer::parseInt),
                                        Session::fromIndex),
                                session -> App.addAsync_Guava(session, x, y)));
    }
}
