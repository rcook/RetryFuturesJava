# Java futures

On the relative ugliness of the composition of various implementations of the concept of "future" in God's Own Programming Language (a.k.a. Java), including:

* [`java.util.concurrent.CompletableFuture`][completable-future] [(source)][completable-future-source]
* [`com.google.common.util.concurrent.FluentFuture`][fluent-future] [(source)][fluent-future-source]
* [`com.google.common.util.concurrent.ListenableFuture`][listenable-future] [(source)][listenable-future-source]

## Licence

Released under [MIT License][licence]

Copyright &copy; 2019, Richard Cook. All rights reserved.

[completable-future]: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
[completable-future-source]: src/org/rcook/java/JavaFuturesWithEither.java
[fluent-future]: https://google.github.io/guava/releases/23.0/api/docs/com/google/common/util/concurrent/FluentFuture.html
[fluent-future-source]: src/org/rcook/guava/GuavaFluentFutures.java
[licence]: LICENSE
[listenable-future]: https://google.github.io/guava/releases/23.0/api/docs/com/google/common/util/concurrent/ListenableFuture.html
[listenable-future-source]: src/org/rcook/guava/GuavaFutures.java
