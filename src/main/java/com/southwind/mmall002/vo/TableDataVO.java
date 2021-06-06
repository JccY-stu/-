package com.southwind.mmall002.vo;

import lombok.Data;

import java.util.List;

/**
 *
 * @param <T>
 */
@Data
public class TableDataVO<T> {
    private Integer code;
    private String msg;
    private Long count;
    private List<T> data;
}
