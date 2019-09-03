package com.bofa.javafx.controller;

import com.bofa.codegen.mybatis.MybatisGenerateCodeSv;
import com.bofa.codegen.mybatis.dto.MybatisGenerateCodeRequest;
import com.bofa.common.model.TableInfo;
import com.bofa.javafx.constant.FxmlEnum;
import com.bofa.javafx.extension.FilterableTreeItem;
import com.bofa.javafx.util.AlertUtil;
import com.bofa.javafx.util.ViewPropertyUtil;
import com.bofa.management.bean.DatasourceHolder;
import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.exception.BusinessException;
import com.bofa.management.service.datasource.DbSv;
import com.bofa.management.util.ApplicationContextUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import lombok.Data;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-29
 */
public class RootViewController extends AbstractFxmlController {

    static final Logger logger = LoggerFactory.getLogger(RootViewController.class);

    private final String JDBC_ENTITY_PRIMARY_KEY_RULE = "gen";
    private final String ORACLE_ENTITY_PRIMARY_KEY_RULE = "seq";
    private final String OTHER_ENTITY_PRIMARY_KEY_RULE = "dtl";

    /**
     * ================ tool bar ================
     */
    @FXML
    private Button connectDbBtn;
    @FXML
    private Button saveConfigBtn;
    /**
     * ================ left db view ================
     */
    @FXML
    private ScrollPane dbPane;
    @FXML
    private VBox dbVBox;
    @FXML
    private TextField searchView;
    @FXML
    private TreeView<String> dbTreeView;

    /**
     * ================ right details view ================
     */
    @FXML
    private TextField basePackageView;
    @FXML
    private TextField bizView;
    @FXML
    private TextField daoProjectView;
    @FXML
    private TextField primaryKeyView;
    @FXML
    private TextField entityPackageView;
    @FXML
    private TextField daoPackageView;
    @FXML
    private ToggleGroup primaryKeyRuleGroup = new ToggleGroup();
    @FXML
    private ToggleGroup controllerToggleGroup = new ToggleGroup();
    @FXML
    private ToggleGroup serviceToggleGroup = new ToggleGroup();
    @FXML
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    @FXML
    private RadioButton primaryKeyRule1;
    @FXML
    private RadioButton primaryKeyRule2;
    @FXML
    private RadioButton primaryKeyRule3;
    @FXML
    private ToggleButton controllerOff;
    @FXML
    private ToggleButton controllerOn;
    @FXML
    private ToggleButton serviceOff;
    @FXML
    private ToggleButton serviceOn;
    @FXML
    private Button generateBtn;
    @FXML
    private Button chooseDaoProjectBtn;

    private DbSv dbSv;

    private MybatisGenerateCodeSv mybatisGenerateCodeSv;

    private GenDetailsHolder genHolder = new GenDetailsHolder();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 手动注入容器业务对象
        dbSv = ApplicationContextUtil.getBean(DbSv.class);
        mybatisGenerateCodeSv = ApplicationContextUtil.getBean(MybatisGenerateCodeSv.class);
        /*
         *  dynamic init view prefer width and height
         *  direct to set property is not useful.
         *  dbVBox.setPrefHeight(dbPane.getPrefHeight());
         */
        ViewPropertyUtil.reHeightOnce(dbPane, dbVBox, true);
        /* init radioButtons in toggleGroup */
        initRadioButtonsToggleGroup();
        /* init toggleButtons in toggleGroup */
        initControllerToggleButtonsGroup();
        initServiceToggleButtonsGroup();
        /* bind button eventHandler */
        connectDbBtn.setOnMouseClicked(event -> {
            DbDialogController controller = (DbDialogController) loadFxml("New DataBase Connection", FxmlEnum.NEW_CONNECTION);
            controller.setMainController(this);
            controller.showDialogStage();
        });
        saveConfigBtn.setOnMouseClicked(event -> {
            ConfigDialogController controller = (ConfigDialogController) loadFxml("Generator Configuration", FxmlEnum.NEW_CONNECTION);
            controller.setMainController(this);
            controller.showDialogStage();
        });
        chooseDaoProjectBtn.setOnAction(event -> {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            Optional.ofNullable(directoryChooser.showDialog(getPrimaryStage())).map(File::getAbsolutePath)
                    .ifPresent(path -> daoProjectView.setText(path));
        });
        generateBtn.setOnAction(event -> {
            try {
                mybatisGenerateCodeSv.generateMybatis(mapperGenRequest());
            } catch (Exception e) {
                AlertUtil.showErrorAlert(ExceptionUtils.getStackTrace(e));
                return;
            }
            AlertUtil.showInfoAlert("生成成功!");
        });

