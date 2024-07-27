import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;

public class ScientificCalculator extends Application {

    private TextField display;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Scientific Calculator");

        // Create the display
        display = new TextField();
        display.setEditable(false);
        display.setPrefColumnCount(10);

        // Create the number buttons
        Button[] numberButtons = new Button[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new Button(Integer.toString(i));
            numberButtons[i].setOnAction(this::handleDigitButtonClick);
        }

        // Create the operator buttons
        Button addButton = createButton("+", this::handleOperatorButtonClick);
        Button subtractButton = createButton("-", this::handleOperatorButtonClick);
        Button multiplyButton = createButton("*", this::handleOperatorButtonClick);
        Button divideButton = createButton("/", this::handleOperatorButtonClick);
        Button equalsButton = createButton("=", this::handleEqualsButtonClick);
        Button clearButton = createButton("C", this::handleClearButtonClick);

        // Create the scientific buttons
        Button sqrtButton = createUnaryOperatorButton("√", Math::sqrt);
        Button powerButton = createButton("pow", this::handlePowerButtonClick);
        Button sinButton = createUnaryOperatorButton("sin", Math::sin);
        Button cosButton = createUnaryOperatorButton("cos", Math::cos);
        Button tanButton = createUnaryOperatorButton("tan", Math::tan);
        Button logButton = createUnaryOperatorButton("log", Math::log);

        // Create the layout
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Add components to the layout
        grid.add(display, 0, 0, 4, 1);
        grid.add(numberButtons[7], 0, 1);
        grid.add(numberButtons[8], 1, 1);
        grid.add(numberButtons[9], 2, 1);
        grid.add(divideButton, 3, 1);
        grid.add(numberButtons[4], 0, 2);
        grid.add(numberButtons[5], 1, 2);
        grid.add(numberButtons[6], 2, 2);
        grid.add(multiplyButton, 3, 2);
        grid.add(numberButtons[1], 0, 3);
        grid.add(numberButtons[2], 1, 3);
        grid.add(numberButtons[3], 2, 3);
        grid.add(subtractButton, 3, 3);
        grid.add(numberButtons[0], 0, 4);
        grid.add(clearButton, 1, 4);
        grid.add(equalsButton, 2, 4);
        grid.add(addButton, 3, 4);
        grid.add(sqrtButton, 0, 5);
        grid.add(powerButton, 1, 5);
        grid.add(sinButton, 2, 5);
        grid.add(cosButton, 3, 5);
        grid.add(tanButton, 0, 6);
        grid.add(logButton, 1, 6);

        // Create the scene
        Scene scene = new Scene(grid, 300, 400);

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private Button createButton(String text, javafx.event.EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        return button;
    }

    private Button createUnaryOperatorButton(String text, UnaryOperator<Double> operator) {
        Button button = createButton(text, event -> {
            String currentText = display.getText();
            if (!currentText.isEmpty()) {
                double operand = Double.parseDouble(currentText);
                double result = operator.apply(operand);
                display.setText(String.valueOf(result));
            }
        });
        return button;
    }

    private void handleDigitButtonClick(ActionEvent event) {
        Button source = (Button) event.getSource();
        display.appendText(source.getText());
    }

    private void handleOperatorButtonClick(ActionEvent event) {
        Button source = (Button) event.getSource();
        display.appendText(" " + source.getText() + " ");
    }

    private void handleEqualsButtonClick(ActionEvent event) {
        String expression = display.getText();
        try {
            // Evaluate the expression and display the result
            String result = String.valueOf(eval(expression));
            display.setText(result);
        } catch (Exception e) {
            // Handle invalid expressions
            display.setText("Error");
        }
    }

    private void handleClearButtonClick(ActionEvent event) {
        display.clear();
    }

    private void handlePowerButtonClick(ActionEvent event) {
        display.appendText("pow");
    }

    private double eval(String expression) {
        // Check if the expression contains "pow" in the format "2pow3"
        if (expression.contains("pow")) {
            String[] parts = expression.split("pow");
            if (parts.length == 2) {
                double base = Double.parseDouble(parts[0].trim());
                double exponent = Double.parseDouble(parts[1].trim());
                return Math.pow(base, exponent);
            } else {
                throw new IllegalArgumentException("Invalid 'pow' expression format");
            }
        }
    
        // If it's a regular arithmetic expression, evaluate it
        String[] tokens = expression.split(" ");
        double operand1 = Double.parseDouble(tokens[0]);
        String operator = tokens[1];
        double operand2 = Double.parseDouble(tokens[2]);
    
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    throw new ArithmeticException("Division by zero");
                }
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
    
}