package com.dafy.lxp.ms.service.pub.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.dafy.lxp.ms.service.pub.IFileUploadService;
import com.dafy.yihui.common.exception.ServerException;
import com.dafy.yihui.common.po.Response;
import com.dafy.yihui.common.util.CommonUtils;
import com.dafy.yihui.common.util.JsonUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 使用阿里云OSS sdk实现文件api操作
 * Created by liaoxudong
 * Date:2018/3/15
 */

@Service
public class FileUploadService implements IFileUploadService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    // 允许的文件类型
    /*@Value("${modules.public.file.fileTypes}")
    private String fileTyps;
    @Value("${modules.public.file.url}")
    private String url;*/
    @Value("${modules.public.file.aliyun-oss.accessKeyId}")
    private String accessKeyId;
    @Value("${modules.public.file.aliyun-oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${modules.public.file.aliyun-oss.endpoint}")
    private String endpoint;
    @Value("${modules.public.file.aliyun-oss.defaultBucket}")
    private String defaultBucket;
    @Value("${modules.public.file.assets_url}")
    private String assets_url;


    @Override
    public Response upload(MultipartFile file, String bucketName,String parentFolder) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        logger.info("文件上传,contentType:{},fileName:{}", contentType, filename);
        String fileName = parentFolder == null?filename:parentFolder.endsWith("/")?parentFolder+filename:parentFolder + "/" +filename;
        /*String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if(!fileName.contains(".") || StringUtils.isEmpty(fileType))
            fileType = contentType.substring(contentType.lastIndexOf("/")+1,contentType.length());
        if (!ArrayUtils.contains(fileTyps.split(","), fileType)) {
            logger.error("文件类型不匹配，限制【{}】，当前文件类型【{}】", fileTyps, fileType);
            throw new ServerException(ExceptionCode.FILE_TYPE_NOT_MATCHES);
        }*/
        // 解决中文问题，liunx下中文路径，图片显示问题
