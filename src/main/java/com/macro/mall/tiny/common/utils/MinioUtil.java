package com.macro.mall.tiny.common.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Minio对象存储服务的工具类
 */
@Slf4j
@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    /**
     * -- GETTER --
     *  获取默认桶名称
     *
     * @return 桶名称
     */
    @Getter
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 检查桶是否存在
     *
     * @param bucketName 桶名称
     * @return true存在，false不存在
     */
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("检查桶是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 创建桶
     *
     * @param bucketName 桶名称
     * @return true成功，false失败
     */
    public boolean createBucket(String bucketName) {
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("创建桶成功: {}", bucketName);
                return true;
            }
            return true;
        } catch (Exception e) {
            log.error("创建桶失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 删除桶
     *
     * @param bucketName 桶名称
     * @return true成功，false失败
     */
    public boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            log.info("删除桶成功: {}", bucketName);
            return true;
        } catch (Exception e) {
            log.error("删除桶失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取所有桶
     *
     * @return 桶列表
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error("获取所有桶失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param objectName 对象名称
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String objectName) {
        try {
            // 确保桶存在
            if (!bucketExists(bucketName)) {
                createBucket(bucketName);
            }

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("文件上传成功: {}", objectName);
            return getFileUrl(objectName);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传文件流
     *
     * @param inputStream 文件流
     * @param objectName  对象名称
     * @param contentType 文件类型
     * @return 文件访问URL
     */
    public String uploadFile(InputStream inputStream, String objectName, String contentType) {
        try {
            // 确保桶存在
            if (!bucketExists(bucketName)) {
                createBucket(bucketName);
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, -1, 10485760) // 10MB
                            .contentType(contentType)
                            .build()
            );

            log.info("文件流上传成功: {}", objectName);
            return getFileUrl(objectName);
        } catch (Exception e) {
            log.error("文件流上传失败: {}", e.getMessage());
            throw new RuntimeException("文件流上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传字节数组
     *
     * @param bytes       字节数组
     * @param objectName  对象名称
     * @param contentType 文件类型
     * @return 文件访问URL
     */
    public String uploadFile(byte[] bytes, String objectName, String contentType) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            return uploadFile(inputStream, objectName, contentType);
        } catch (Exception e) {
            log.error("字节数组上传失败: {}", e.getMessage());
            throw new RuntimeException("字节数组上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     *
     * @param objectName 对象名称
     * @return 文件流
     */
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage());
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     * @return true成功，false失败
     */
    public boolean deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("文件删除成功: {}", objectName);
            return true;
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 批量删除文件
     *
     * @param objectNames 对象名称列表
     * @return 删除失败的文件列表
     */
    public List<String> deleteFiles(List<String> objectNames) {
        List<String> failedFiles = new ArrayList<>();
        try {
            List<DeleteObject> objects = new ArrayList<>();
            for (String objectName : objectNames) {
                objects.add(new DeleteObject(objectName));
            }

            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(objects)
                            .build()
            );

            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                failedFiles.add(error.objectName());
                log.error("删除文件失败: {} - {}", error.objectName(), error.message());
            }
        } catch (Exception e) {
            log.error("批量删除文件失败: {}", e.getMessage());
        }
        return failedFiles;
    }

    /**
     * 检查文件是否存在
     *
     * @param objectName 对象名称
     * @return true存在，false不存在
     */
    public boolean fileExists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件信息
     *
     * @param objectName 对象名称
     * @return 文件信息
     */
    public StatObjectResponse getFileInfo(String objectName) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件信息失败: {}", e.getMessage());
            throw new RuntimeException("获取文件信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件列表
     *
     * @param prefix 前缀
     * @return 文件列表
     */
    public List<Item> listFiles(String prefix) {
        List<Item> items = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            log.error("获取文件列表失败: {}", e.getMessage());
        }
        return items;
    }

    /**
     * 获取所有文件列表
     *
     * @return 文件列表
     */
    public List<Item> listAllFiles() {
        return listFiles("");
    }

    /**
     * 获取文件访问URL
     *
     * @param objectName 对象名称
     * @return 文件访问URL
     */
    public String getFileUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS) // 7天有效期
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件URL失败: {}", e.getMessage());
            throw new RuntimeException("获取文件URL失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件预签名上传URL
     *
     * @param objectName 对象名称
     * @param expiry     过期时间（秒）
     * @return 预签名上传URL
     */
    public String getPresignedUploadUrl(String objectName, int expiry) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiry, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取预签名上传URL失败: {}", e.getMessage());
            throw new RuntimeException("获取预签名上传URL失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件预签名下载URL
     *
     * @param objectName 对象名称
     * @param expiry     过期时间（秒）
     * @return 预签名下载URL
     */
    public String getPresignedDownloadUrl(String objectName, int expiry) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiry, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取预签名下载URL失败: {}", e.getMessage());
            throw new RuntimeException("获取预签名下载URL失败: " + e.getMessage());
        }
    }

    /**
     * 复制文件
     *
     * @param sourceObjectName 源文件名
     * @param targetObjectName 目标文件名
     * @return true成功，false失败
     */
    public boolean copyFile(String sourceObjectName, String targetObjectName) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucketName)
                            .object(targetObjectName)
                            .source(CopySource.builder()
                                    .bucket(bucketName)
                                    .object(sourceObjectName)
                                    .build())
                            .build()
            );
            log.info("文件复制成功: {} -> {}", sourceObjectName, targetObjectName);
            return true;
        } catch (Exception e) {
            log.error("文件复制失败: {}", e.getMessage());
            return false;
        }
    }

}
