package com.mizore.easybuy.utils;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Component
public class AliOSSUtils {

    private String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    private String accessKeyId = "LTAI5tLqCHuGgbHCXhk8RSGk";
    private String accessKeySecret = "2Mqw8Im9yjEKEzuejqSSCFr5Z6PPtx";
    private String bucketName = "starskylk";

    /**
     * 文件上传
     *
     * @param
     * @param
     * @return
     */
    public String upload(MultipartFile file)throws Exception {
        //获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        //避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + originalFilename;

        //上传文件到OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        ossClient.putObject(bucketName,filename,inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + filename;
        ossClient.shutdown();
        return url;
    }
}
