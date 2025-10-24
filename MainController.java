import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController implements Initializable {

    @FXML
    private VBox vbox;

    private Parent fxml;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        animateAndLoad("SignIn.fxml", vbox.getLayoutX() * 10);
    }

    @FXML
    private void open_signin(ActionEvent event) {
        animateAndLoad("SignIn.fxml", vbox.getLayoutX() * 10);
    }

    @FXML
    private void open_signup(ActionEvent event) {
        animateAndLoad("SignUp.fxml", 0);
    }

    private void animateAndLoad(String fxmlFile, double toX) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), vbox);
        transition.setToX(toX);
        transition.play();
        transition.setOnFinished(e -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource(fxmlFile));
                vbox.getChildren().clear();
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                ex.printStackTrace(); // Consider logging this properly
            }
        });
    }
}