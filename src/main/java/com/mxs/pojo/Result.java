package com.mxs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success(){
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }
    public static Result success(Object object){
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        result.data = object;
        return result;
    }
    public static Result error(){
        Result result = new Result();
        result.code = 0;
        result.msg = "error";
        return result;
    }
    public static Result error(Object object){
        Result result = new Result();
        result.code = 0;
        result.msg = "error";
        result.data = object;
        return result;
    }
}
