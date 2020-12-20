package com.langer.server.service.importancecalculators;

import com.langer.server.service.ImportanceCalculator;
import com.langer.server.service.importancecalculators.RussianImportanceCalculator;
import com.langer.server.service.importancecalculators.SpanishImportanceCalculator;
import com.langer.server.service.importancecalculators.UkrainianImportanceCalculator;
import org.springframework.stereotype.Service;

@Service
public class ImportanceCalculatorFactory
{
    public ImportanceCalculator create(String languageName)
    {
        switch (languageName)
        {
            case "Russian":
                return new RussianImportanceCalculator();

            case "Spanish":
                return new SpanishImportanceCalculator();

            case "Ukrainian":
                return new UkrainianImportanceCalculator();

            case "Portuguese":
                return new PortugueseImportanceCalculator();
        }
        return null;
    }
}