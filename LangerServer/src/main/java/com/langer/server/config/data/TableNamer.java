package com.langer.server.config.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TableNamer
{
    private Environment environment;

    private String activeProfile = null;

    @Value("${langer.data.prefix}")
    private String prefix;

    @Autowired
    public TableNamer(Environment environment)
    {
        this.environment = environment;
    }

    public String name(String text)
    {
        return String.format("%s_%s", getPrefix(), text);
    }

    private String getPrefix()
    {
        if (prefix != null && !prefix.isEmpty())
        {
            return prefix;
        }
        return getActiveProfile();
    }


    private String getActiveProfile()
    {
        if (activeProfile == null)
        {
            String[] profiles = environment.getActiveProfiles();
            if (profiles.length <= 0)
            {
                throw new IllegalArgumentException("must have an active profile");
            }

            activeProfile = profiles[0];
        }

        return activeProfile;
    }

}
