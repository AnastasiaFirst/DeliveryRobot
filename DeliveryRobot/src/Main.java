import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countOccurrences(route, 'R');
                updateFrequency(countR);
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        printResults();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countOccurrences(String route, char character) {
        int count = 0;
        for (char c : route.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }

    public static void updateFrequency(int count) {
        synchronized (sizeToFreq) {
            sizeToFreq.put(count, sizeToFreq.getOrDefault(count, 0) + 1);
        }
    }

    public static void printResults() {
        int mostFrequentCount = -1;
        int mostFrequentSize = 0;

        synchronized (sizeToFreq) {
            for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                if (entry.getValue() > mostFrequentCount) {
                    mostFrequentCount = entry.getValue();
                    mostFrequentSize = entry.getKey();
                }
            }
        }

        System.out.println("Самое частое количество повторений " + mostFrequentSize + " (встретилось " + mostFrequentCount + " раз)");
        System.out.println("Другие размеры:");

        synchronized (sizeToFreq) {
            for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                if (entry.getKey() != mostFrequentSize) {
                    System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
                }
            }
        }
    }
}
