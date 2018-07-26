package com.dafy.lxp.ms.service.pub;

import com.dafy.yihui.common.po.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 供可视化文件上传
 * Created by liaoxudong
 * Date:2018/3/15
 */

public interface IFileUploadService {

    /**
     * 文件上传
     * @param file
     * @param bucketName 指定Bucket
     * @param parentFolder 指定存放目录
     * @return
     */
    Response upload(MultipartFile file, String bucketName,String parentFolder);

    /**
     * 列出所有bucket
     * @return
     */
    Response listBucket();

    /**
     * 分页显示指定bucket下从beginFileName开始的pageSize个文件
     * @param bucketName 指定Bucket
     * @param beginFileName 从哪个文件开始 如果从头开始则传入null
     * @param pageSize 显示文件数量
     * @return
     */
    Response listFiles(String bucketName, String beginFileName, int pageSize);

    /**
     * 删除文件
     * @param bucketName 指定bucket
     * @param fileNames 需要删除的文件名
     * @return
     */
    Response deleteFile(String bucketName, List<String> fileNames);

    /**
     * 下载文件
     * @param bucketName 指定bucket
     * @param fileName 需要下载的文件名
     * @return
     */
    Response download(String bucketName, String fileName);
}
