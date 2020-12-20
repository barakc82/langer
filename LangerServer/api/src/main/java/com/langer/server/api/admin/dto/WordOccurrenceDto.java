package com.langer.server.api.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordOccurrenceDto
{
    private long    id;
    private WordDto word;
    private int     selectedTranslationIndex;
    private int     position;

    @JsonIgnore
    public WordTranslationDto getWordTranslation()
    {
        return word.getTranslations().get(selectedTranslationIndex);
    }

    public void setTranslation(String translation)
    {
        selectedTranslationIndex = IntStream.range(0, word.getTranslations().size())
                .filter(i -> word.getTranslations().get(i).getTranslation().equals(translation))
                .findAny().getAsInt();
    }
}