package com.upload.filedemo;

import com.upload.filedemo.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class FileDemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(FileDemoApplication.class, args);
	}

}
