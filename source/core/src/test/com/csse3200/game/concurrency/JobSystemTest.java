package com.csse3200.game.concurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.extensions.GameExtension;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * There aren't a lot of tests here since some of the job system's desired behaviour is
 * performance-based and hard to verify in unit tests. For example, testing that we can run many
 * tasks efficiently without spawning extra threads.
 */
@ExtendWith(GameExtension.class)
class JobSystemTest {
  @Test
  void shouldRunTask() throws InterruptedException, ExecutionException, TimeoutException {
    CompletableFuture<Integer> future = JobSystem.launch(() -> 10);
    int result = future.get(500, TimeUnit.MILLISECONDS);

    assertEquals(10, result);
  }

  @Test
  void shouldRunBlockingTask() throws InterruptedException, ExecutionException, TimeoutException {
    CompletableFuture<Integer> future = JobSystem.launchBlocking(() -> 10);
    int result = future.get(500, TimeUnit.MILLISECONDS);

    assertEquals(10, result);
  }
}
