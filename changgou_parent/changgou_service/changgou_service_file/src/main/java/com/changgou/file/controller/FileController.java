package com.changgou.file.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.file.util.FastDFSClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(value = "/file")
public class FileController {

    @PostMapping("/upload")
    public String upload(@RequestParam(value="file") MultipartFile file){
        String path="";

        try {
            path=saveFile(file);
            System.out.println(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @GetMapping("/{id}")
    public String out(@PathVariable  Long id)
    {
        System.out.println(id);
        return "success";
    }

    private String saveFile(MultipartFile multipartFile) throws IOException {
        String fileName=multipartFile.getOriginalFilename();
        byte[] content=multipartFile.getBytes();
        String ext="";
        if (fileName!=null&&!"".equals(fileName)){
            ext=fileName.substring(fileName.lastIndexOf("."));
        }
        FastDFSFile fastDFSFile=new FastDFSFile(fileName,content,ext);
        String[] uploadResults= FastDFSClient.upload(fastDFSFile);
        String path = FastDFSClient.getTrackerUrl() + uploadResults[0] + "/" + uploadResults[1];
        //7. 返回
        return path;

    }
}
