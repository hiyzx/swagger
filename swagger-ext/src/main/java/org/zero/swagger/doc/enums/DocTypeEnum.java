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

    XXL(false, "/xxl"), KAFKA(false, "/kafka"), FEIGN(false, "/feign"), CUSTOM(false, "/custom"), CUSTOM_TOKEN(true, "/custom"), EVENT_LISTENER(false, "/event-listener");

    private final boolean token;

    private final String prefix;
}
