package tima;

import tima.global.ConfigLoader;
import tima.wordcount.ThreadWordCount;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCountApp {
    public static void main(String[] args) throws IOException {

        Properties properties = ConfigLoader.getInstance().getProperties();
        String fileName = properties.getProperty("file_path");
        int numThreads = Integer.parseInt(properties.getProperty("num_threads"));
        int chunksize = Integer.parseInt(properties.getProperty("chunksize"));

        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        BufferedReader reader = null;
        ConcurrentMap<String,Integer> m = new ConcurrentHashMap<String,Integer>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while (true) {
                String res = ThreadWordCount.readFileAsString(reader,chunksize);
                if (res.equals("")) {
                    break;
                }
                pool.submit(new ThreadWordCount(res,m));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            reader.close();
        }
        pool.shutdown();
        try {
            pool.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("Pool interrupted!");
            System.exit(1);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("input/output.txt", true));

        int total = 0;
        for (Map.Entry<String,Integer> entry : m.entrySet()) {
            int count = entry.getValue();
            total += 1;
            String strWord = String.format("%-30s %d\n", entry.getKey(),count);
            writer.write(strWord);

        }
        writer.close();
        System.out.println("Total words = " + total);
    }
}
