import { WordTranslation } from './word-translation.model';

export class Word
{
    id           : number;
    value        : string;
    translations : WordTranslation[];
    type         : string;
}