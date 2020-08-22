package shopProduct.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shopProduct.controllers.MainController;

import java.io.IOException;

public class Main extends Application {

    private final String FXML_MAIN = "../fxml/main.fxml";
    private final String NAME_PROGRAM = "Product book";

    private Stage primaryStage;
    private Parent fxmlMain;
    private MainController mainController;
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        loadFXML();

        mainController.setMainStage(primaryStage);

        createGUI();
    }

    private void loadFXML() {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(FXML_MAIN));

        try {
            fxmlMain = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainController = fxmlLoader.getController();
    }

    private void createGUI() {
        primaryStage.setTitle(NAME_PROGRAM);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(new Scene(fxmlMain, 300, 275));
        primaryStage.show();
    }

}
