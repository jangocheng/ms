package com.dafy.lxp.ms.mapper.pub;

import com.dafy.lxp.ms.common.datasource.MultiDataSource;
import com.dafy.lxp.ms.common.datasource.TargetDataSource;
import com.dafy.lxp.ms.dto.pub.SysDataConfig;
import com.dafy.lxp.ms.dto.pub.SysDataConfigWithBLOBs;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@TargetDataSource(MultiDataSource.PUBLIC)
public interface SysDataConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysDataConfigWithBLOBs record);

    int insertSelective(SysDataConfigWithBLOBs record);

    SysDataConfigWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysDataConfigWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysDataConfigWithBLOBs record);

    int updateByPrimaryKey(SysDataConfig record);

    List<SysDataConfigWithBLOBs> findAll();
}