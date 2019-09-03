package com.bofa.javafx;

import com.bofa.javafx.controller.RootViewController;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-29
 */
public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    private Stage primaryStage;
    private VBox rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("mybatisGenApp");
        initRootLayout();
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(MainApp.class, args);
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/rootView.fxml"));
            rootLayout = loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add("fxml/style.css");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            // bind the primaryStage to controller
            ((RootViewController) loader.getController()).setPrimaryStage(primaryStage);
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e), e);
        }
    }
}
