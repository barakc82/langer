package com.langer.server.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dictionary
{
    String                      languageName;
    List<DictionaryWord>        words;
    List<DictionarySentence>    sentences;
    List<String>                sentencesToIgnore;
}
