package me.hysong.dev.site.modules.docsign;

import me.hysong.libhycore.CoreLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileDeletionQueue {

    private static Thread workerThread;
    private static final String databasePath = CentralPathManager.getCentralStoragePath() + "/docsign-deletion-queue.txt";

    private static void generateIfNotExists() {
        File file = new File(databasePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String loadData() {
        generateIfNotExists();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(databasePath));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            reader.close();
            return builder.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void createWorkerThread() {
        generateIfNotExists();
        if (workerThread == null || !workerThread.isAlive()) {
            workerThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String[] lines = loadData().split("\n");

                    for (String line : lines) {
                        String[] data = line.split("</>"); // Split by banned characters for both linux and windows

                        if (data.length != 2) {
                            continue;
                        }

                        String fileName = data[0];
                        long deleteOn = Long.parseLong(data[1]);
                        if (System.currentTimeMillis() >= deleteOn) {
                            // Delete file
                            CoreLogger.print(CoreLogger.EventType.INFO, "To delete: " + fileName);
                            File file = new File(fileName);
                            if (file.exists()) {
                                if (file.delete()) {
                                    // Remove line from database
                                    removeFileFromQueue(fileName);
                                    CoreLogger.print(CoreLogger.EventType.INFO, "Deleted file: " + fileName);
                                }else{
                                    CoreLogger.print(CoreLogger.EventType.ERROR, "Failed to delete file: " + fileName);
                                }
                            }else{
                                CoreLogger.print(CoreLogger.EventType.ERROR, "File does not exist: " + fileName);
                                removeFileFromQueue(fileName);
                            }
                        }
                    }

                }
            });
            workerThread.start();
            CoreLogger.print(CoreLogger.EventType.INFO, "Created worker thread");
        }
    }

    public static void addFileToQueue(String fileName, long deleteOn) {
        createWorkerThread();
        // Add file to queue
        // Format: fileName</>deleteOn
        // Example: /opt/data/docsign/1234567890.pdf</>1234567890
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(databasePath, true));
            writer.write(fileName + "</>" + deleteOn + "");
            writer.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeFileFromQueue(String fileName) {
        generateIfNotExists();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(databasePath));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(fileName)) {
                    builder.append(line).append("\n");
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(databasePath));
            writer.write(builder.toString());

            writer.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
