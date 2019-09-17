package com.bofa.javafx.util;

import com.bofa.management.exception.BusinessException;
import javafx.scene.control.TextInputControl;
import org.apache.commons.lang3.StringUtils;


/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-14
 */
public class ValidatorUtil {

    public static void validate(TextInputControl... controls) {
        for (final TextInputControl control : controls) {
            validate(control);
        }
    }

    private static void validate(TextInputControl textInputControl) {
        if (StringUtils.isBlank(textInputControl.getText())) {
            AlertUtil.showErrorAlert(textInputControl + "is null");
            BusinessException.throwBusinessException(textInputControl + "is null");
        }
    }
}
