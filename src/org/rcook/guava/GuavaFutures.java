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
                                        Session::fromIndex),
                                session -> App.add2(session, x, y)));
    }
}
