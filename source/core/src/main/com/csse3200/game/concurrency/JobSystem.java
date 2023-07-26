package com.csse3200.game.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * A job system provides a general-purpose way to run multi-threaded code. This is a recommended
 * approach for compute-heavy tasks which may slow down the game. When a 'job' is launched, it is
 * scheduled to be executed on an available thread at some time in the future. The job system makes
 * use of thread pooling and software tasks rather than spawning a new thread per task. See
 * Wiki/Concurrency for details.
 */
public class JobSystem {

  /**
   * Our main thread pool uses work stealing, which is based on Java's ForkJoinPool. This maintains
   * one permanent thread per CPU core, where each thread has a queue of tasks to run. Threads may
   * steal tasks from other busy threads. This means that we don't need a thread per task.
   */
  private static final ExecutorService executor = Executors.newWorkStealingPool();

  /**
   * We also maintain a second thread pool for any blocking operations, since we don't want one of
   * the main pool's threads to be blocked by one blocking task, unable to run other tasks. Blocking
   * tasks need a dedicated thread that can be blocked without affecting other tasks.
   */
  private static final ExecutorService blockingExecutor = Executors.newCachedThreadPool();

  /**
   * Launch an asynchronous job which may be run on a separate thread. The job should not block on
   * anything except other jobs, i.e. using get(). For jobs which block on I/O, delays, etc. use
   * {@link JobSystem#launchBlocking(Supplier)}.
   *
   * @param supplier Non-blocking method which is executed asynchronously.
   * @param <T> Return type of the job
   * @return A Future which evaluates to the job's return value. Calling get() will give the result
   *     of the supplied method, blocking until it's finished. Avoid calling get() in the main
   *     update loop.
   */
  public static <T> CompletableFuture<T> launch(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier, executor);
  }

  /**
   * Launch an asynchronous job which may be run on a separate thread. This is much less efficient
   * than {@link JobSystem#launch(Supplier)} since a new thread may be created for each call. Avoid
   * unless blocking is necessary.
   *
   * @param supplier Method which is executed asynchronously, and may block.
   * @param <T> Return type of the job
   * @return A Future which evaluates to the job's return value. Calling get() will give the result
   *     of the supplied method, blocking until it's finished.
   */
  public static <T> CompletableFuture<T> launchBlocking(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier, blockingExecutor);
  }

  private JobSystem() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
