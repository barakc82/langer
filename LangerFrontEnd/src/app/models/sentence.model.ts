import { WordOccurrence } from './word-occurrence.model';

export class Sentence
{
    id              : number;
    text            : string;
    wordOccurrences : WordOccurrence[];
    languageName    : string;
    resourceTitle   : string;
    episodeTitle    : string;
    paragraphIndex  : number;
    sentenceIndex   : number;
    translation     : string;
    shouldBeIgnored : boolean;
}