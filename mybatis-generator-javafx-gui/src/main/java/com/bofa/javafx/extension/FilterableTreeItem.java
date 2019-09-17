package com.bofa.javafx.extension;

import com.bofa.management.dao.datasource.entity.Dbconfig;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-01
 */
public class FilterableTreeItem<T> extends TreeItem<T> {
    private final ObservableList<TreeItem<T>> sourceList;
    private final FilteredList<TreeItem<T>> filteredList;
    private ObjectProperty<Predicate<String>> predicate = new SimpleObjectProperty<>();

    public FilterableTreeItem(T value) {
        super(value);
        this.sourceList = FXCollections.observableArrayList();
        this.filteredList = new FilteredList<>(this.sourceList);
        this.filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> child -> {
            // Set the predicate of child items to force filtering
            if (child instanceof FilterableTreeItem) {
                FilterableTreeItem<T> filterableChild = (FilterableTreeItem<T>) child;
                filterableChild.setPredicate(this.predicate.get());
            }
            // If there is no predicate, keep this tree item
            if (this.predicate.get() == null) {
                return true;
            }
            // If there are children, keep this tree item
            if (child.getChildren().size() > 0) {
                return true;
            }
            // Otherwise ask the Predicate
            // TODO 2019-09-02 检索到父节点, 子节点未显示
            // TODO 这里需要判断child的parent, 像是父节点value parent, 子节点value parent.sub1
            // TODO 解决上述问题
            StringBuilder itemRealValue = new StringBuilder();
            Optional.ofNullable(child.getParent()).map(TreeItem::getValue).ifPresent(t ->
                    itemRealValue.append(t).append("."));
            itemRealValue.append(child.getValue());
            return this.predicate.get().test(itemRealValue.toString());
        }, this.predicate));
        setHiddenFieldChildren(this.filteredList);
    }

    private void setHiddenFieldChildren(ObservableList<TreeItem<T>> list) {
        try {
            Field childrenField = TreeItem.class.getDeclaredField("children");
            childrenField.setAccessible(true);
            childrenField.set(this, list);
            Field declaredField = TreeItem.class.getDeclaredField("childrenListener");
            declaredField.setAccessible(true);
            list.addListener((ListChangeListener<? super TreeItem<T>>) declaredField.get(this));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Could not set TreeItem.children", e);
        }
    }

    public ObservableList<TreeItem<T>> getInternalChildren() {
        return this.sourceList;
    }

    public void remove(TreeItem value) {
        this.sourceList.remove(value);
    }

    public void removeIfAbsent(Dbconfig dbconfig) {
        TreeItem removeItem = null;
        for (final TreeItem<T> item : this.sourceList) {
            Long dbId = ((Dbconfig) item.getGraphic().getUserData()).getId();
            if (dbId.equals(dbconfig.getId())) {
                removeItem = item;
                break;
            }
        }
        Optional.ofNullable(removeItem).ifPresent(this.sourceList::remove);

    }

    public int size() {
        return this.sourceList.size();
    }

    public void add(TreeItem value) {
        this.sourceList.add(value);
    }

    public void clear() {
        this.sourceList.clear();
    }

    public void addAll(TreeItem... values) {
        this.sourceList.addAll(values);
    }

    public ObjectProperty<Predicate<String>> predicateProperty() {
        return predicate;
    }

    public final Predicate<String> getPredicate() {
        return this.predicate.get();
    }

    /**
     * Set the predicate
     *
     * @param predicate the predicate
     */
    public final void setPredicate(Predicate<String> predicate) {
        this.predicate.set(predicate);
    }
}
