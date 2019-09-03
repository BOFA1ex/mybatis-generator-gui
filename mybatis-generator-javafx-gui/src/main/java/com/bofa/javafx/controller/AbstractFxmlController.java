package com.bofa.javafx.controller;

import com.bofa.javafx.constant.FxmlEnum;
import com.bofa.javafx.util.AlertUtil;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.*;


/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-29
 */
@Data
public abstract class AbstractFxmlController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFxmlController.class);
    private static final Map<FxmlEnum, SoftReference<? extends AbstractFxmlController>> nodeCache = new HashMap<>();
    private Stage primaryStage;
    private Stage dialogStage;

    /**
     * loadFxml by {@link com.bofa.javafx.constant.FxmlEnum#fxmlPath}
     *
     * @param title    dialog title
     * @param fxmlEnum enum which stored dialog fxmlPath
     *
     * @return base fxml controller
     */
    protected AbstractFxmlController loadFxml(String title, FxmlEnum fxmlEnum) {
        SoftReference<? extends AbstractFxmlController> ref = nodeCache.get(fxmlEnum);
        if (ref != null) {
            return ref.get();
        }
        URL url = ClassLoader.getSystemResource(fxmlEnum.getFxmlPath());
        FXMLLoader loader = new FXMLLoader(url);
        try {
            Parent parentNode = loader.load();
            AbstractFxmlController controller = loader.getController();
            dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(parentNode));
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);
            controller.setDialogStage(dialogStage);
            // put into cache
            nodeCache.put(fxmlEnum, new SoftReference<>(controller));
            return controller;
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e), e);
            AlertUtil.showErrorAlert(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * if dialogStage is not null, show dialogStage
     */
    protected void showDialogStage() {
        Optional.ofNullable(dialogStage).ifPresent(Stage::show);
    }

    /**
     * if dialogStage is not null, close dialogStage
     */
    protected void closeDialogStage() {
        Optional.ofNullable(dialogStage).ifPresent(Stage::close);
    }
}
