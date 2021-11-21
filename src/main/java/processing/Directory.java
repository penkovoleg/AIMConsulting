package processing;

import java.io.File;
import java.util.Scanner;

public class Directory {

    private String nameDir;
    private final Scanner scanner;

    public Directory(final Scanner scanner) {
        this.scanner = scanner;
    }

    public String getNameDir() {
        while (scanner.hasNext()) {
            nameDir = scanner.nextLine();
            File file = new File(nameDir);
            if (file.exists()) {
                return nameDir;
            } else {
                System.out.println("There are no files in the directory or wrong directory!");
            }
        }
        return nameDir;
    }
}
