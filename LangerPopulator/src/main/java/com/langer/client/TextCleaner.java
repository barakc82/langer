package com.langer.client;

import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TextCleaner
{
    public static void main(String[] args) throws IOException
    {
        File resourceDir = new File("C:\\langer\\Portuguese\\A Guerra Dos Tronos");
        File[] episodeFiles = resourceDir.listFiles();
        File episodeFile = episodeFiles[episodeFiles.length-1];
        //for (File episodeFile : episodeFiles)
        {
            String text = FileUtils.readFileToString(episodeFile, "UTF-8");
            String[] lines = text.split("\r\n");
            StringBuffer cleanText = new StringBuffer();
            for (String line : lines)
            {
                line = line.trim();
                if (line.contains("literatura fantástica"))
                    continue;

                if (line.chars().noneMatch(c -> Character.isAlphabetic(c)))
                    continue;

                System.out.println((line.endsWith(".") || line.endsWith("!") || line.endsWith("?")));
                if (line.endsWith(".") || line.endsWith("!") || line.endsWith("?"))
                    cleanText.append(line + "\r\n");
                else
                    cleanText.append(line + " ");
            }

//            File newFile = new File(resourceDir.getAbsolutePath() + "\\" + episodeFile.getName());
            FileUtils.writeStringToFile(episodeFile, cleanText.toString(), "UTF-8");
        }
    }
}