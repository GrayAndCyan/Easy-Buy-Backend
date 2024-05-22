package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class UploadController {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("/upload")
    public BaseVO<String> upload(MultipartFile image) throws Exception{
        log.info("文件上传：文件名：{}",image.getOriginalFilename());
        String url = aliOSSUtils.upload(image);
        log.info("aliOSS图片url:{}",url);
        return new BaseVO().setData(url);

    }

}
