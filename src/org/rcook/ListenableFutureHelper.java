package org.rcook;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;

/**
 * Helper for composing instances of ListenableFuture
 */
public final class ListenableFutureHelper {
    private final Executor executor;

    /**
     * Constructor
     *
     * @param executor Executor
     */
    public ListenableFutureHelper(final Executor executor) {
        this.executor = executor;
    }

    /**
     * Exception-catching combinator
     *
     * @param input    Input future
     * @param fallback Fallback future
     * @param <V>
     * @return Future
     */
    public <V> ListenableFuture<V> catchingAsync(
            final ListenableFuture<V> input,
            final AsyncFunction<? super RuntimeException, ? extends V> fallback) {
        return Futures.catchingAsync(input, RuntimeException.class, fallback, executor);
    }

    /**
     * Synchronous transform combinator
     *
     * @param input    Input future
     * @param function Transform function
     * @param <I>
     * @param <O>
     * @return Future
     */
    public <I, O> ListenableFuture<O> transform(
            final ListenableFuture<I> input,
            final Function<? super I, ? extends O> function) {
        return Futures.transform(input, function, executor);
    }

    /**
     * Asynchronous transform combinator
     *
     * @param input    Input future
     * @param function Transform future
     * @param <I>
     * @param <O>
     * @return Future
     */
    public <I, O> ListenableFuture<O> transformAsync(
            final ListenableFuture<I> input,
            final AsyncFunction<? super I, ? extends O> function) {
        return Futures.transformAsync(input, function, executor);
    }
}
