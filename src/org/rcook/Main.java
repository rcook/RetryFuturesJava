package org.rcook;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.rcook.guava.GuavaFutures;
import org.rcook.java.JavaFutures;
import org.rcook.java.JavaFuturesWithIntermediateUnionType;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class Main {
    private Main() {
    }

    public static void main(final String[] args) {
        System.out.format("[%d] main%n", Thread.currentThread().getId());

        {
            JavaFutures.add(5, 6).handle((value, e) -> {
                if (e == null) {
                    System.out.format("value=%d%n", value);
                } else {
                    System.out.format("e=%s%n", e);
                }

                return CompletableFuture.allOf();
            }).join();
        }

        {
            JavaFuturesWithIntermediateUnionType.add(5, 6).handle((value, e) -> {
                if (e == null) {
                    System.out.format("value=%d%n", value);
                } else {
                    System.out.format("e=%s%n", e);
                }

                return CompletableFuture.allOf();
            }).join();
        }

        {
            final ListenableFuture<Integer> future = GuavaFutures.add(5, 6);
            Futures.addCallback(future, new FutureCallback<Integer>() {
                @Override
                public void onSuccess(@Nullable final Integer value) {
                    System.out.format("value=%d%n", value);
                }

                @Override
                public void onFailure(final Throwable e) {
                    System.out.format("e=%s%n", e);
                }
            }, MoreExecutors.directExecutor());
            assertGetSucceeds(future);
        }
    }

    private static <T> void assertGetFails(final ListenableFuture<T> future) {
        try {
            future.get();
            System.out.format("[%d] get succeeded unexpectedly%n", Thread.currentThread().getId());
            throw new RuntimeException("<assert-failed>");
        } catch (final ExecutionException | InterruptedException e) {
            System.out.format("[%d] get failed as expected%n", Thread.currentThread().getId());
        }
    }

    private static <T> T assertGetSucceeds(final ListenableFuture<T> future) {
        try {
            final T result = future.get();
            System.out.format("[%d] get succeeded as expected%n", Thread.currentThread().getId());
            return result;
        } catch (final ExecutionException | InterruptedException e) {
            System.out.format("[%d] get failed unexpectedly (%s)%n", Thread.currentThread().getId(), e);
            throw new RuntimeException("<assert-failed>");
        }
    }
}
