package pl.ketom.crawlingworms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main {
    public static void main(String[] args) {
        App.main(args);
    }

    public static class App extends Application {
        public static void main(String[] args) {
            App.launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("stage.fxml"));
            Parent root = fxmlLoader.load();

            primaryStage.setTitle("Crawling Worms");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();

            Controller controller = fxmlLoader.getController();
            primaryStage.setOnCloseRequest(event -> controller.onClose());
        }
    }
}
