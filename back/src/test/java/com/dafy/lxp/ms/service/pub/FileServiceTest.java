package com.dafy.lxp.ms.service.pub;

import com.dafy.lxp.ms.TestBaseConfig;
import com.dafy.yihui.common.po.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Created by liaoxudong
 * Date:2018/3/15
 */

public class FileServiceTest extends TestBaseConfig{

    @Autowired
    private IFileUploadService fileUploadService;

    @Test
    public void uploadTest(){
//        fileUploadService.upload(new File("D:\\Users\\liaoxudong\\Pictures\\a4fbced584d3e5c61d4891dda70a44eb.jpg"),"xxx");
        Response response = fileUploadService.listBucket();
        System.out.println(response);
    }

    @Test
    public void listFiles(){
        Response response = fileUploadService.listFiles("assets-file", "石嘴山客服报表20180127.xls", 100);
        System.out.println(response);
    }

    @Test
    public void download(){
        Response response = fileUploadService.download("test-other-file", "00213c3b524d4ca1950456b786c0937f.png");
        System.out.println(response);

    }

    @Test
    public void deleteFile(){
        Response response = fileUploadService.deleteFile("test-other-file", Arrays.asList(new String[]{"00213c3b524d4ca1950456b786c0937f.png", "003e656c913e4a9594c72abc05e45b61.png"}));
        System.out.println(response);


    }
}
