package com.langer.server.service;

import com.langer.server.model.entity.impl.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutomaticTranslatorFactory
{
    public AutomaticTranslator create(TextStorage textStorage, Language language)
    {
        switch (language.getName())
        {
            case "Russian":
                return new RussianAutomaticTranslator(textStorage, language.getId());
         }
        return null;
    }
}