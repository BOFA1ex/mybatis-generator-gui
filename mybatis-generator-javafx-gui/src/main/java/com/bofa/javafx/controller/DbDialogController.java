package com.bofa.javafx.controller;

import com.bofa.javafx.util.AlertUtil;
import com.bofa.management.bean.DatasourceHolder;
import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.exception.BusinessException;
import com.bofa.management.service.datasource.DbSv;
import com.bofa.management.service.datasource.constant.DbType;
import com.bofa.management.service.datasource.dto.DbconfigRequest;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;

/**
 * @author bofa1ex
 * @since 2019-08-30
 */
public class DbDialogController extends AbstractFxmlController {

    static final Logger logger = LoggerFactory.getLogger(DbDialogController.class);

    private AbstractFxmlController mainController;

    @FXML
    private TextField connectNameView;
    @FXML
    private TextField connectCommentView;
    @FXML
    private TextField hostView;
    @FXML
    private TextField portView;
    @FXML
    private TextField userView;
    @FXML
    private PasswordField pwdView;
    @FXML
    private TextField schemaView;
    @FXML
    private TextField urlView;
    @FXML
    private ComboBox<String> driverComboBox;
    @FXML
    private Button dbTestBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button applyBtn;

    /** global control */
    private boolean mainCommand = false;
    /** parent control host,port,schema */
    private boolean parentCommand = false;
    /** sub control */
    private boolean subCommand = false;
    /**
     * 尝试过自动装配，但是需要定义类变量instance且借助postConstruct获取容器对象，以及initialize来反转装配对象的属性
     * 方法可行，但是不如直接借助ApplicationContext获取bean对象，手动注入来的思路更利于理解
     */
    private DbSv dbSv;
    private Dbconfig replicaConfig;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* driverComboBox' init operation such like mapping items, binding eventHandler */
        driverComboBox.getItems().addAll("mysql", "oracle", "h2");
        driverComboBox.setOnAction(event -> {
            String selectedItem = driverComboBox.getSelectionModel().getSelectedItem();
            Optional.ofNullable(DbType.findDbType(selectedItem))
                    .ifPresent(dbType -> {
                        try {
                            mainCommand = true;
                            portView.setText(dbType.getDefaultPort());
                            String host = hostView.getText();
                            String port = portView.getText();
                            String schema = schemaView.getText();
                            /* format the dbType templateValue */
                            urlView.setText(String.format(dbType.getConnectionUrlPattern(), host, port, schema));
                        } finally {
                            mainCommand = false;
                        }
                    });
        });
        /* buttons init operation */
        dbTestBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> testDbConnect());
        cancelBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeDialogStage());
        applyBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> saveDbConfig());
        /* textField textProperty bind changeListener */
        hostView.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mainCommand || parentCommand) {
                return;
            }
            String selectedItem = driverComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                AlertUtil.showInfoAlert("driverComboBox未选择数据库类型");
                return;
            }
            StringBuilder sb = new StringBuilder(urlView.getText());
            DbType dt = DbType.findDbType(selectedItem);
            assert dt != null;
            int hostIndex = sb.indexOf(dt.getHostDelimiter());
            int portIndex = sb.indexOf(dt.getPortDelimiter(), hostIndex);
            sb.replace(hostIndex + dt.getHostDelimiter().length(), portIndex, newValue);
            urlView.setText(sb.toString());
        });
        portView.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mainCommand || parentCommand) {
                return;
            }
            String selectedItem = driverComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                AlertUtil.showInfoAlert("driverComboBox未选择数据库类型");
                return;
            }
            /*
             * when this controller#initialize method invoked
             * driverComboBox#setOnAction will listen the selectedItem to make sure which dbType it is.
             * via dbType to mapper the portView default portValue, it will be triggered portView#textProperty#ChanedListener
             * But at the same time, the urlView have not mapper the templateValue yet
             * so the next step which getting portIndex and schemaIndex will throw {@link ArrayIndexOutOfBoundsException}
             */
            if (StringUtils.isBlank(urlView.getText())) {
                // is ok, just let it go.
                return;
            }
            StringBuilder sb = new StringBuilder(urlView.getText());
            DbType dt = DbType.findDbType(selectedItem);
            assert dt != null;
            int portIndex = sb.lastIndexOf(dt.getPortDelimiter());
            int schemaIndex = sb.indexOf(dt.getSchemaDelimiter(), portIndex);
            sb.replace(portIndex + dt.getPortDelimiter().length(), schemaIndex, newValue);
            urlView.setText(sb.toString());
        });
        schemaView.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mainCommand || parentCommand) {
                return;
            }
            String selectedItem = driverComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                AlertUtil.showInfoAlert("driverComboBox未选择数据库类型");
                return;
            }
            StringBuilder sb = new StringBuilder(urlView.getText());
            DbType dt = DbType.findDbType(selectedItem);
            assert dt != null;
            int schemaIndex = sb.lastIndexOf(dt.getSchemaDelimiter());
            sb.replace(schemaIndex + dt.getSchemaDelimiter().length(), sb.length(), newValue);
            urlView.setText(sb.toString());
        });
        /* u know, just reverse to filling the according field */
        urlView.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mainCommand) {
                return;
            }
            String selectedItem = driverComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                AlertUtil.showInfoAlert("driverComboBox未选择数据库类型");
                return;
            }
            DbType dt = DbType.findDbType(selectedItem);
            assert dt != null;
            String original = urlView.getText();
            int hostIndex = original.indexOf(dt.getHostDelimiter());
            int portIndex = original.indexOf(dt.getPortDelimiter(), hostIndex);
            int schemaIndex = original.indexOf(dt.getSchemaDelimiter(), portIndex);
            hostView.setText(original.substring(hostIndex + dt.getHostDelimiter().length(), portIndex));
            portView.setText(original.substring(portIndex + dt.getPortDelimiter().length(), schemaIndex));
            schemaView.setText(original.substring(schemaIndex + dt.getSchemaDelimiter().length()));
        });
    }

    private void clearViewContent() {
        try {
            mainCommand = true;
            connectNameView.setText("");
            connectCommentView.setText("");
            userView.setText("");
            pwdView.setText("");
            hostView.setText("");
            portView.setText("");
            urlView.setText("");
            schemaView.setText("");
            driverComboBox.setValue(null);
        } finally {
            mainCommand = false;
        }
    }

    /**
     * 由RootViewController编辑连接事件触发
     *
     * @param dbconfig
     */
    public void fillView(Dbconfig dbconfig) {
        try {
            parentCommand = true;
            replicaConfig = dbconfig;
            connectNameView.setText(dbconfig.getDbconnectname());
            connectCommentView.setText(dbconfig.getDescription());
            driverComboBox.getSelectionModel().select(dbconfig.getDbtype());
            userView.setText(dbconfig.getDbname());
            pwdView.setText(dbconfig.getDbpassword());
            urlView.setText(dbconfig.getDburl());
        } finally {
            parentCommand = false;
        }
    }

    /**
     * when click the applyBtn will triggered saving dbconfig
     */
    private void saveDbConfig() {
        manualAutowiredDbSv();
        boolean sc = true;
        DbconfigRequest dto = mapperDbConfigRequest();
        try {
            dbSv.testDbConnect(dto);
            Dbconfig dbconfigWithId = dbSv.saveDbConfig(dto);
            DatasourceHolder holder = dbSv.getDatasourceHolder(dbconfigWithId);
            ((RootViewController) mainController).loadTreeView(holder.getDbconfig());
        } catch (Exception e) {
            sc = false;
            AlertUtil.showErrorAlert(e.getMessage());
        } finally {
            if (sc) {
                clearViewContent();
                closeDialogStage();
            }
        }
    }

    private void testDbConnect() {
        manualAutowiredDbSv();
        DbconfigRequest dto = mapperDbConfigRequest();
        try {
            dbSv.testDbConnect(dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            AlertUtil.showErrorAlert(e.getMessage());
            return;
        }
        AlertUtil.showInfoAlert(String.format("db connect [%s] successful", dto.getDburl()));
    }

    public void setMainController(AbstractFxmlController mainController) {
        this.mainController = mainController;
    }

    private DbconfigRequest mapperDbConfigRequest() {
        DbconfigRequest dto = new DbconfigRequest();
        if (replicaConfig != null) {
            dto.setId(replicaConfig.getId());
            dto.setStatus(replicaConfig.getStatus());
        }
        String dbType = driverComboBox.getSelectionModel().getSelectedItem();
        dto.setDbconnectname(connectNameView.getText());
        dto.setDbschema(schemaView.getText());
        dto.setDbtype(dbType);
        DbType dt = DbType.findDbType(dbType);
        assert dt != null;
        dto.setDbdrive(dt.getDriverClass());
        dto.setDbname(userView.getText());
        dto.setDbpassword(pwdView.getText());
        dto.setDburl(urlView.getText());
        dto.setDescription(connectCommentView.getText());
        return dto;
    }

    /**
     * 手动装配业务对象
     */
    private void manualAutowiredDbSv() {
        if (dbSv == null) {
            dbSv = ((RootViewController) mainController).getDbSv();
        }
    }
}
