package org.sorus.launchwrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Printer {

    private static File file = new File("out.txt");
    private static FileOutputStream print;

    static {
        try {
            print = new FileOutputStream(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    print.close();
                } catch(IOException ioException) {
                    ioException.printStackTrace();
                }
            }));
        }
    }

    public static void println(String string) {
        try {
            print.write((string + "\n").getBytes());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
