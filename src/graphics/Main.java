package graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("bricks.fxml"));
        primaryStage.setTitle("BricksGameSystem");
        Scene scene = new  Scene(root, 800, 620);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(816);
        primaryStage.setMinHeight(659);
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
