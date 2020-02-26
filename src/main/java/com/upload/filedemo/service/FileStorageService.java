package com.upload.filedemo.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.upload.filedemo.config.SpringMongoConfig;
import com.upload.filedemo.exception.FileStorageException;
import com.upload.filedemo.exception.MyFileNotFoundException;
import com.upload.filedemo.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {

        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringMongoConfig.class);

        GridFsOperations gridOperations =
                (GridFsOperations) ctx.getBean("gridFsTemplate");


        DBObject metaData = new BasicDBObject();
        metaData.put("extra1", "anything 1");
        metaData.put("extra2", "anything 2");

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());


        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            gridOperations.store(file.getInputStream(), file.getOriginalFilename(), "text/html", metaData);

            GridFSFindIterable result =
                    gridOperations.find(
                            new Query().addCriteria(
                                    Criteria.where("filename").is("33.rel")));

            for (GridFSFile fileToRead : result) {
                try {
                    System.out.println(fileToRead.getFilename());
                    System.out.println(fileToRead.getUploadDate());
                    System.out.println(fileToRead.getMetadata().getString("extra1"));
                    System.out.println(fileToRead.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            MongoClient mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB("myapp-service");
            //Create instance of GridFS implementation
            GridFS gridFs = new GridFS(db);
            GridFSDBFile outputImageFile = gridFs.findOne("33.rel");
            System.out.println("Total Chunks: " + outputImageFile.numChunks());

            String imageLocation = "C:/FILETEST/33.rel";

            outputImageFile.writeTo(imageLocation);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

        return fileName;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}