import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class WordFrequencyWorker implements Runnable {
    private String[] lines;
    private int start;
    private int end;
    private ConcurrentHashMap<String, Integer> wordFrequencyMap;

    public WordFrequencyWorker(String[] lines, int start, int end, ConcurrentHashMap<String, Integer> wordFrequencyMap) {
        this.lines = lines;
        this.start = start;
        this.end = end;
        this.wordFrequencyMap = wordFrequencyMap;
    }

    @Override
    public void run() {
        Map<String, Integer> localWordFrequency = new HashMap<>();

        // Process the assigned chunk of lines
        for (int i = start; i < end; i++) {
            if (lines[i] != null) {
                String[] words = lines[i].toLowerCase().split("\\W+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        localWordFrequency.put(word, localWordFrequency.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }

        // Update the shared word frequency map (thread-safe)
        for (Map.Entry<String, Integer> entry : localWordFrequency.entrySet()) {
            wordFrequencyMap.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }
}

