package com.langer.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.langer.server.api.admin.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Populator implements CommandLineRunner
{
    private final AdminClient adminClient;

    public void run(String[] args)
    {
        try
        {
            storeDictionaries();
            /*populate("All");
            waitForMaintenanceToFinish();
            applyDictionaries();*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("error, bummer");
        }
    }

    private void waitForMaintenanceToFinish() throws InterruptedException
    {
        System.out.println("Starting to wait for maintenance to finish");
        Thread.sleep(500);
        while (adminClient.isUnderMaintenance())
        {
            System.out.println("Waiting for maintenance to finish");
            Thread.sleep(500);
        }
    }

    private void applyDictionaries() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Dictionary>> dictionaryListType = new TypeReference<List<Dictionary>>() {};
        List<Dictionary> dictionaries = mapper.readValue(new File("c:\\langer\\dictionaries.txt"), dictionaryListType);
        adminClient.applyDictionaries(dictionaries);
    }

    private void populate(String languageName) throws IOException
    {
        adminClient.clear(languageName);

        File root = new File("C:\\Langer");
        for (File languageDir : root.listFiles())
        {
            if (!languageDir.isDirectory() || languageDir.getName().equals("temp"))
                continue;

            String name = languageDir.getName();

            if (!languageName.equals("All") && !name.equals(languageName))
                continue;

            LanguageDto language = adminClient.getLanguage(name);
            if (language == null)
            {
                language = LanguageDto.builder().name(name).build();
                long languageId = adminClient.createLanguage(language);
                language.setId(languageId);
            }
            uploadResources(languageDir, language.getId());
        }
    }

    private void storeDictionaries() throws IOException {
        List<Dictionary> dictionaries = adminClient.getDictionaries();
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File("c:\\langer\\dictionaries.txt"), dictionaries);
    }

    private void uploadResources(File languageDir, long languageId) throws IOException
    {
        for (File resourceDir : languageDir.listFiles())
        {
            String title = resourceDir.getName();
            if (title.endsWith(".pdf"))
            {
                System.out.println("Skipping " + title);
                continue;
            }

            System.out.println("Working on resource " + title);

            ResourceDto resourceDto = ResourceDto.builder()
                    .title(title)
                    .languageId(languageId)
                    .build();
            long resourceId = adminClient.createResource(resourceDto);

            uploadEpisodes(resourceDir, resourceId);
        }
    }

    private void uploadEpisodes(File resourceDir, long resourceId) throws IOException
    {
        for (File episodeFile : resourceDir.listFiles())
        {
            String title = episodeFile.getName().replaceAll("\\.txt", "");
            System.out.println("Creating episode " + title);

            String text = FileUtils.readFileToString(episodeFile, "UTF-8");

            EpisodeDto episodeDto = EpisodeDto.builder()
                    .title(title)
                    .text(text)
                    .resourceId(resourceId)
                    .build();
            adminClient.createEpisode(episodeDto);
        }
    }
}
