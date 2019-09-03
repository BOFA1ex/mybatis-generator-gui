package com.bofa;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-27
 */
public class DialogsAction extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogsAction.class.getResource("/fxml/rootView.fxml"));
        BorderPane rootLayout = (BorderPane) loader.load();

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @Test
    public void start0() {
    }
}
