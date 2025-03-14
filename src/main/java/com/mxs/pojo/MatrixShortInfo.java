package com.mxs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/*
    用于展示下拉列表的记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatrixShortInfo {
    private Integer n;
    private Integer m;
    private Integer requestProcess;
    private LocalDateTime createTime;
}
