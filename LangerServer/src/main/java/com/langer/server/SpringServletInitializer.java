package com.langer.server;

import com.google.common.collect.Lists;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.List;
import java.util.Map;

public class SpringServletInitializer extends SpringBootServletInitializer
{

	public static final String ENV_KEY_GAE_ENV = "GAE_ENV";
	public static final String ENV_KEY_USER = "USER";
	public static final String ENV_KEY_USERNAME = "USERNAME";
	public static final String ENV_KEY_GCLOUD_PROJECT = "GCLOUD_PROJECT";

	public static final String PROJECT_PROD_NAME = "langer";
	public static final String PROJECT_QA_NAME = "langer-2";

	public static final String PROFILE_PROD = "prod";
	public static final String PROFILE_QA = "qa";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		application.application().setAdditionalProfiles(resolveProfiles());
		return application.sources(Application.class);
	}

	private String[] resolveProfiles() //CAREFUL!
	{
		List<String> profiles = Lists.newArrayList();
		Map<String, String> env = System.getenv();

		if (env.containsKey(ENV_KEY_GAE_ENV) && env.get(ENV_KEY_GAE_ENV).equalsIgnoreCase("standard"))
		{
			if (env.containsKey(ENV_KEY_GCLOUD_PROJECT))
			{
				if (env.get(ENV_KEY_GCLOUD_PROJECT).equals(PROJECT_PROD_NAME))
				{
					profiles.add(PROFILE_PROD);
				}
				else if (env.get(ENV_KEY_GCLOUD_PROJECT).equals(PROJECT_QA_NAME))
				{
					profiles.add(PROFILE_QA);
				}
			}
		}
		else if (env.containsKey(ENV_KEY_USER))
		{
			profiles.add(env.get(ENV_KEY_USER).toLowerCase().replaceAll("^([0-9a-zA-Z]+).*","$1"));
		}
		else if (env.containsKey(ENV_KEY_USERNAME))
		{
			profiles.add(env.get(ENV_KEY_USERNAME).toLowerCase().replaceAll("^([0-9a-zA-Z]+).*","$1"));
		}

		return profiles.toArray(new String[]{});
	}
}