//        fileName = StringUtils.getUUID();
        // 创建OSSClient实例
        Map<String, String> resultMap = new HashMap<>();
        try {
            ossHandler(OSSType.UPLOAD, PutObjectResult.class, bucketName, fileName, file.getBytes());
            String url = "http://"+bucketName+assets_url+fileName;
            logger.debug("文件上传成功，访问url:{}",url);
            resultMap.put("url", url);
        } catch (Exception e) {
            logger.error("文件上传失败：" + e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        // 上传了新文件也刷新缓存
        clearCache(bucketName);
        return CommonUtils.buildSuccessResp(resultMap);

    }

    private static final Map<String, List<OSSObjectSummary>> BUCKETS_FILE_CACHE = new ConcurrentHashMap<>();

    /**
     * 阿里云对象存储处理
     *
     * @param handlerType 处理类型 上传、下载、列出bucket等
     * @param returnType  指定返回类型
     * @param params      需要传入的参数，如果没有参数不传入即可
     * @param <T> 返回类型，由入参指定
     * @return
     */
    private <T> T ossHandler(OSSType handlerType, Class<T> returnType, Object... params) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        Object result = null;
        switch (handlerType) {
            case UPLOAD: {// 文件上传
                String bucketName = String.valueOf(params[0]);// 指定bucketName
                String fileName = String.valueOf(params[1]);// 指定文件名
                byte[] bytes = (byte[]) params[2];// 文件对应的二进制数组
                /*PutObjectResult */
                result = ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
//                    return (T) result;
                break;
            }
            case LIST_BUCKET: {// 列出所有bucket
                // 列举bucket
                /*List<Bucket>*/
                result = ossClient.listBuckets();
                logger.debug("buckets:【{}】",result);
//                return (T) buckets;
                break;
            }
            case DOWNLOAD: {// 文件下载
                String bucketName = String.valueOf(params[0]);
                String fileName = String.valueOf(params[1]);
                OSSObject ossObject = null;
                try {
                    ossObject = ossClient.getObject(bucketName, fileName);
                    logger.debug("下载完成");
                } catch (OSSException e) {
                    logger.error(e.getMessage(),e);
                    throw new ServerException(e.getErrorCode(),e.getErrorMessage());
//                    e.printStackTrace();
                } catch (ClientException e) {
                    logger.error(e.getMessage(),e);
                    throw new ServerException(e.getErrorCode(),e.getErrorMessage());
//                    e.printStackTrace();
                }
                /*InputStream*/
//                result = ossObject.getObjectContent();
                try {
                    // 直接转二进制了，不用sdk的inputStream
                    result = EntityUtils.toByteArray(ossObject.getResponse().getHttpResponse().getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                return (T) objectContent;
                break;
            }
            case LIST_FILE: {// 列出某个bucket下所有文件 采用分页的方式
                String bucketName = String.valueOf(params[0]);// 指定Bucket
                String nextMarker = String.valueOf(params[1]);// 从哪个文件名开始
                int maxKeys = (int) params[2];// 总共显示多少条
                List<OSSObjectSummary> allFiles = new ArrayList<>();// 存放所有的bucket文件
                if (BUCKETS_FILE_CACHE.containsKey(bucketName)) {// 缓存中存在直接返回
                    logger.debug("缓存中存在当前bucket文件，直接获取返回");
                    result = BUCKETS_FILE_CACHE.get(bucketName);
                    break;
                }
                try {
                    ObjectListing objectListing;
                    logger.debug("遍历获取当前bucket所有文件");
                    do{
                        objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withMarker(nextMarker).withMaxKeys(maxKeys));
                        allFiles.addAll(objectListing.getObjectSummaries());
                        nextMarker = objectListing.getNextMarker();
                    }while (objectListing.isTruncated());
                } catch (OSSException e) {
                    logger.error(e.getMessage(),e);
                    throw new ServerException(e.getErrorCode(),e.getErrorMessage());
//                    e.printStackTrace();
                } catch (ClientException e) {
                    logger.error(e.getMessage(),e);
                    throw new ServerException(e.getErrorCode(),e.getErrorMessage());
//                    e.printStackTrace();
                }
                result = allFiles;
                logger.debug("files:【{}】",result);
                logger.debug("加入缓存：{}",bucketName);
                BUCKETS_FILE_CACHE.put(bucketName, allFiles);// 获取完成加入缓存
                break;
            }
            case DELETE: {// 删除指定文件
                // 删除Objects
                String bucketName = String.valueOf(params[0]);// 指定Bucket
                List<String> fileNames = (List<String>) params[1];// 需要删除的文件名列表
                DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileNames));
                /*List<String> */
                result = deleteObjectsResult.getDeletedObjects();
//                return (T) deletedObjects;
                break;
            }
            case BUCKET_FILENUMS:// 某个Bucket下存放的文件数量
                String bucketName = String.valueOf(params[0]);
                ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName));
                Integer size = objectListing.getObjectSummaries().size();
                return (T)size;
        }
        ossClient.shutdown();
        return (T) result;
    }



    /**
     * 列出所有bucket
     *
     * @return
     */
    @Override
    public Response listBucket() {
        logger.debug("列出OSS所有bucket");
        List<Bucket> list = ossHandler(OSSType.LIST_BUCKET, List.class);
        List<String> buckets = list.stream().map(Bucket::getName).collect(Collectors.toList());
        return CommonUtils.buildSuccessResp(buckets);
    }

    /**
     * 用于页面显示的对象封装
     */
    static class OssFileInfo extends OSSObjectSummary{
        private String url;
        private String strSize;

        public String getStrSize() {
            return strSize;
        }

        public void setStrSize(String strSize) {
            this.strSize = strSize;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public static OssFileInfo parse(OSSObjectSummary oss) {
            if (oss == null) {
                return null;
            }
            String json = JsonUtils.toJson(oss);
            return JsonUtils.fromJson(json, OssFileInfo.class);
        }

        @Override
        public boolean equals(Object obj) {//FIX issue 01
            if (obj == null) {
                return false;
            }
            if (obj instanceof OssFileInfo) {
                OssFileInfo ossFileInfo = (OssFileInfo) obj;
                return ossFileInfo.getKey().equals(this.getKey());
            }
            return false;
        }
    }

    @Override
    @SuppressWarnings("all")
    public Response listFiles(String bucketName, String beginFileName, int pageSize){
        logger.debug("列出{}下的所有文件，从{}开始，展示{}个文件",bucketName,beginFileName == null || "null".equals(beginFileName)?"第一个文件":beginFileName,pageSize);
        // 获取到当前bucket的所有文件
        List<OSSObjectSummary> allFiles = ossHandler(OSSType.LIST_FILE, List.class, bucketName, null, pageSize);
        logger.debug("当前Bucket存在{}个文件，开始在内存中构建分页数据...",allFiles.size());
        List<OssFileInfo> resultList = new ArrayList<>(pageSize);
        // 过滤分页显示
        for(int i=0;i<allFiles.size();i++) {
            OSSObjectSummary oss = allFiles.get(i);
            if ("null".equals(beginFileName) && resultList.size() < pageSize) {// 第一页
                addToResult(resultList,oss,bucketName);
            }else{// 第二页开始
                if(resultList.size() == 0){
                    if (oss.getKey().equals(beginFileName)) {// 跳过前面的文件，从beginFileName才开始添加第一个元素，相当于分页的开始第一行
                        oss = allFiles.get(i + 1);// FIXME 暂时先这样，如果前端控制不好可能会有溢出异常蹦出来
                        addToResult(resultList,oss,bucketName);
                    }
                }else if (resultList.size() < pageSize) {// 添加“分页”后的一页文件
                    addToResult(resultList,oss,bucketName);// issue 01 非第一页的第一个文件会出现重复，要过滤
                }
            }
        }
        /*String json = JsonUtils.toJson(response.getData());
        List<Map<String,Object>> list = JsonUtils.fromJson(json, List.class);
        list.forEach((map) -> {
            map.put("url", "http://"+map.get("bucketName")+assets_url+map.get("key"));// 设置url属性
            long size = Long.valueOf(map.get("size")+"");
            // 将文件大小显示从B转换为KB或MB
            String strSize = (size>=1024*1024)?new BigDecimal(size).divide(new BigDecimal(1024*1024)).setScale(3,BigDecimal.ROUND_HALF_UP)+"MB":(size==0)?"0":new BigDecimal(size).divide(new BigDecimal(1024)).setScale(3,BigDecimal.ROUND_HALF_UP)+"KB";
            map.put("size", strSize);
        });*/
        // 再封装一层 好添加分页属性
        Map<String,Object> data = new HashMap<>();
        data.put("fileInfos", resultList);
        data.put("totalCount", allFiles.size());
        logger.debug("分页对象构建完成");
        return CommonUtils.buildSuccessResp(data);
    }

    private void addToResult(List<OssFileInfo> resultList, OSSObjectSummary oss,String bucketName) {
        // 将文件大小显示从B转换为KB或MB
        long size = oss.getSize();
        OssFileInfo ossFileInfo = OssFileInfo.parse(oss);
        ossFileInfo.setUrl("http://"+bucketName+assets_url+oss.getKey());
        String strSize = (size>=1024*1024)?new BigDecimal(size).divide(new BigDecimal(1024*1024)).setScale(3,BigDecimal.ROUND_HALF_UP)+"MB":(size==0)?"0":new BigDecimal(size).divide(new BigDecimal(1024)).setScale(3,BigDecimal.ROUND_HALF_UP)+"KB";
        ossFileInfo.setStrSize(strSize);
        if(!resultList.contains(ossFileInfo)){// 非第一页的第一个文件会出现重复，要过滤
//            logger.debug("筛选通过，加入分页对象：{}",ossFileInfo);
            resultList.add(ossFileInfo);
        }

    }

    @Override
    public Response deleteFile(String bucketName, List<String> fileNames) {
        logger.debug("删除{}下的文件：{}",bucketName,fileNames);
        // 删除文件时同时清除这个bucket下的缓存
        clearCache(bucketName);
        return CommonUtils.buildSuccessResp(ossHandler(OSSType.DELETE,List.class, bucketName, fileNames));
    }

    /**
     * 清除固定bucket下的本地缓存
     * @param bucketName
     */
    private void clearCache(String bucketName) {
        logger.debug("刷新缓存：{}",bucketName);
        BUCKETS_FILE_CACHE.remove(bucketName);
    }

    @Override
    public Response download(String bucketName, String fileName) {
        logger.debug("下载{}下的文件：{}",bucketName,fileName);
        return CommonUtils.buildSuccessResp(ossHandler(OSSType.DOWNLOAD,byte[].class, bucketName, fileName));
    }


}
