package graphics;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * @author Paweł Zych
 */

public class Dialogs {

    public int showIntValueSelectDialog(String title, String headerText, int defaultValue, int min, int max){
        TextInputDialog dialog = new TextInputDialog(String.valueOf(defaultValue));
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText("between " + String.valueOf(min) + " and " + String.valueOf(max));

        Optional<String> result = dialog.showAndWait();
        int value = defaultValue;

        if (result.isPresent()) {
            try {
                value = Integer.parseInt(result.get());
            } catch (NumberFormatException e) {
                showErrorValue(result.get());
            }
            if (value > max){
                showErrorValueBound(value,true,false);
                value = defaultValue;
            }
            if (value < min){
                showErrorValueBound(value,false,true);
                value = defaultValue;
            }
        }
        return value;
    }

    private void showErrorValue(String result){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(result + " is not an number!!!");
        alert.setContentText("");
        alert.showAndWait();
    }

    private void showErrorValueBound(int value, boolean tooBig, boolean tooSmall){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if(tooBig)
            alert.setHeaderText(String.valueOf(value) + " is too Big");
        if(tooSmall)
            alert.setHeaderText(String.valueOf(value)+ " is too small!");
        alert.setContentText("");
        alert.showAndWait();
    }

    public void showErrorDialogWithStack(Exception ex, String myMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        if (myMessage == null)
            alert.setHeaderText("Oops! There was an exception.");
        else
            alert.setHeaderText(myMessage);
        alert.setContentText("Exception Type: " + ex.getClass().getSimpleName());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Exception Stack Trace:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public void showWipDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Work in progress");
        alert.setContentText("");

        alert.showAndWait();
    }

    public File showDriectoryChooser(String title, Pane component){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(new File("./test/testFiles"));
        File selectedDirectory = directoryChooser.showDialog(component.getScene().getWindow());

        if(selectedDirectory != null){
            return selectedDirectory;
        }
        return null;
    }

    public File showFileChooser(String title, Pane component, boolean save){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File("./src/archiving"));
        fileChooser.setInitialFileName("defaultLog.txt");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile;
        if (save)
            selectedFile = fileChooser.showSaveDialog(component.getScene().getWindow());
        else
            selectedFile = fileChooser.showOpenDialog(component.getScene().getWindow());

        if (selectedFile != null) {
            return selectedFile;
        }
        return null;
    }

    public void showAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Autorzy:");
        alert.setContentText("Patryk Cholewa \nPaweł Zych");
        alert.showAndWait();
    }
}