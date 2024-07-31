package org.zero.swagger.doc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yzx
 * @description
 * @since 2024/7/16
 */
@Getter
@AllArgsConstructor
public enum DocTypeEnum {

    XXL("/xxl"), KAFKA("/kafka");

    private final String prefix;
}
