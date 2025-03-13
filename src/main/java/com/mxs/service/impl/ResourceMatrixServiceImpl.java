package com.mxs.service.impl;

import com.mxs.mapper.ResourceMatrixMapper;
import com.mxs.pojo.ResourceMatrix;
import com.mxs.pojo.ResourceTable;
import com.mxs.service.ResourceMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceMatrixServiceImpl implements ResourceMatrixService {

    @Autowired
    private ResourceMatrixMapper resourceMatrixMapper;

    /*
        随机生成矩阵
     */
    @Override
    public ResourceMatrix randomGenerate(Integer n, Integer m ,Integer requestProcess) {
        ResourceMatrix resourceMatrix = new ResourceMatrix(n,m,requestProcess);//随机生成矩阵
        save(resourceMatrix);//保存到数据库
        return resourceMatrix;
    }

    /*
        银行家算法调度
     */
    @Override
    public List<List<Integer>> dispatch(ResourceMatrix resourceMatrix) {
        return resourceMatrix.dispatch();
    }

    /*
        根据创建时间获取所有矩阵
     */
    @Override
    public List<ResourceMatrix> list() {
        List<ResourceTable> resourceTables = resourceMatrixMapper.list();
        return resourceTables.stream().map(ResourceTable::toResourceMatrix).toList();
    }

    /*
        保存矩阵
     */
    @Override
    public void save(ResourceMatrix resourceMatrix) {
        ResourceTable resourceTable = resourceMatrix.toResourceTable();//先转换为ResourceTable可被数据库保存
        resourceMatrixMapper.save(resourceTable);
    }

    /*
        加载指定时间的矩阵
     */
    @Override
    public ResourceMatrix findByTime(LocalDateTime createTime) {
        ResourceTable resourceTable = resourceMatrixMapper.findByTime(createTime);
        return resourceTable.toResourceMatrix();
    }


}
