package textprocessing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFrequencyParallelThreads {
    private static final int NUM_THREADS = 4;
    private static final int BUFFER_SIZE = 16 * 1024 * 1024;
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-z0-9]+");

    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.nanoTime();
        String filePath = "src/main/resources/test_7.txt";

        long fileSize = Paths.get(filePath).toFile().length();
        System.out.printf("Dimensiunea fișierului: %.2f MB%n", fileSize / (1024.0 * 1024.0));

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        Map<String, Integer> globalCount = new ConcurrentHashMap<>();

        try (FileChannel fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ)) {
            long position = 0;
            while (position < fileSize) {
                long chunkSize = Math.min(BUFFER_SIZE, fileSize - position);
                ByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, chunkSize);
                position += chunkSize;

                executor.submit(() -> processChunk(buffer, globalCount));
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        printTopWords(globalCount);

        long endTime = System.nanoTime();
        double durationInSeconds = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Timp de rulare: " + durationInSeconds + " secunde");
    }

    private static void processChunk(ByteBuffer buffer, Map<String, Integer> globalCount) {
        StringBuilder sb = new StringBuilder();
        while (buffer.hasRemaining()) {
            char c = (char) buffer.get();
            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toLowerCase(c));
            } else {
                processWord(sb.toString(), globalCount);
                sb.setLength(0);
            }
        }
        processWord(sb.toString(), globalCount);
    }

    private static void processWord(String word, Map<String, Integer> globalCount) {
        if (!word.isEmpty()) {
            globalCount.merge(word, 1, Integer::sum);
        }
    }

    private static void printTopWords(Map<String, Integer> globalCount) {
        globalCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(20)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}
