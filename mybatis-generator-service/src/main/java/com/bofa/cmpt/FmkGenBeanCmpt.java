package com.bofa.cmpt;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Component
@Data
public class FmkGenBeanCmpt {

    private String baseMapperPath;

    private String baseEntityPath;

    private String baseSvPath;
}
