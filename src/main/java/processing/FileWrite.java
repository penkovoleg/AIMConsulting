package processing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

public class FileWrite {

    private final String outputDirectory;
    private final Map<String, ConcurrentSkipListSet<String>> valuesForKey;

    public FileWrite(final String outputDirectory,
                     final Map<String, ConcurrentSkipListSet<String>> valuesForKey) {
        this.outputDirectory = outputDirectory;
        this.valuesForKey = valuesForKey;
    }

    public void runWriteAndPrint() {
        System.out.println("\nResult:");
        for (Map.Entry<String, ConcurrentSkipListSet<String>> entry : valuesForKey.entrySet()) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputDirectory
                                                                                   + "/"
                                                                                   + entry.getKey(), false))) {
                StringBuilder sb = new StringBuilder();
                sb.append(entry.getKey()).append(": ");
                for (String value : entry.getValue()) {
                    sb.append(value).append("; ");
                    bufferedWriter.write(value + ";");
                }
                System.out.println(sb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
