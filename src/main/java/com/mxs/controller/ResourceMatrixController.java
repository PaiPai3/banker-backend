package com.mxs.controller;

import com.mxs.pojo.ResourceMatrix;
import com.mxs.pojo.Result;
import com.mxs.service.ResourceMatrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:5174")
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@Slf4j
@RequestMapping("/api")
public class ResourceMatrixController {

    @Autowired
    private ResourceMatrixService resourceMatrixService;

    /*
    随机生成矩阵
    */
    @GetMapping("/generate")
    public Result randomGenerate(@RequestParam(value = "n", defaultValue = "3") Integer n,
                                 @RequestParam(value = "m", defaultValue = "5") Integer m,
                                 @RequestParam(value = "requestProcess",defaultValue = "2") Integer requestProcess) {

        log.info("接收数据n={},m={},process={}",n,m, requestProcess);
        ResourceMatrix resourceMatrix = resourceMatrixService.randomGenerate(n,m, requestProcess);
        return Result.success(resourceMatrix);
    }

    /*
        银行家算法调度
     */
    @PostMapping("/dispatch")
    public Result dispatch(@RequestBody ResourceMatrix resourceMatrix) {
        log.info("matrix:{}",resourceMatrix);
        List<List<Integer>> dispatchSequence = resourceMatrixService.dispatch(resourceMatrix);
        return Result.success(dispatchSequence);
    }


    /*
        根据创建时间获取所有矩阵
     */
    @GetMapping("/list")
    public Result list() {
        List<ResourceMatrix> resourceTableList = resourceMatrixService.list();
        return Result.success(resourceTableList);
    }

    /*
        保存矩阵
     */
    @PostMapping("/save")
    public Result save(@RequestBody ResourceMatrix resourceMatrix) {
        resourceMatrixService.save(resourceMatrix);
        return Result.success();

    }

    /*
        加载指定创建时间的矩阵
     */
    @PostMapping("/load")
    public Result findByTime(LocalDateTime createTime) {
        ResourceMatrix resourceMatrix = resourceMatrixService.findByTime(createTime);
        return Result.success(resourceMatrix);
    }
}
