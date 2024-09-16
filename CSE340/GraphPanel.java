import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GraphPanel extends JPanel {
    public Map<String, Integer> wordFrequencyMap;

    public GraphPanel(Map<String, Integer> wordFrequencyMap) {
        this.wordFrequencyMap = wordFrequencyMap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Example drawing code (draw a bar chart or other representation)
        int x = 10;
        int y = 10;
        int barWidth = 50;
        int maxHeight = getHeight() - 20;

        // Find the maximum frequency value to scale bars
        int maxFrequency = wordFrequencyMap.values().stream().max(Integer::compare).orElse(1);

        // Draw bars
        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            String word = entry.getKey();
            int frequency = entry.getValue();
            int barHeight = (int) ((double) frequency / maxFrequency * maxHeight);

            g.setColor(Color.BLUE);
            g.fillRect(x, getHeight() - barHeight - y, barWidth, barHeight);

            g.setColor(Color.BLACK);
            g.drawRect(x, getHeight() - barHeight - y, barWidth, barHeight);

            g.drawString(word, x, getHeight() - barHeight - y - 5);

            x += barWidth + 10;
            if (x > getWidth() - barWidth) {
                x = 10;
                y += maxHeight / 5;
            }
        }
    }
}

