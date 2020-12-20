import { WordStatistics } from './word-statistics.model';
import { SentenceStatistics } from './sentence-statistics.model';

export class CorpusStatistics
{
    wordTranslationStatisticsItems  : WordStatistics[];
    sentenceStatisticsItems         : SentenceStatistics[];
}