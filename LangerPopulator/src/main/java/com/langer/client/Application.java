package com.langer.client;


import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@SpringBootApplication
@EnableFeignClients
public class Application
{
	public static void main(String[] args) throws IOException
	{
		SpringApplication springApplication =
				new SpringApplicationBuilder()
						.sources(Application.class)
						.web(WebApplicationType.NONE)
						.build();

		setProfiles(springApplication);

		springApplication.run(args);
	}

	//if no profile selected, set dev as profile
	private static void setProfiles(SpringApplication springApplication)
	{
		if (System.getProperty("spring.profiles.active") == null || System.getProperty("spring.profiles.active").isEmpty())
		{
			springApplication.setAdditionalProfiles("dev");
		}
	}

	private static void extractResourceIfNeeded(String filename, String destinationFolder) throws IOException
	{
		Path path = Paths.get(destinationFolder+ "/" + filename);
		if (Files.exists(path))
		{
			return;
		}

		boolean mkdirs = new File(destinationFolder).mkdirs();

		InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(filename);
		assert inputStream != null;
		FileUtils.copyInputStreamToFile(inputStream, path.toFile());
	}
}