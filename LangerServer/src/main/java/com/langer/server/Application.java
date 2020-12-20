package com.langer.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
public class Application
{
	public static void main(String[] args)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jerusalem"));
		setPortFromEnvironment();//preparation for moving to java11 runtime in appengine.
		SpringApplication.run(Application.class, args);
	}

	private static void setPortFromEnvironment()
	{
		String port = System.getenv().get("PORT");
		if (port != null)
		{
			System.getProperties().put("server.port",port);
		}
	}
}