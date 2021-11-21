import processing.Directory;
import processing.FileRead;
import processing.FileWrite;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/*
 * default input directory: "./src/main/resources/InputFiles"
 * default output directory: "./src/main/resources/OutputFiles"
 */

public class Main {

    private static final int NUMBER_THREAD = 3;
    private static final String INPUT_DIRECTORY;
    private static final String OUTPUT_DIRECTORY;
    private static final Map<String, ConcurrentSkipListSet<String>> valuesForKey = new ConcurrentHashMap<>();

    static {
        try (Scanner scanner = new Scanner(System.in)) {
            Directory directory = new Directory(scanner);
            System.out.println("Please enter input directory:");
            INPUT_DIRECTORY = directory.getNameDir();
            System.out.println("Please enter output directory:");
            OUTPUT_DIRECTORY = directory.getNameDir();
        }
    }

    public static void main(String[] args) {
        runRead(Objects.requireNonNull(new File(INPUT_DIRECTORY).listFiles()));
        new FileWrite(OUTPUT_DIRECTORY, valuesForKey).runWriteAndPrint();
    }

    private static void runRead(final File[] inputFiles) {
        CountDownLatch doneSignal = new CountDownLatch(NUMBER_THREAD);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREAD);
        for (File inputFile : inputFiles) {
            executorService.execute(new FileRead(inputFile, valuesForKey, doneSignal));
        }
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
