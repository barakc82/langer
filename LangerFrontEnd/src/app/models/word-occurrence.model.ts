import { Word } from './word.model';

export class WordOccurrence
{
    id                       : number;
    word                     : Word;
    selectedTranslationIndex : number;
}