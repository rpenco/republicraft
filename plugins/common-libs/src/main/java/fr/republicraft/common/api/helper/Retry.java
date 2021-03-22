package fr.republicraft.common.api.helper;

public class Retry {


    public static boolean execute(int maxTries, long wait, Consumer consumer) throws InterruptedException {
        int count = 0;
        while (true) {
            try {
                if (consumer.execute(maxTries, wait, count)) {
                    return true;
                }
            } catch (Exception e) {
                // do nothing
            }

            Thread.sleep(wait);
            if (++count == maxTries) throw new RuntimeException("execution retry attempted");
        }
    }

    @FunctionalInterface
    public interface Consumer {
        boolean execute(int maxTries, long delay, int currentTry) throws Exception;
    }
}
