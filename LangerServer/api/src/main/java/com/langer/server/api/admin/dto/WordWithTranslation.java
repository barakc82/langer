package com.langer.server.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordWithTranslation
{
    WordDto             word;
    WordTranslationDto  wordTranslation;

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        WordWithTranslation other = (WordWithTranslation) obj;
        return word.getId() == other.word.getId() && wordTranslation.getId() == other.wordTranslation.getId();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = prime * word.hashCode();
        result = prime * result + wordTranslation.hashCode();
        return result;
    }
}