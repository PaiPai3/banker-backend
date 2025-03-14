package com.mxs.service;

import com.mxs.pojo.MatrixShortInfo;
import com.mxs.pojo.ResourceMatrix;
import com.mxs.pojo.ResourceTable;

import java.time.LocalDateTime;
import java.util.List;

public interface ResourceMatrixService {
    ResourceMatrix randomGenerate(Integer n,Integer m,Integer requestProcess);

    List<List<Integer>> dispatch(ResourceMatrix resourceMatrix);

    List<MatrixShortInfo> list();

    void save(ResourceMatrix resourceMatrix);

    ResourceMatrix findByTime(LocalDateTime createTime);
}
