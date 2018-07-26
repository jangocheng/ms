package com.dafy.lxp.ms.mapper.pub;

import com.dafy.lxp.ms.common.datasource.MultiDataSource;
import com.dafy.lxp.ms.common.datasource.TargetDataSource;
import com.dafy.lxp.ms.dto.pub.ClientLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@TargetDataSource(MultiDataSource.PUBLIC)
public interface ClientLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ClientLog record);

    int insertSelective(ClientLog record);

    ClientLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ClientLog record);

    int updateByPrimaryKeyWithBLOBs(ClientLog record);

    int updateByPrimaryKey(ClientLog record);

    /**
     * 获取日志列表
     * @param map
     * @return
     */
    List<ClientLog> selectList(Map<String, Object> map);
}