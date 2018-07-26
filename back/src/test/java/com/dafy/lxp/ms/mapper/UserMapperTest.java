package com.dafy.lxp.ms.mapper;

import com.dafy.lxp.ms.TestBaseConfig;
import com.dafy.lxp.ms.dto.system.User;
import com.dafy.lxp.ms.mapper.system.UserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liaoxudong
 * Date:2018/1/30
 */

public class UserMapperTest extends TestBaseConfig{

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testAnnotation(){
        User userByName = userMapper.findUserByName("admin", "pwd13148899469");
        System.out.println(userByName);
    }


    @Test
    public void testXml(){
        User user = userMapper.selectByPrimaryKey(1L);
        System.out.println(user);
    }
}
