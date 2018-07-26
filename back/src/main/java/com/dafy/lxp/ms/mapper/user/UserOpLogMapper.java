package com.dafy.lxp.ms.mapper.user;

import com.dafy.lxp.ms.common.datasource.MultiDataSource;
import com.dafy.lxp.ms.common.datasource.TargetDataSource;
import com.dafy.lxp.ms.dto.user.UserOpLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@TargetDataSource(MultiDataSource.USER)
public interface UserOpLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserOpLog record);

    int insertSelective(UserOpLog record);

    UserOpLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserOpLog record);

    int updateByPrimaryKey(UserOpLog record);

    /**
     * 获取日志列表
     * @param map
     * @return
     */
    List<UserOpLog> selectList(Map<String, Object> map);
}