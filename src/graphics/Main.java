package graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("bricks.fxml"));
        primaryStage.setTitle("BricksGameSystem");
        primaryStage.getIcons().add(
                new Image(
                        Main.class.getResourceAsStream( "brick-icon.png" )));
        Scene scene = new  Scene(root, 800, 650);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(816);
        primaryStage.setMinHeight(689);
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
