package com.red.ElectronicStore.services.Impl;

import com.red.ElectronicStore.exceptions.BadApiRequest;
import com.red.ElectronicStore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws BadApiRequest, IOException {

        String originalFilename = file.getOriginalFilename();
        logger.info("File name {}",originalFilename);
        String fileName = UUID.randomUUID().toString();

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));


        String fileNameWithExtention = fileName+extension;

        String fullPathWithFileName=path+ File.separator+fileNameWithExtention;

        if(extension.equalsIgnoreCase(".png")||
                extension.equalsIgnoreCase(".jpg")||
                extension.equalsIgnoreCase("jpeg")){

            File folder = new File(path);

            if(!folder.exists()){
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));

            return fileNameWithExtention;

        }
        else {
           throw  new BadApiRequest("File with this"+ extension+ "not allowed!!!");
        }


    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath = path+File.separator+name;

       InputStream inputStream =new FileInputStream(fullPath);
        return inputStream;
    }
}
