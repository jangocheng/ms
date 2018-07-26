package com.dafy.lxp.ms.api.pub;

import com.dafy.lxp.ms.service.pub.IFileUploadService;
import com.dafy.yihui.common.exception.ExceptionCode;
import com.dafy.yihui.common.exception.ServerException;
import com.dafy.yihui.common.po.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by liaoxudong
 * Date:2018/3/15
 */

@RestController
@RequestMapping("/public/file")
public class FileApi {

    private static final Logger logger = LoggerFactory.getLogger(FileApi.class);

    @Autowired
    private IFileUploadService fileUploadService;

    /**
     * 需要一定的上传时间间隔，否则会报错
     * @param file
     * @param bucketName
     * @param parentFolder 存放父级文件夹 只是阿里云文件服务的一个层级表现形式，实际会创建一个大小为null的object,如果多层使用/隔开，如存放在A文件夹下的B文件夹则传入A/B 不存放子文件夹则不传入此参数，如果传入null也会"创建"null文件夹
     * @return
     */
    @PostMapping("/upload/{bucketName}")
    public Response upload(@RequestParam("file") MultipartFile file, @PathVariable("bucketName") String bucketName,@RequestParam(value = "parentFolder",required = false) String parentFolder) {
        if (file == null || file.isEmpty())
            throw new ServerException(ExceptionCode.FILE_IS_EMPTY);
        return fileUploadService.upload(file, bucketName,parentFolder);
    }

    /*@PostMapping("/uploads")
    @ResponseBody
    public Response upload(@RequestParam("files") MultipartFile[] files,@RequestParam("userCode") String userCode) {
        if (files == null || files.length <= 0)
            throw new ServerException(ExceptionCode.FILE_IS_EMPTY);
//        return fileUploadService.uploads(files, userCode);
        return null;
    }*/

    @GetMapping("/download/{bucketName}")
    public void download(@PathVariable("bucketName") String bucketName, @RequestParam("fileName") String fileName, HttpServletResponse response) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        Response download = fileUploadService.download(bucketName, fileName);
        byte[] data = (byte[]) download.getData();
        try {
            response.getOutputStream().write(data);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @GetMapping("/list/buckets")
    public Response listBuckets(){
        return fileUploadService.listBucket();
    }

    /**
     * 列出某个Bucket下的所有文件，可分页
     * @param bucketName
     * @param startFileName
     * @param pageSize
     * @return
     */
    @GetMapping({"/list/{bucketName}/{pageSize}"/*, "/list/{bucketName}/{startFileName}/{pageSize}"*/})
    public Response listFiles(@PathVariable String bucketName, /*@PathVariable(required = false) String startFileName, */@PathVariable int pageSize,@RequestParam(required = false) String startFileName) {
        return fileUploadService.listFiles(bucketName, startFileName, pageSize);
    }

    @DeleteMapping("/delete/{bucketName}")
    public Response delete(@PathVariable String bucketName, @RequestBody List<String> files) {
        return fileUploadService.deleteFile(bucketName, files);
    }




}
