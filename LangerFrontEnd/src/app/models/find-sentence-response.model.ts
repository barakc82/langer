import { Language } from './language.model';
import { Resource } from './resource.model';
import { Episode } from './episode.model';
import { Sentence } from './sentence.model';

export class FindSentenceResponse
{
    language    : Language;
    resource    : Resource;
    episode     : Episode;
    sentence    : Sentence;
}