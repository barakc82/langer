import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from "@angular/material"

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { Word } from '../../models/word.model';
import { WordTranslation } from '../../models/word-translation.model';
import { CreateWordTranslationRequest } from '../../models/create-word-translation-request.model';
import { isUndefined } from 'util';


@Component({
  selector: 'app-admin-word',
  templateUrl: './admin-word.component.html',
  styleUrls: ['./admin-word.component.less']
})
export class AdminWordComponent implements OnInit {

  word : Word;
  dataSource        : MatTableDataSource<WordTranslation>
  displayedColumns  : string[] =  ['translation', 'delete']
  newTranslation    : string;

  constructor(private adminService: AdminService,
    private notificationsService : NotificationsService,
    private router: Router,
    private activatedRouter: ActivatedRoute) {}

  ngOnInit() 
  {
    let wordId   = parseInt(this.activatedRouter.snapshot.paramMap.get('wordId'));
    this.adminService.getWord(wordId).subscribe(
      word => 
      {
        this.word = word;
        this.dataSource = new MatTableDataSource(this.word.translations);
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  onAddTranslation()
  {
    if (isUndefined(this.newTranslation) || this.newTranslation.length == 0)
      return;

    let createWordTranslationRequest = new CreateWordTranslationRequest();
    createWordTranslationRequest.wordId      = this.word.id;
    createWordTranslationRequest.translation = this.newTranslation;
    this.adminService.createWordTranslation(createWordTranslationRequest).subscribe(
      wordTranslationId => 
      {
        let newTranslation = new WordTranslation();
        newTranslation.id           = wordTranslationId;
        newTranslation.translation  = createWordTranslationRequest.translation;
        this.notificationsService.notifySuccess("The translation was added successfully");
        this.word.translations.push(newTranslation);
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  typeSelected(type : string)
  {
    this.adminService.updateWordType(this.word.id, type).subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("The type was set successfully");
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  deleteWord()
  {
    this.adminService.deleteWord(this.word.id).subscribe(
      () => 
      {
        this.router.navigate(['/admin']);
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );   
  }

  deleteWordTranslation(wordTranslation : WordTranslation)
  {/*
    this.adminService.deleteWordTranslation(wordTranslation.id).subscribe(
      () => 
      {
        
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );   */
  }
}