package com.mxs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

/*
 * @Description: 资源矩阵表，保存在数据库中
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTable {
    private Integer id;
    private Integer n;
    private Integer m;
    private String available;
    private String max;
    private String allocation;
    private String need;
    private String executeTime;

    private Integer requestProcess;
    private String request;
    private LocalDateTime createTime;

    public ResourceMatrix toResourceMatrix() {
        //将json数组转换为数组
        Integer[] available = stringToInt1DArray(this.available);
        Integer[][] max = stringToInt2DArray(this.max);
        Integer[][] allocation = stringToInt2DArray(this.allocation);
        Integer[][] need = stringToInt2DArray(this.need);
        Integer[] executeTime = stringToInt1DArray(this.executeTime);
        Integer[] request = stringToInt1DArray(this.request);

        return new ResourceMatrix(n, m, available, max, allocation, need, executeTime, requestProcess, request);
    }

    /*
        json数组转为一维数组
     */
    private Integer[] stringToInt1DArray(String str) {
        return Arrays.stream(str
                .substring(1, str.length() - 1)
                .split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
    }

    /*
        json数组转为二维数组
     */
    private Integer[][] stringToInt2DArray(String str) {
        return Arrays.stream(str
                        .substring(1, str.length() - 1)
                        .split(","))
                .map(s -> Arrays.stream(s
                                .substring(1, s.length() - 1)
                                .split(","))
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }
}
