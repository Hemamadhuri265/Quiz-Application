import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Questions extends JFrame implements ActionListener {
    private int currentQuestionIndex = 0;
    private int score = 0;

    private String[][] questions = {
            {"What is the default value of a boolean variable in Java?", "true", "false", "0", "null", "false"},
            {"Which of the following is not a Java keyword?", "class", "interface", "extends", "define", "define"},
            {"Which method is the entry point for any Java program?", "main", "start", "entry", "run", "main"},
            {"What is the default value of a byte variable in Java?", "0", "null", "undefined", "1", "0"},
            {"Which package is automatically imported in all Java programs?", "java.awt", "java.lang", "java.io", "java.net", "java.lang"},
            {"Which keyword is used to create an object in Java?", "new", "class", "static", "this", "new"},
            {"What is the size of int in Java?", "2 bytes", "4 bytes", "8 bytes", "16 bytes", "4 bytes"},
            {"Which exception is thrown when an array is accessed out of its bounds?", "IOException", "ArrayIndexOutOfBoundsException", "NullPointerException", "ArithmeticException", "ArrayIndexOutOfBoundsException"},
            {"Which operator is used to concatenate strings in Java?", "+", "&", "&&", "||", "+"},
            {"What is the base class of all classes in Java?", "Object", "String", "Class", "Main", "Object"},
            {"Which keyword is used to inherit a class in Java?", "implements", "extends", "inherits", "super", "extends"},
            {"What is the return type of a constructor?", "void", "int", "float", "No return type", "No return type"},
            {"Which keyword is used to define a constant in Java?", "const", "final", "static", "immutable", "final"},
            {"Which method is used to convert a string to lowercase?", "toLower()", "lower()", "toLowerCase()", "convertToLower()", "toLowerCase()"},
            {"Which data type is used to store decimal numbers in Java?", "int", "double", "String", "byte", "double"}
    };

    private String[][] options = new String[questions.length][4];
    private JRadioButton[] radioButtons = new JRadioButton[4];
    private ButtonGroup group;
    private JButton nextButton;
    private JLabel questionLabel;

    private String[] selectedAnswers = new String[questions.length];

    public Questions() {
        setTitle("Java Quiz");
        setSize(900, 700);  // Set frame size to 900 x 700
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize options for each question
        for (int i = 0; i < questions.length; i++) {
            System.arraycopy(questions[i], 1, options[i], 0, 4);  // Copy options to the options array
        }

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));  // Set question text size to 18
        radioButtons[0] = createNormalRadioButton();
        radioButtons[1] = createNormalRadioButton();
        radioButtons[2] = createNormalRadioButton();
        radioButtons[3] = createNormalRadioButton();

        group = new ButtonGroup();
        for (JRadioButton rb : radioButtons) {
            group.add(rb);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        nextButton.setPreferredSize(new Dimension(90, 30));  // Smaller size for Next button
        nextButton.setBackground(new Color(34, 139, 34));  // Green background for button
        nextButton.setForeground(Color.WHITE);  // White text for button
        nextButton.setBorder(BorderFactory.createLineBorder(new Color(45, 75, 85)));  // Green border

        // Create a panel for the question and radio buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 5, 10));  // Set horizontal and vertical gap (5 and 10)
        panel.setBackground(Color.WHITE);  // White background for the panel
        panel.add(questionLabel);
        for (JRadioButton rb : radioButtons) {
            panel.add(rb);
        }

        // Panel for next button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Arrange buttons in a row
        buttonPanel.setBackground(Color.WHITE);  // White background for button panel
        buttonPanel.add(nextButton);

        panel.add(buttonPanel);  // Add the button panel

        add(panel);
        loadQuestion();
        setVisible(true);
    }

    // Helper method to create a normal radio button (default style)
    private JRadioButton createNormalRadioButton() {
        JRadioButton radioButton = new JRadioButton();
        radioButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Modern font
        radioButton.setBackground(Color.WHITE); // White background for the radio button container
        radioButton.setForeground(Color.BLACK); // Black text color
        radioButton.setFocusPainted(false); // Remove focus border
        radioButton.setBorder(BorderFactory.createLineBorder(new Color(45, 75, 85), 2)); // Green border color
        radioButton.setMargin(new Insets(5, 10, 5, 10)); // Padding inside the button
        radioButton.setOpaque(true);
        radioButton.setRolloverEnabled(true); // Make the button change on hover
        radioButton.addActionListener(e -> {
            // Change the color of the text when the radio button is selected
            if (radioButton.isSelected()) {
                radioButton.setForeground(Color.BLUE); // Change text color to blue
            } else {
                radioButton.setForeground(Color.BLACK); // Revert text color when unselected
            }
        });
        return radioButton;
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.length && currentQuestionIndex >= 0) {
            questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + questions[currentQuestionIndex][0]);
            questionLabel.setForeground(Color.BLACK); // Set question text to black

            for (int i = 0; i < 4; i++) {
                radioButtons[i].setText(options[currentQuestionIndex][i]);
                radioButtons[i].setSelected(false); // Reset selection for each radio button
                radioButtons[i].setForeground(Color.BLACK); // Reset text color
            }

            // If an answer is already selected for this question, select it
            if (selectedAnswers[currentQuestionIndex] != null) {
                for (int i = 0; i < 4; i++) {
                    if (radioButtons[i].getText().equals(selectedAnswers[currentQuestionIndex])) {
                        radioButtons[i].setSelected(true); // Re-select the previous choice
                        radioButtons[i].setForeground(Color.BLUE); // Set color to blue for selected answer
                        break;
                    }
                }
            }

            group.clearSelection(); // Clear the selection of the button group to handle the selection properly
        } else {
            // When quiz finishes, show the score and correct answers
            JOptionPane.showMessageDialog(this, "Quiz finished! Your score: " + score + "/" + questions.length);
            showResults();
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            handleNext();
        }
    }

    private void handleNext() {
        JRadioButton selectedButton = null;
        for (JRadioButton rb : radioButtons) {
            if (rb.isSelected()) {
                selectedButton = rb;
                break;
            }
        }

        if (selectedButton != null) {
            String selectedAnswer = selectedButton.getText();
            selectedAnswers[currentQuestionIndex] = selectedAnswer;  // Store the answer
            if (selectedAnswer.equals(questions[currentQuestionIndex][5])) {
                score++;
            }
            currentQuestionIndex++;
            loadQuestion();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an answer before proceeding.");
        }
    }

    private void showResults() {
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("Your Final Score: " + score + "/" + questions.length + "\n\n");

        for (int i = 0; i < questions.length; i++) {
            resultMessage.append("Q" + (i + 1) + ": " + questions[i][0] + "\n");
            resultMessage.append("Your Answer: " + selectedAnswers[i] + "\n");
            resultMessage.append("Correct Answer: " + questions[i][5] + "\n\n");
        }

        if (score == questions.length) {
            resultMessage.append("Excellent! You've answered all questions correctly!");
        } else if (score >= questions.length / 2) {
            resultMessage.append("Good job! You got more than half correct.");
        } else {
            resultMessage.append("Don't worry, try again to improve your score!");
        }

        JOptionPane.showMessageDialog(this, resultMessage.toString());
    }

    public static void main(String[] args) {
        new Questions();
    }
}

