
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class OnlineExamApp {
    private static int timeLeft = 2 * 60; // ⏱️ 2 minutes in seconds
    private static java.util.Timer timer;
    private static JLabel timerLabel;
    private static JFrame frame;
    private static int currentQuestion = 0;

    private static java.util.List<Question> questionList = new ArrayList<>();
    private static ButtonGroup optionsGroup;
    private static JRadioButton[] radioButtons = new JRadioButton[4];
    private static JPanel questionPanel;
    private static int[] userAnswers;

    public static void main(String[] args) {
        loadQuestions("questions.txt");

        if (questionList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No questions loaded.");
            System.exit(1);
        }

        userAnswers = new int[questionList.size()];
        Arrays.fill(userAnswers, -1);

        frame = new JFrame("Online Exam System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        timerLabel = new JLabel("Time Left: " + formatTime(timeLeft), JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(timerLabel, BorderLayout.NORTH);

        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        frame.add(questionPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> submitExam());
        frame.add(submitButton, BorderLayout.SOUTH);

        loadQuestion(currentQuestion);
        startTimer();

        frame.setVisible(true);
    }

    private static void loadQuestions(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String qText = null;
            String[] opts = null;
            int correct = -1;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Q:")) {
                    qText = line.substring(2).trim();
                } else if (line.startsWith("A:")) {
                    opts = line.substring(2).split(",");
                } else if (line.startsWith("C:")) {
                    correct = Integer.parseInt(line.substring(2).trim());
                    if (qText != null && opts != null && opts.length == 4) {
                        questionList.add(new Question(qText, opts, correct));
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void loadQuestion(int index) {
        questionPanel.removeAll();
        Question q = questionList.get(index);

        JLabel qLabel = new JLabel("Q" + (index + 1) + ": " + q.questionText);
        qLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        questionPanel.add(qLabel);

        optionsGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            radioButtons[i] = new JRadioButton(q.options[i]);
            optionsGroup.add(radioButtons[i]);
            questionPanel.add(radioButtons[i]);
            if (userAnswers[index] == i) {
                radioButtons[i].setSelected(true);
            }
        }

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout());

        // Back button
        if (index > 0) {
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                saveAnswer(index);
                currentQuestion--;
                loadQuestion(currentQuestion);
            });
            navPanel.add(backButton);
        }

        // Next or Finish
        JButton nextButton = new JButton((index == questionList.size() - 1) ? "Finish" : "Next");
        nextButton.addActionListener(e -> {
            saveAnswer(index);
            if (index < questionList.size() - 1) {
                currentQuestion++;
                loadQuestion(currentQuestion);
            } else {
                submitExam();
            }
        });

        navPanel.add(nextButton);
        questionPanel.add(navPanel);

        frame.revalidate();
        frame.repaint();
    }

    private static void saveAnswer(int index) {
        for (int i = 0; i < 4; i++) {
            if (radioButtons[i].isSelected()) {
                userAnswers[index] = i;
                break;
            }
        }
    }

    private static void startTimer() {
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeLeft--;
                timerLabel.setText("Time Left: " + formatTime(timeLeft));
                if (timeLeft <= 0) {
                    timer.cancel();
                    JOptionPane.showMessageDialog(frame, "Time's up! Auto-submitting exam.");
                    submitExam();
                }
            }
        }, 1000, 1000);
    }

    private static String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private static void submitExam() {
        if (timer != null) timer.cancel();

        int score = 0;
        for (int i = 0; i < questionList.size(); i++) {
            if (userAnswers[i] == questionList.get(i).correctOption) {
                score++;
            }
        }

        JOptionPane.showMessageDialog(frame, "Your Score: " + score + "/" + questionList.size());
        frame.dispose();
    }
}

class Question {
    String questionText;
    String[] options;
    int correctOption;

    Question(String questionText, String[] options, int correctOption) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
    }
}
