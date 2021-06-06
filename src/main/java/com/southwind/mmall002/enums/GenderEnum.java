package com.southwind.mmall002.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum GenderEnum {
    WOMAN(0,"女"),
    MAN(1,"男");

    @EnumValue
    private Integer code;
    private String value;

    GenderEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