        /* init textFieldView listener */
        basePackageView.textProperty().addListener((observable, oldValue, newValue) -> {
            if (StringUtils.isBlank(newValue)) {
                entityPackageView.setText("");
                daoPackageView.setText("");
                return;
            }
            String entityOldText = StringUtils.isNotBlank(entityPackageView.getText()) ? entityPackageView.getText() : "com..dao..entity";
            String daoOldText = StringUtils.isNotBlank(daoPackageView.getText()) ? daoPackageView.getText() : "com..dao.";
            String regexPattern = "(.*?)\\.dao";
            entityPackageView.setText(entityOldText.replaceFirst(regexPattern, newValue + ".dao"));
            daoPackageView.setText(daoOldText.replaceFirst(regexPattern, newValue + ".dao"));
        });
        bizView.textProperty().addListener((observable, oldValue, newValue) -> {
            if (StringUtils.isBlank(newValue)) {
                entityPackageView.setText("");
                daoPackageView.setText("");
            }
            String entityOldText = StringUtils.isNotBlank(entityPackageView.getText()) ? entityPackageView.getText() : "com..dao..entity";
            String daoOldText = StringUtils.isNotBlank(daoPackageView.getText()) ? daoPackageView.getText() : "com..dao.";
            String regexPattern = "dao\\.(.*)";
            entityPackageView.setText(entityOldText.replaceFirst(regexPattern, "dao." + newValue + ".entity"));
            daoPackageView.setText(daoOldText.replaceFirst(regexPattern, "dao." + newValue));
        });
        /* init dbTreeView items */
        dbTreeView.setShowRoot(false);
        FilterableTreeItem<String> root = new FilterableTreeItem<>(null);
        /* treeView rootItem bind predicateHandler */
        bindPredicateEventHandler(root);
        dbTreeView.setRoot(root);
        /* get all dbConnections then loadTreeView and tableInfos */
        dbSv.getAllDbConnections().forEach(this::loadTreeView);
        Callback<TreeView<String>, TreeCell<String>> defaultCellFactory = TextFieldTreeCell.forTreeView();
        /* treeCell bind eventHandler */
        dbTreeView.setCellFactory((TreeView<String> param) -> {
            TreeCell<String> cell = defaultCellFactory.call(param);
            cell.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                int level = dbTreeView.getTreeItemLevel(cell.getTreeItem());
                TreeCell<String> treeCell = (TreeCell<String>) event.getSource();
                /*
                 * Note:
                 * Must cast to FilterableTreeItem
                 * FilteredList is not support add, remove, clear, etc.
                 * @throw UnsupportedOperationException
                 */
                FilterableTreeItem<String> treeItem = ((FilterableTreeItem) treeCell.getTreeItem());
                /* handle click db event, build-up connection */
                if (level == 1) {
                    final ContextMenu contextMenu = new ContextMenu();
                    MenuItem item1 = new MenuItem("Disconnect");
                    item1.setGraphic(new ImageView("icons/disconnect.png"));
                    item1.setOnAction(event1 -> {
                        treeItem.clear();
                        treeItem.setExpanded(false);
                    });
                    MenuItem item2 = new MenuItem("Edit");
                    item2.setGraphic(new ImageView("icons/edit.png"));
                    item2.setOnAction(event1 -> {
                        Dbconfig selectedConfig = (Dbconfig) treeItem.getGraphic().getUserData();
                        DbDialogController controller = (DbDialogController) loadFxml("编辑数据库连接", FxmlEnum.NEW_CONNECTION);
                        controller.setMainController(this);
                        controller.fillView(selectedConfig);
                        controller.showDialogStage();
                    });
                    //TODO have not test synchronized yet. 2019-09-02 reply:finished!
                    MenuItem item3 = new MenuItem("Synchronize");
                    item3.setGraphic(new ImageView("icons/synchronize.png"));
                    item3.setOnAction(event1 -> {
                        treeItem.clear();
                        loadTableInfos(treeItem);
                    });
                    MenuItem item4 = new MenuItem("Remove");
                    item4.setGraphic(new ImageView("icons/remove.png"));
                    item4.setOnAction(event1 -> {
                        root.remove(treeItem);
                        Dbconfig selectedConfig = (Dbconfig) treeItem.getGraphic().getUserData();
                        try {
                            dbSv.delDbconfig(selectedConfig);
                        } catch (Exception e) {
                            AlertUtil.showErrorAlert("Delete connection failed! Reason: " + e.getMessage());
                        }
                    });
                    contextMenu.getItems().addAll(item1, item2, item3, item4);
                    cell.setContextMenu(contextMenu);
                }
                /* handle double click event */
                if (event.getClickCount() == 2) {
                    if (treeItem == null) {
                        return;
                    }
                    /* 这里不考虑判断折叠，监听有问题 */
                    treeItem.setExpanded(true);
                    if (level == 1) {
                        /* if it did load yet, keep return */
                        if (treeItem.size() > 0) {
                            return;
                        }
                        loadTableInfos(treeItem);
                    } else if (level == 2) {
                        GenDetailsHolder itemHolder = (GenDetailsHolder) treeItem.getGraphic().getUserData();
                        genHolder.setDbconfig(itemHolder.getDbconfig());
                        genHolder.setTableInfo(itemHolder.getTableInfo());
                        primaryKeyView.setText(itemHolder.getTableInfo().getPrimaryKeyColumnName());
                        bizView.setText(itemHolder.getDbconfig().getDbschema());
                    }
                }
            });
            return cell;
        });
    }


    public void loadTreeView(Dbconfig dbconfig) {
        FilterableTreeItem root = (FilterableTreeItem) dbTreeView.getRoot();
        root.removeIfAbsent(dbconfig);
        FilterableTreeItem<String> childRoot = new FilterableTreeItem<>(dbconfig.getDbconnectname());
        childRoot.setExpanded(false);
        ImageView rootImg = new ImageView("icons/database.png");
        rootImg.setUserData(dbconfig);
        childRoot.setGraphic(rootImg);
        root.add(childRoot);

        loadTableInfos(childRoot);
    }

    private void loadTableInfos(FilterableTreeItem treeItem) {
        Dbconfig selectedConfig = (Dbconfig) treeItem.getGraphic().getUserData();
        try {
            DatasourceHolder holder = dbSv.getDatasourceHolder(selectedConfig);
            List<TableInfo> allTables = holder.getAllTables();
            if (allTables != null && allTables.size() > 0) {
                ObservableList<TreeItem<String>> children = treeItem.getChildren();
                children.clear();
                for (TableInfo info : allTables) {
                    TreeItem<String> newTreeItem = new FilterableTreeItem<>(info.getTableName());
                    ImageView imageView = new ImageView("icons/table.png");
                    GenDetailsHolder detailsHolder = new GenDetailsHolder();
                    detailsHolder.setTableInfo(info);
                    detailsHolder.setDbconfig(selectedConfig);
                    imageView.setUserData(detailsHolder);
                    newTreeItem.setGraphic(imageView);
                    treeItem.setExpanded(true);
                    treeItem.add(newTreeItem);
                }
            } else {
                AlertUtil.showInfoAlert("对应schema的table为空，详情自查");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            AlertUtil.showErrorAlert(e.getMessage());
        }
    }

    /**
     * bind filter predicate event handler in this treeItem,
     * if childItems instanceof FilterableTreeItem,
     * it will set the predicate of child items to force filtering
     *
     * @param root FilterableTreeItem tagRoot which value is null
     */
    private void bindPredicateEventHandler(FilterableTreeItem<String> root) {
        root.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            if (searchView.getText() == null || searchView.getText().isEmpty()) {
                return null;
            }
            return (s -> {
                StringBuilder regexBuilder = new StringBuilder();
                for (final char c : searchView.getText().toCharArray()) {
                    regexBuilder.append(".*").append(c).append(".*");
                }
                return s.matches(regexBuilder.toString());
            });
        }, searchView.textProperty()));
    }

    /**
     * when click the generateBtn, mapper MybatisGenerateCodeRequest
     *
     * @return MybatisGenerateCodeRequest
     */
    private MybatisGenerateCodeRequest mapperGenRequest() {
        if (genHolder.getDbconfig() == null) {
            AlertUtil.showErrorAlert("dbConfig is null");
            BusinessException.throwBusinessException("dbConfig is null");
        }
        if (genHolder.getTableInfo() == null) {
            AlertUtil.showErrorAlert("tableInfo is null");
            BusinessException.throwBusinessException("tableInfo is null");
        }
        MybatisGenerateCodeRequest request = new MybatisGenerateCodeRequest();
        request.setDaoBasePath(daoProjectView.getText());
        request.setDaoPackage(daoPackageView.getText());
        request.setEntityPackage(entityPackageView.getText());
        request.setBusiName(bizView.getText());
        request.setBusiPackage(basePackageView.getText());
        request.setEntityPrimaryKey(primaryKeyView.getText());
        request.setEntityPrimaryKeyRule(genHolder.getEntityPrimaryKeyRule());
        request.setTableName(genHolder.getTableInfo().getTableName());
        request.setDbId(genHolder.getDbconfig().getId());
        return request;
    }

    private void initControllerToggleButtonsGroup() {
        controllerOff.setToggleGroup(controllerToggleGroup);
        controllerOn.setToggleGroup(controllerToggleGroup);
        controllerOff.setSelected(true);
        controllerOff.getStyleClass().add("toggle-button-on");
        controllerOn.getStyleClass().add("toggle-button-close");
        controllerToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> controllerOnStyleClass = controllerOn.getStyleClass();
            ObservableList<String> controllerOffStyleClass = controllerOff.getStyleClass();
            controllerOnStyleClass.remove(1);
            controllerOffStyleClass.remove(1);
            if (newValue.equals(controllerOn)) {
                controllerOnStyleClass.add("toggle-button-on");
                controllerOffStyleClass.add("toggle-button-close");
                //set current pane unVisible, set controller pane visible
                //TODO 2019-09-03 00:03 see above comment to finish it.
            } else {
                controllerOnStyleClass.add("toggle-button-close");
                controllerOffStyleClass.add("toggle-button-on");
            }
        });
    }

    private void initServiceToggleButtonsGroup() {
        serviceOff.setToggleGroup(serviceToggleGroup);
        serviceOn.setToggleGroup(serviceToggleGroup);
        serviceOff.setSelected(true);
        serviceOff.getStyleClass().add("toggle-button-on");
        serviceOn.getStyleClass().add("toggle-button-close");
        serviceToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> serviceOnStyleClass = serviceOn.getStyleClass();
            ObservableList<String> serviceOffStyleClass = serviceOff.getStyleClass();
            serviceOnStyleClass.remove(1);
            serviceOffStyleClass.remove(1);
            if (newValue.equals(serviceOn)) {
                serviceOnStyleClass.add("toggle-button-on");
                serviceOffStyleClass.add("toggle-button-close");
                //set current pane unVisible, set controller pane visible
                //TODO 2019-09-03 00:03 see above comment to finish it.
            } else {
                serviceOnStyleClass.add("toggle-button-close");
                serviceOffStyleClass.add("toggle-button-on");
            }
        });
    }

    private void initRadioButtonsToggleGroup() {
        primaryKeyRule1.setUserData(JDBC_ENTITY_PRIMARY_KEY_RULE);
        primaryKeyRule1.setSelected(true);
        genHolder.setEntityPrimaryKeyRule(JDBC_ENTITY_PRIMARY_KEY_RULE);
        primaryKeyRule2.setUserData(ORACLE_ENTITY_PRIMARY_KEY_RULE);
        primaryKeyRule3.setUserData(OTHER_ENTITY_PRIMARY_KEY_RULE);
        primaryKeyRule1.setToggleGroup(primaryKeyRuleGroup);
        primaryKeyRule2.setToggleGroup(primaryKeyRuleGroup);
        primaryKeyRule3.setToggleGroup(primaryKeyRuleGroup);
        primaryKeyRuleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
                genHolder.setEntityPrimaryKeyRule(((String) newValue.getUserData())));
    }

    public DbSv getDbSv() {
        return dbSv;
    }


    @Data
    static class GenDetailsHolder {
        TableInfo tableInfo;
        Dbconfig dbconfig;
        String entityPrimaryKeyRule;
    }
}
