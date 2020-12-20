import { Language } from './language.model';
import { Resource } from './resource.model';
import { Episode } from './episode.model';

export class FindEpisodeResponse
{
    language    : Language;
    resource    : Resource;
    episode     : Episode;
}