package com.mxs.mapper;

import com.mxs.pojo.ResourceTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ResourceMatrixMapper {

    /*
        保存矩阵
     */
    @Insert("insert into resource_table (n,m,available, max, allocation, need, create_time, execute_time, request_process,request) " +
            "values (#{n},#{m},#{available},#{max},#{allocation},#{need}, #{createTime}, #{executeTime}, #{requestProcess}, #{request})")
    void save(ResourceTable resourceTable);

    /*
        获取所有矩阵
     */
    @Select("select * from resource_table order by create_time desc")
    List<ResourceTable> list();

    /*
        根据创建时间加载矩阵
     */
    @Select("select * from resource_table where create_time = #{createTime}")
    ResourceTable findByTime(LocalDateTime createTime);
}
