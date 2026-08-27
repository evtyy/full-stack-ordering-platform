package com.palette.controller.admin;

import com.palette.constant.MessageConstant;
import com.palette.result.Result;
import com.palette.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Common controller
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "Common controller")
@Slf4j
public class CommonController {

    // dependency injection
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * File upload
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("File upload")
    public Result<String> upload(MultipartFile file) {
        log.info("File upload: {}", file);

        try {
            //original file name
            String originalFilename = file.getOriginalFilename();
            //extract file extension
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //use UUID as file name
            String objectName = UUID.randomUUID() + extension;
            //file request path
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

}
