import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
//import javafx.scene.control.ScrollBar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BrundleTelepodSystem extends Application {
    private boolean systemBusy = false;
    private int organismCount = 0;
    private int telepod2OrganismCount = 0;

    @Override
    public void start(Stage primaryStage) {

        final boolean[] systemReady = {false};
        TextArea terminal = new TextArea();

        terminal.setText("");

        String startupMessage =
            "BRUNDLE TELEPOD CONTROL SYSTEM\n" +
            "COPYRIGHT 1986 BRUNDLE LABORATORIES\n\n" +
            "INITIALIZING SYSTEM...\n\n" +
            "MEMORY CHECK .......... OK\n" +
            "MOLECULAR ANALYZER .... ONLINE\n" +
            "TELEPOD 1 ............. READY\n" +
            "TELEPOD 2 ............. READY\n\n" +
            "SYSTEM READY.\n";
        final int[] characterIndex = {0};

Timeline startupAnimation = new Timeline(
    new KeyFrame(Duration.millis(75), event -> {

        if (characterIndex[0] < startupMessage.length()) {

            char character = startupMessage.charAt(characterIndex[0]);

            terminal.appendText(String.valueOf(character));

            characterIndex[0]++;

        }
    })
);

startupAnimation.setCycleCount(startupMessage.length());



        terminal.setEditable(false);

     

        Label commandLabel = new Label("ENTER COMMAND: ");

        commandLabel.setStyle(
        "-fx-text-fill: #FFB000;" +
        "-fx-font-family: 'Consolas';" +
        "-fx-font-size: 18px;"
        );

        // Display the user's typed command
        Label commandInput = new Label("");

        commandInput.setStyle(
            "-fx-text-fill: #FFB000;" +
            "-fx-font-family: 'Consolas';" +
            "-fx-font-size: 18px;"
        );

       

        
        // Create the blinking block cursor
        Label cursor = new Label("█");

        cursor.setStyle(
            "-fx-text-fill: #FFB000;" +
            "-fx-font-family: 'Consolas';" +
            "-fx-font-size: 18px;"
        );

        // Make the cursor blink every half-second
        Timeline blink = new Timeline(
            new KeyFrame(Duration.seconds(0.5), event -> {
            cursor.setVisible(!cursor.isVisible());
            })
        );

        blink.setCycleCount(Timeline.INDEFINITE);
        blink.play();

        terminal.setStyle(
            "-fx-control-inner-background: black;" +
            "-fx-background-color: black;" +
            "-fx-text-fill: #FFB000;" +
            "-fx-font-family: 'Consolas';" +
            "-fx-font-size: 18px;" +
            "-fx-background-insets: 0;" +
            "-fx-background-radius: 0;" +
            "-fx-border-width: 0;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );

        
        HBox commandBox = new HBox(commandLabel, commandInput, cursor);

            commandBox.setVisible(false);
            commandBox.setManaged(false);

            commandBox.setStyle("-fx-background-color: black;");

            // Set the initial width of the command field
            commandInput.setPrefWidth(10);

            // Resize the field as the user types
            commandInput.textProperty().addListener((observable, oldText, newText) -> {

            double characterWidth = 11;

            double newWidth = Math.max(10, newText.length() * characterWidth + 5);

            commandInput.setPrefWidth(newWidth);
    
        });

        startupAnimation.setOnFinished(event -> {
    
            systemReady[0] = true;

            commandBox.setManaged(true);
            commandBox.setVisible(true);
}   
        );

        VBox root = new VBox(terminal, commandBox);

        // Make the terminal expand to fill the window
        VBox.setVgrow(terminal, Priority.ALWAYS);

        // Set the background to black
        root.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(root, 700, 500);

        scene.addEventFilter(KeyEvent.KEY_TYPED, event -> {

            if (!systemReady[0] || systemBusy) {
                event.consume();
                return;
        }

        String character = event.getCharacter();

        if (!character.isEmpty() &&
            character.charAt(0) >= 32 &&
            character.charAt(0) != 127) {

            commandInput.setText(commandInput.getText() + character);
        }
        event.consume();
    
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (!systemReady[0] || systemBusy) {
            event.consume();
            return;
        }

    if (event.getCode() == KeyCode.BACK_SPACE) {

        String text = commandInput.getText();

        if (!text.isEmpty()) {
            commandInput.setText(
                text.substring(0, text.length() - 1)
            );
        }

        event.consume();
    }  else if (event.getCode() == KeyCode.ENTER) {

    String command = commandInput.getText().trim().toUpperCase();

    if (!command.isEmpty()) {

        systemBusy = true;

        processCommand(command, terminal);
    }

    commandInput.setText("");

    event.consume();
} 
});


        scene.getStylesheets().add(
            getClass().getResource("terminal.css").toExternalForm()
        );

        primaryStage.setTitle("BRUNDLE TELEPOD SYSTEM");
        primaryStage.setScene(scene);
        primaryStage.show();
        startupAnimation.play();
    }
    
private void processCommand(String command, TextArea terminal) {

    terminal.appendText("\n> " + command + "\n");

    switch (command) {

        case "HELP":
    typeText(terminal,
        "AVAILABLE COMMANDS:\n\n" +
        "HELP        - DISPLAY AVAILABLE COMMANDS\n" +
        "STATUS      - DISPLAY SYSTEM STATUS\n" +
        "VERSION     - DISPLAY SOFTWARE VERSION\n" +
        "LOAD        - LOAD ORGANISM INTO TELEPOD 1\n" +
        "UNLOAD      - REMOVE ORGANISM FROM TELEPOD 1\n" +
        "SCAN        - SCAN BOTH TELEPODS\n" +
        "TELEPORT    - TRANSFER FROM TELEPOD 1 TO 2\n" +
        "RETURN      - TRANSFER FROM TELEPOD 2 TO 1\n" +
        "DIAGNOSTICS - RUN SYSTEM DIAGNOSTICS\n",
        () -> systemBusy = false
    );
    break;

        case "STATUS":
            typeText(terminal,
                "TELEPOD 1 ........ READY\n" +
                "TELEPOD 2 ........ READY\n" +
                "MOLECULAR ANALYZER ONLINE\n",
                () -> systemBusy = false
            );
            break;

        case "VERSION":
            typeText(terminal,
                "BRUNDLE TELEPOD SYSTEM VERSION 1.0\n" +
                "Software Version: 1.0.0\n" +
                "Copyright 1986 Brundle Laboratories\n",
                () -> systemBusy = false        
            );
            break;

        case "SCAN":

            String telepodStatus;
            String telepod2Status;

            if (organismCount == 0) {
                telepodStatus = "EMPTY";
            } else if (organismCount == 1) {
                telepodStatus = "OCCUPIED - ONE ORGANISM";
            } else {
                telepodStatus = "WARNING - MULTIPLE ORGANISMS DETECTED";
            }

            if (telepod2OrganismCount == 0) {
                telepod2Status = "EMPTY";
            } else if (telepod2OrganismCount == 1) {
                telepod2Status = "OCCUPIED - ONE ORGANISM";
            } else {
                telepod2Status = "WARNING - MULTIPLE ORGANISMS DETECTED";
            }

            typeText(terminal,
            "INITIATING MOLECULAR SCAN...\n\n" +
            "SCANNING TELEPOD 1...\n" +
            "ANALYZING MOLECULAR STRUCTURE...\n" +
            "ORGANISMS DETECTED: " + organismCount + "\n" +
            "TELEPOD 1 STATUS: " + telepodStatus + "\n\n" +

            "SCANNING TELEPOD 2...\n" +
            "ANALYZING MOLECULAR STRUCTURE...\n" +
            "ORGANISMS DETECTED: " + telepod2OrganismCount + "\n" +
            "TELEPOD 2 STATUS: " + telepod2Status + "\n\n" +

            "MOLECULAR SCAN COMPLETE.\n",
            () -> systemBusy = false
        );
        break;
            
        case "LOAD":
            organismCount++;

            typeText(terminal,
                "ACCESSING TELEPOD 1...\n" +
                "LOADING BIOLOGICAL SPECIMEN...\n" +
                "SPECIMEN DETECTED.\n" +
                "ORGANISM COUNT: " + organismCount + "\n",
                () -> systemBusy = false
            );
            break;
            
        case "UNLOAD":

            if (organismCount > 0) {

            organismCount--;

            typeText(terminal,
                "ACCESSING TELEPOD 1...\n" +
                "REMOVING BIOLOGICAL SPECIMEN...\n" +
                "SPECIMEN REMOVED.\n" +
                "ORGANISMS REMAINING: " + organismCount + "\n",
                () -> systemBusy = false
            );

            } else {

            typeText(terminal,
                "WARNING: TELEPOD 1 IS EMPTY.\n" +
                "NO ORGANISMS AVAILABLE FOR REMOVAL.\n",
                () -> systemBusy = false
            );
           
        }
            break; 

            case "TELEPORT":

                if (organismCount == 0) {

                    typeText(terminal,
                    "INITIATING TELEPORTATION SEQUENCE...\n" +
                    "ERROR: TELEPOD 1 IS EMPTY.\n" +
                    "TELEPORTATION ABORTED.\n",
                    () -> systemBusy = false
                );

            } else if (organismCount > 1) {

                    typeText(terminal,
                    "INITIATING TELEPORTATION SEQUENCE...\n" +
                    "WARNING: MULTIPLE ORGANISMS DETECTED!\n" +
                    "MOLECULAR FUSION RISK IDENTIFIED.\n" +
                    "SAFETY INTERLOCK ACTIVATED.\n" +
                    "TELEPORTATION ABORTED.\n",
                    () -> systemBusy = false
                );

                } else if (telepod2OrganismCount > 0) {

                    typeText(terminal,
                    "INITIATING TELEPORTATION SEQUENCE...\n" +
                    "SCANNING DESTINATION TELEPOD...\n" +
                    "WARNING: TELEPOD 2 IS OCCUPIED!\n" +
                    "SAFETY INTERLOCK ACTIVATED.\n" +
                    "TELEPORTATION ABORTED.\n",
                    () -> systemBusy = false
                );

            } else if (isTeleportationSafe(organismCount, telepod2OrganismCount)) {

                    typeText(terminal,
                    "INITIATING TELEPORTATION SEQUENCE...\n" +
                    "VERIFYING MOLECULAR STRUCTURE...\n" +
                    "ORGANISM COUNT: 1\n" +
                    "SAFETY CHECK PASSED.\n" +
                    "TELEPORTATION AUTHORIZED.\n\n" +
                    "DEMATERIALIZING ORGANISM...\n" +
                    "TRANSMITTING MOLECULAR DATA...\n" +
                    "REMATERIALIZING IN TELEPOD 2...\n\n" +
                    "TELEPORTATION COMPLETE.\n",
                    () -> {
                        organismCount = 0;
                        telepod2OrganismCount = 1;
                        systemBusy = false;
                    }
                ); 
            }
            break;

            case "RETURN":

            if (telepod2OrganismCount == 0) {

                typeText(terminal,
                "INITIATING RETURN SEQUENCE...\n" +
                "ERROR: TELEPOD 2 IS EMPTY.\n" +
                "TELEPORTATION ABORTED.\n",
                () -> systemBusy = false
            );

            } else if (telepod2OrganismCount > 1) {

                typeText(terminal,
                "WARNING: MULTIPLE ORGANISMS DETECTED!\n" +
                "MOLECULAR FUSION RISK IDENTIFIED.\n" +
                "SAFETY INTERLOCK ACTIVATED.\n" +
                "TELEPORTATION ABORTED.\n",
                () -> systemBusy = false
            );

            } else if (organismCount > 0) {

                typeText(terminal,
                "WARNING: TELEPOD 1 IS OCCUPIED!\n" +
                "SAFETY INTERLOCK ACTIVATED.\n" +
                "TELEPORTATION ABORTED.\n",
                () -> systemBusy = false
            );

            } else if (isTeleportationSafe(telepod2OrganismCount, organismCount)) {

                typeText(terminal,
                "INITIATING RETURN SEQUENCE...\n" +
                "SCANNING TELEPOD 2...\n" +
                "VERIFYING MOLECULAR STRUCTURE...\n" +
                "SAFETY CHECK PASSED.\n\n" +
                "DEMATERIALIZING ORGANISM...\n" +
                "TRANSMITTING MOLECULAR DATA...\n" +
                "REMATERIALIZING IN TELEPOD 1...\n\n" +
                "RETURN TELEPORTATION COMPLETE.\n",
                () -> {
                    telepod2OrganismCount = 0;
                    organismCount = 1;
                    systemBusy = false;
                }
            );
        }

            break;

            case "DIAGNOSTICS":

            String safetyStatus;

            if (organismCount > 1 || telepod2OrganismCount > 1) {
                safetyStatus = "WARNING - MULTIPLE ORGANISMS DETECTED";
            } else if (organismCount > 0 && telepod2OrganismCount > 0) {
                safetyStatus = "WARNING - BOTH TELEPODS OCCUPIED";
            } else {
                safetyStatus = "NORMAL";
            }

            String diagnosticResult;

                if (safetyStatus.equals("NORMAL")) {
                    diagnosticResult = "ALL SYSTEMS OPERATIONAL.";
            } else {
                    diagnosticResult = "SAFETY WARNING - TELEPORTATION RESTRICTED.";
        }

            typeText(terminal,
                "BRUNDLE TELEPOD SYSTEM DIAGNOSTICS\n" +
                "----------------------------------\n\n" +
                "CHECKING SYSTEM COMPONENTS...\n\n" +
                "COMPUTER MEMORY .......... OK\n" +
                "MOLECULAR ANALYZER ....... ONLINE\n" +
                "TELEPOD 1 ............... ONLINE\n" +
                "TELEPOD 2 ............... ONLINE\n" +
                "SAFETY INTERLOCK ........ ACTIVE\n" +
                "BIOLOGICAL SAFETY ....... " + safetyStatus + "\n\n" +
                "DIAGNOSTIC TEST COMPLETE.\n" +
                "SAFETY STATUS: " + safetyStatus + "\n" +
                diagnosticResult + "\n",
                () -> systemBusy = false
            );

            break;

        default:
            typeText(terminal,
                "UNKNOWN COMMAND.\n" +
                "TYPE HELP FOR AVAILABLE COMMANDS.\n"
            , () -> systemBusy = false);
    }

    
}

private boolean isTeleportationSafe(int sourceCount, int destinationCount) {

    return sourceCount == 1 && destinationCount == 0;
}

private void typeText(TextArea terminal, String message, Runnable onFinished) {

    if (message.isEmpty()) {
        if (onFinished != null) {
            onFinished.run();
        }
        return;
    }

    final int[] index = {0};

    Timeline typingAnimation = new Timeline(
        new KeyFrame(Duration.millis(25), event -> {

            if (index[0] < message.length()) {

                terminal.appendText(
                    String.valueOf(message.charAt(index[0]))
                );

                index[0]++;
            }
        })
    );

    typingAnimation.setCycleCount(message.length());

    typingAnimation.setOnFinished(event -> {

        if (onFinished != null) {
            onFinished.run();
        }
    });

    typingAnimation.play();

}
    public static void main(String[] args) {
        launch(args);
    }
}