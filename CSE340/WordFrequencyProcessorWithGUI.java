import javax.swing.*;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.awt.BorderLayout;



public class WordFrequencyProcessorWithGUI extends JFrame {
    private JTextArea textArea;
    private JPanel GraphPanel;

    public WordFrequencyProcessorWithGUI() {
        // Set up the GUI components
        setTitle("Word Frequency Processor"); // No return type needed, it’s a void method
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and add the text area for word frequency output
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Create and add the custom panel for graphical output
        GraphPanel = new GraphPanel(null); // Pass necessary data to the panel if needed
        add(GraphPanel, BorderLayout.SOUTH);

        // Set the window size
        setSize(800, 600);
        setLocationRelativeTo(null);  // Center the window
    }

    // Shared map for word frequency, accessible to all threads
    private static ConcurrentHashMap<String, Integer> wordFrequencyMap = new ConcurrentHashMap<>();
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) throws InterruptedException, IOException {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        // Initialize the GUI
        WordFrequencyProcessorWithGUI gui = new WordFrequencyProcessorWithGUI();

        // Read the large text file
        String filePath = "large_text_file.txt"; // Replace with your actual file path
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String[] lines = new String[100000]; // Assuming the file has at least 100,000 lines
        String line;
        int lineCount = 0;

        // Read lines from the file
        while ((line = reader.readLine()) != null && lineCount < 100000) {
            lines[lineCount] = line;
            lineCount++;
        }
        reader.close();

        // Create thread pool
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // Define the portion of lines each thread should process
        int chunkSize = lines.length / NUM_THREADS;

        // Start each thread and process its respective chunk
        for (int i = 0; i < NUM_THREADS; i++) {
            final int start = i * chunkSize;
            final int end = (i == NUM_THREADS - 1) ? lines.length : (i + 1) * chunkSize;

            // Pass the wordFrequencyMap to each worker thread
            executor.execute(new WordFrequencyWorker(lines, start, end, wordFrequencyMap));
        }

        // Shutdown the executor and wait for the tasks to complete
        executor.shutdown();
        while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            // Refresh the GUI with live word frequency updates
            gui.updateGraphicalOutput();
        }

        // After completion, show the final word frequencies in the text area
        gui.displayTextOutput();
    }

    // Method to update the GUI (not implemented yet)
    public void updateGraphicalOutput() {
        SwingUtilities.invokeLater(() -> {
            GraphPanel.repaint();  // Request a repaint to update the graphical panel
        });
    }
        // Code to refresh the graphical output (not shown in this snippet)
    

    // Method to display final text output (not implemented yet)
    public void displayTextOutput() {
        SwingUtilities.invokeLater(() -> {
        textArea.setText("");  // Clear existing content
        // Loop through the word frequency map and update the JTextArea
        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            textArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    });
        // Code to show the word frequency in the text area (not shown in this snippet)
    }


    private static void createAndShowGUI() {
        WordFrequencyProcessorWithGUI gui = new WordFrequencyProcessorWithGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(800, 600);
        gui.setVisible(true);
    }
}
