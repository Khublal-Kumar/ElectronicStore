package com.red.ElectronicStore.services;

import com.red.ElectronicStore.exceptions.BadApiRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public interface FileService {

    String uploadFile(MultipartFile file, String path) throws BadApiRequest, IOException;

    InputStream getResource(String path,String name) throws FileNotFoundException;
}
