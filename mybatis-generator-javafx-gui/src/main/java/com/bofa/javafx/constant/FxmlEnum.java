package com.bofa.javafx.constant;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-29
 */
public enum FxmlEnum {

    /** FXML PATH ENUM */
    NEW_CONNECTION("fxml/dbConnectionView.fxml"),
    TABLE_LIST("fxml/tableListView.fxml"),
    APPLICATION_CONFIG_INFO("fxml/applicationConfigView.fxml");

    public String fxmlPath;

    FxmlEnum(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}
