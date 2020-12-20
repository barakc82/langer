import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment'

import { Language } from '../../models/language.model';
import { Resource } from '../../models/resource.model';
import { Episode } from '../../models/episode.model';
import { Word } from '../../models/word.model';
import { FindResourceRequest } from '../../models/find-resource-request.model';
import { FindResourceResponse } from '../../models/find-resource-response.model';
import { FindEpisodeRequest } from '../../models/find-episode-request.model';
import { FindEpisodeResponse } from '../../models/find-episode-response.model';
import { FindSentenceRequest } from '../../models/find-sentence-request.model';
import { FindSentenceResponse } from '../../models/find-sentence-response.model';
import { CreateWordTranslationRequest } from '../../models/create-word-translation-request.model';
import { SetWordOccurrenceTranslationRequest } from '../../models/set-word-occurrence-translation-request.model';
import { CorpusStatistics } from '../../models/corpus-statistics.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private adminUrl = environment.host + "/admin";

  constructor(private http : HttpClient) { }

  getLanguage(name : string)
  {
    return this.http.get<any>(this.adminUrl + "/language/" + name);
  }

  createLanguage(language : Language)
  {
    return this.http.post<Language>(this.adminUrl + "/language", language);
  }

  getResources(languageId : number)
  {
    return this.http.get<Resource[]>(this.adminUrl + "/resource?language-id=" + languageId);
  }

  findResource(findResourceRequest : FindResourceRequest)
  {
    return this.http.post<FindResourceResponse>(this.adminUrl + "/resource/find", findResourceRequest);
  }

  createResource(resource : Resource)
  {
    return this.http.post<number>(this.adminUrl + "/resource", resource);
  }

  deleteResource(resourceId : number)
  {
    return this.http.delete<void>(this.adminUrl + "/resource/" + resourceId)
  }

  getEpisodes(resourceId : number)
  {
    return this.http.get<Episode[]>(this.adminUrl + "/episode?resource-id=" + resourceId);
  }

  createEpisode(episode : Episode)
  {
    return this.http.post<number>(this.adminUrl + "/episode", episode);
  }

  findEpisode(findEpisodeRequest : FindEpisodeRequest)
  {
    return this.http.post<FindEpisodeResponse>(this.adminUrl + "/episode/find", findEpisodeRequest);
  }

  updateEpisodeText(episodeId : number, text : string)
  {
    return this.http.put<void>(this.adminUrl + "/episode/" + episodeId + "/update-text", text);
  }

  deleteEpisode(episodeId : number)
  {
    return this.http.delete<void>(this.adminUrl + "/episode/" + episodeId)
  }

  findSentence(findSentenceRequest : FindSentenceRequest)
  {
    return this.http.post<FindSentenceResponse>(this.adminUrl + "/sentence/find", findSentenceRequest);
  }

  updateSentenceTranslation(sentenceId : number, sentenceTranslation : string)
  {
    return this.http.put<any>(this.adminUrl + "/sentence/" + sentenceId + "/update-translation", sentenceTranslation);
  }

  updateSentenceShouldBeIgnored(sentenceId : number, shouldBeIgnored : boolean)
  {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.http.put<any>(this.adminUrl + "/sentence/" + sentenceId + "/update-should-be-ignored", shouldBeIgnored, {headers: headers});
  }

  getWord(wordId : number)
  {
    return this.http.get<Word>(this.adminUrl + "/word/" + wordId);
  }

  deleteWord(wordId : number)
  {
    return this.http.delete<Word>(this.adminUrl + "/word/" + wordId);
  }

  updateWordType(wordId : number, type : string)
  {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.http.put<void>(this.adminUrl + "/word/" + wordId + "/update-type", JSON.stringify(type), {headers: headers});
  }

  createWordTranslation(createWordTranslationRequest : CreateWordTranslationRequest)
  {
    return this.http.post<any>(this.adminUrl + "/word-translation", createWordTranslationRequest);
  }

  setTranslationForWordOccurrence(wordOccurrenceId : number, setWordOccurrenceTranslationRequest : SetWordOccurrenceTranslationRequest)
  {
    return this.http.put<any>(this.adminUrl + "/word-occurrence/" + wordOccurrenceId + "/set-translation", setWordOccurrenceTranslationRequest);
  }

  getUsers()
  {
    return this.http.get<any>(this.adminUrl + "/user");
  }

  getUserProgress(userId : number)
  {
    return this.http.get<any>(this.adminUrl + "/user/" + userId + "/progress");
  }

  clearUserProgress(userId : number)
  {
    return this.http.post<any>(this.adminUrl + "/user/" + userId + "/progress", null);
  }

  getCourpusStatistics(languageId : number)
  {
    return this.http.get<CorpusStatistics>(this.adminUrl + "/language/" + languageId + "/corpus-statistics");
  }

  restoreFromEpisodes()
  {
    return this.http.post<any>(this.adminUrl + "/restore-from-episodes", null);
  }

  verifyConsistency()
  {
    return this.http.post<any>(this.adminUrl + "/verify-consistency", null);
  }

  rebuildSentencesGraph()
  {
    return this.http.post<any>(this.adminUrl + "/rebuild-sentences-graph", null);
  }
}