package com.macro.mall.tiny;

import com.macro.mall.tiny.common.utils.MinioUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@ActiveProfiles("dev")
public class MallTinyApplicationTests {

    @Autowired
    private MinioUtil minioUtil;

    @Test
    public void contextLoads() {
    }

    /**
     * 测试Minio上传和删除文件
     * @throws Exception
     */
    @Test
    public void testMinioUploadAndDelete() throws Exception {
        String objectName = "test/minio-test-" + System.currentTimeMillis() + ".txt";
        byte[] data = "hello mall-tiny minio".getBytes(StandardCharsets.UTF_8);

        // 上传
        String url = minioUtil.uploadFile(data, objectName, "text/plain");
        Assertions.assertNotNull(url, "上传后返回的URL不应为null");
        Assertions.assertTrue(minioUtil.fileExists(objectName), "上传后文件应存在");

        // 下载并验证内容
        try (InputStream is = minioUtil.downloadFile(objectName)) {
            byte[] downloaded = StreamUtils.copyToByteArray(is);
            Assertions.assertArrayEquals(data, downloaded, "下载的内容应与上传内容一致");
        }

        // 删除并验证
        boolean deleted = minioUtil.deleteFile(objectName);
        Assertions.assertTrue(deleted, "删除应返回true");
        Assertions.assertFalse(minioUtil.fileExists(objectName), "删除后文件不应存在");
    }
}
