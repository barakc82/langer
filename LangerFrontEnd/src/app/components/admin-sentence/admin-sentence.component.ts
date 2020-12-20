import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from "@angular/material"

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { FindSentenceRequest } from '../../models/find-sentence-request.model';
import { Sentence } from '../../models/sentence.model';
import { WordOccurrence } from '../../models/word-occurrence.model';
import { WordTranslation } from '../../models/word-translation.model';
import { SetWordOccurrenceTranslationRequest } from '../../models/set-word-occurrence-translation-request.model';


@Component({
  selector: 'app-admin-sentence',
  templateUrl: './admin-sentence.component.html',
  styleUrls: ['./admin-sentence.component.less']
})
export class AdminSentenceComponent implements OnInit {

  sentence              : Sentence;
  languageName          : string;
  resourceTitle         : string;
  episodeTitle          : string;
  paragraphIndex        : number;
  displayedColumns      : string[] = ['index', 'word', 'word-translation']
  dataSource            : MatTableDataSource<WordOccurrence>
  selectedTranslations  : WordTranslation[] = [];
  sentenceTranslation   : string;

  constructor(private adminService: AdminService,
    private notificationsService : NotificationsService,
    private router: Router,
    private activatedRouter: ActivatedRoute) {}

  ngOnInit() 
  {
    this.languageName    = this.activatedRouter.snapshot.paramMap.get('languageName');
    this.resourceTitle   = this.activatedRouter.snapshot.paramMap.get('resourceTitle');
    this.episodeTitle    = this.activatedRouter.snapshot.paramMap.get('episodeTitle');
    this.paragraphIndex  = parseInt(this.activatedRouter.snapshot.paramMap.get('paragraphIndex'));
    let sentenceIndex    = parseInt(this.activatedRouter.snapshot.paramMap.get('sentenceIndex'));
    
    let findSentenceRequest           = new FindSentenceRequest();
    
    findSentenceRequest.languageName    = this.languageName;
    findSentenceRequest.resourceTitle   = this.resourceTitle;
    findSentenceRequest.episodeTitle    = this.episodeTitle;
    findSentenceRequest.paragraphIndex  = this.paragraphIndex;
    findSentenceRequest.sentenceIndex   = sentenceIndex;

    this.adminService.findSentence(findSentenceRequest).subscribe(
      findSentenceResponse => 
      {
        this.sentence = findSentenceResponse.sentence;
        this.dataSource = new MatTableDataSource(this.sentence.wordOccurrences);
        this.selectedTranslations = this.sentence.wordOccurrences.map(wordOccurrence =>
          {
            return wordOccurrence.word.translations[wordOccurrence.selectedTranslationIndex];
          });
        console.log(this.sentence);
        console.log('selectedTranslations: ' + this.selectedTranslations[0]);
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  translationSelected(wordOccurrenceId : number, rowIndex : number, wordTranslation : WordTranslation)
  {
    let setWordOccurrenceTranslationRequest = new SetWordOccurrenceTranslationRequest();
    setWordOccurrenceTranslationRequest.sentenceId        = this.sentence.id;
    setWordOccurrenceTranslationRequest.wordPosition      = rowIndex;
    setWordOccurrenceTranslationRequest.wordTranslationId = wordTranslation.id;
    this.adminService.setTranslationForWordOccurrence(wordOccurrenceId, setWordOccurrenceTranslationRequest).subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("The translation was set successfully");
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  onUpdateTranslation()
  {
    this.adminService.updateSentenceTranslation(this.sentence.id, this.sentenceTranslation).subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("The sentence translation was set successfully");
        this.sentenceTranslation = "";
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  onUpdateShouldBeIgnored()
  {
    this.adminService.updateSentenceShouldBeIgnored(this.sentence.id, this.sentence.shouldBeIgnored).subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("The sentence 'should be ignored' field was updated successfully");
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }
}