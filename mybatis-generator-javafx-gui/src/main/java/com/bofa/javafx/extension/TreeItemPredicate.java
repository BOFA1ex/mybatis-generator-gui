package com.bofa.javafx.extension;

import javafx.scene.control.TreeItem;

import java.util.function.Predicate;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-01
 */
@FunctionalInterface
@Deprecated
public interface TreeItemPredicate<T> {

    boolean test(TreeItem<T> parent, T value);

    static <T> TreeItemPredicate<T> create(Predicate<T> predicate) {
        return (parent, value) -> predicate.test(value);
    }
}
