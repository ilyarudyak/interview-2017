package core_java.swing;

import java.awt.*;
import java.util.*;

import javax.swing.*;

/**
 * Created by ilyarudyak on 4/25/17.
 */
public class SwingThreadDemo {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new SwingThreadFrame();
            frame.setTitle("SwingThreadTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class SwingThreadFrame extends JFrame {
    public SwingThreadFrame() {
        final JComboBox<Integer> combo = new JComboBox<>();
        combo.insertItemAt(Integer.MAX_VALUE, 0);
        combo.setPrototypeDisplayValue(combo.getItemAt(0));
        combo.setSelectedIndex(0);

        JPanel panel = new JPanel();

        JButton goodButton = new JButton("Good");
        goodButton.addActionListener(event ->
                new Thread(new GoodWorkerRunnable(combo)).start());
        panel.add(goodButton);
        JButton badButton = new JButton("Bad");
        badButton.addActionListener(event ->
                new Thread(new BadWorkerRunnable(combo)).start());
        panel.add(badButton);

        panel.add(combo);
        add(panel);
        pack();
    }
}

/**
 * This runnable modifies a combo box by randomly adding and removing numbers.
 * This can result in errors because the combo box methods are not synchronized
 * and both the worker thread and the event dispatch thread access the combo
 * box.
 */
class BadWorkerRunnable implements Runnable {
    private JComboBox<Integer> combo;
    private Random generator;

    public BadWorkerRunnable(JComboBox<Integer> aCombo) {
        combo = aCombo;
        generator = new Random();
    }

    public void run() {
        try {
            while (true) {
                int i = Math.abs(generator.nextInt());
                if (i % 2 == 0)
                    combo.insertItemAt(i, 0);
                else if (combo.getItemCount() > 0)
                    combo.removeItemAt(i % combo.getItemCount());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
        }
    }
}

/**
 * This runnable modifies a combo box by randomly adding and removing numbers.
 * In order to ensure that the combo box is not corrupted, the editing
 * operations are forwarded to the event dispatch thread.
 */
class GoodWorkerRunnable implements Runnable {
    private JComboBox<Integer> combo;
    private Random generator;

    public GoodWorkerRunnable(JComboBox<Integer> aCombo) {
        combo = aCombo;
        generator = new Random();
    }

    public void run() {
        try {
            while (true) {
                EventQueue.invokeLater(() ->
                {
                    int i = Math.abs(generator.nextInt());
                    if (i % 2 == 0)
                        combo.insertItemAt(i, 0);
                    else if (combo.getItemCount() > 0)
                        combo.removeItemAt(i % combo.getItemCount());
                });
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
        }
    }
}

