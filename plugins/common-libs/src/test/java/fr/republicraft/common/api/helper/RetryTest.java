package fr.republicraft.common.api.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RetryTest {

    @Test
    void testRetryThrowException()  {
        assertThrows(RuntimeException.class, () -> {
            Retry.execute(3, 1, (maxTries, wait, current) -> false);
        });
    }

    @Test
    void testRetry() throws InterruptedException {
        assertTrue(Retry.execute(3, 1, (maxTries, wait, current) -> current == 2));
    }
}
