package org.example;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class task_2 {
    private static final String[] BOOK_URLS = {
            "https://iknigi.net/otzivi-na-knigi/page/\"+page+\"/"
    };

    private static final String[] SHOP_URLS = {
            "https://api.example.com/shop_reviews?page=1",
            "https://api.example.com/shop_reviews?page=2",
            "https://api.example.com/shop_reviews?page=3"
    };

    public static void main(String[] args) {
        int[] threadCounts = {1, 2, 4, 8, 16};
        JSONObject resultsData = new JSONObject();

        for (int count : threadCounts) {
            System.out.println("Testing with " + count + " threads...");
            long timeBooks = runExperiment(BOOK_URLS, count);
            long timeShops = runExperiment(SHOP_URLS, count);

            JSONObject result = new JSONObject();
            result.put("time_books", timeBooks);
            result.put("time_shops", timeShops);

            resultsData.put(String.valueOf(count), result);
        }

        try (FileWriter file = new FileWriter("results.json")) {
            file.write(resultsData.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Experiment completed. Results saved in results.json.");
    }

    private static long runExperiment(String[] urls, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Long> times = new ArrayList<>();

        for (String url : urls) {
            executor.execute(() -> {
                long startTime = System.currentTimeMillis();
                fetchReviews(url);
                long endTime = System.currentTimeMillis();
                synchronized (times) {
                    times.add(endTime - startTime);
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return times.stream().mapToLong(Long::longValue).sum();
    }

    private static void fetchReviews(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Error: " + responseCode + " - " + conn.getResponseMessage());
            }
            conn.disconnect();
        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
        }
    }
}
