//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Runtime extends Application {

    public static void main(String[] args)  {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader sceneRaw = new FXMLLoader(getClass().getResource("GUI/Main.fxml"));
        Parent root = sceneRaw.load();
        primaryStage.setTitle("Cryptography");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("./GUI/Resources/style.css");
        primaryStage.show();
    }
}
