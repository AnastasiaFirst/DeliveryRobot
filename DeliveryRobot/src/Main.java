import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    // Статическая мапа для хранения частоты размеров промежутков с 'R'
    public static final ConcurrentHashMap<String, Integer> sizeToFreq = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 1000; // Количество потоков
        List<Thread> threads = new ArrayList<>();

        // Создание и запуск потоков
        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countRCommands(route);
                //System.out.println("Generated route: " + route + " | Count of 'R': " + countR);

                // Обновление мапы с частотой
                sizeToFreq.merge(String.valueOf(countR), 1, Integer::sum);
            });
            threads.add(thread);
            thread.start(); // Запускаем поток
        }

        // Ожидание завершения всех потоков
        for (Thread thread : threads) {
            thread.join();

            // Вывод результатов
            printFrequencyResults();
        }
    }

    // Метод для генерации маршрута
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    // Метод для подсчета количества 'R' в маршруте
    public static int countRCommands(String route) {
        int count = 0;
        for (char command : route.toCharArray()) {
            if (command == 'R') {
                count++;
            }
        }
        return count;
    }

    // Метод для вывода частоты результатов
    public static void printFrequencyResults() {
        // Находим самое частое количество повторений
        String mostFrequentSize = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
        int mostFrequentCount = sizeToFreq.get(mostFrequentSize);

        System.out.println("Самое частое количество повторений " + mostFrequentSize + " (встретилось " + mostFrequentCount + " раз)");

        System.out.println("Другие размеры:");
        sizeToFreq.forEach((size, freq) -> {
            if (!size.equals(mostFrequentSize)) {
                System.out.println("- " + size + " (" + freq + " раз)");
            }
        });
    }
}