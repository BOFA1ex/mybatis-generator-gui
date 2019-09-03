package com.bofa.javafx.util;


import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.*;
import javafx.scene.layout.Region;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-29
 */
public class ViewPropertyUtil {

    static final Logger logger = LoggerFactory.getLogger(ViewPropertyUtil.class);

    static ThreadLocal<ChangeListener> changeListenerTL = new ThreadLocal<>();

    public static void bindChangeListener(ChangeListener changeListener) {
        changeListenerTL.set(changeListener);
    }

    public static void removeChangeListener(ObservableValue property) {
        property.removeListener(changeListenerTL.get());
    }

    @Deprecated
    public static void dynamicReHeight(Region parent, Region sub) {
        dynamicReHeight(parent, sub, false);
    }

    @Deprecated
    public static void dynamicReHeight(Region parent, Region sub, boolean debug) {
        ReadOnlyDoubleProperty property = parent.heightProperty();
        property.addListener(new WeakChangeListener<>((observable, oldValue, newValue) -> {
            if (debug) {
                logger.info("oldValue {} -> newValue {}", oldValue, newValue);
            }
            sub.setPrefHeight(newValue.doubleValue());
        }));
    }

    public static void reHeightOnce(Region parent, Region sub, boolean debug) {
        ReadOnlyDoubleProperty property = parent.heightProperty();
        ChangeListener<Number> listener = (observable, oldValue, newValue) -> {
            if (debug) {
                logger.info("oldValue {} -> newValue {}", oldValue, newValue);
            }
            sub.setPrefHeight(newValue.doubleValue());
            removeChangeListener(property);
        };
        bindChangeListener(listener);
        property.addListener(listener);
    }

    public static void reWidthOnce(Region parent, Region sub, boolean debug) {
        ReadOnlyDoubleProperty property = parent.widthProperty();
        ChangeListener<Number> listener = (observable, oldValue, newValue) -> {
            if (debug) {
                logger.info("oldValue {} -> newValue {}", oldValue, newValue);
            }
            sub.setPrefWidth(newValue.doubleValue());
            removeChangeListener(property);
        };
        bindChangeListener(listener);
        property.addListener(listener);
    }

    @Deprecated
    public static void dynamicReWidth(Region parent, Region sub) {
        dynamicReWidth(parent, sub, false);
    }

    @Deprecated
    public static void dynamicReWidth(Region parent, Region sub, boolean debug) {
        parent.widthProperty().addListener(new WeakChangeListener<>((observable, oldValue, newValue) -> {
            if (debug) {
                logger.info("oldValue {} -> newValue {}", oldValue, newValue);
            }
            sub.setPrefWidth(newValue.doubleValue());
        }));
    }

}
