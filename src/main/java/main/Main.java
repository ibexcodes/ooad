package main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/loginScene.fxml"));
		primaryStage.setTitle("Login");
		primaryStage.setScene(new Scene(root, 400, 300));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
