package processing;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;


public class FileRead implements Runnable {

    private final File inputFile;
    private final CountDownLatch doneSignal;
    private final Map<String, ConcurrentSkipListSet<String>> valuesForKey;

    public FileRead(final File inputFile,
                    final Map<String, ConcurrentSkipListSet<String>> valuesForKey,
                    final CountDownLatch doneSignal) {
        this.inputFile = inputFile;
        this.valuesForKey = valuesForKey;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        read();
        doneSignal.countDown();
    }

    private void read() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
            String str = bufferedReader.readLine();
            String[] keys = str.split(";");
            for (String key : keys) {
                valuesForKey.putIfAbsent(key, new ConcurrentSkipListSet<>());
            }
            while ((str = bufferedReader.readLine()) != null) {
                String[] values = str.split(";");
                for (int i = 0; i < values.length; i++) {
                    valuesForKey.get(keys[i]).add(values[i]);
                }
            }
            System.out.printf("%s: " + "%s\n", Thread.currentThread().getName(), valuesForKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
