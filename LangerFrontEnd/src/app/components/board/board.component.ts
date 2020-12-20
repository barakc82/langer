import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { DataService } from '../../services/data/data.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { NextCardRequest } from '../../models/next-card-request.model';
import { ConfirmIntroductionRequest } from '../../models/confirm-introduction-request.model';
import { Card } from '../../models/cards/card.model';


@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.less']
})
export class BoardComponent implements OnInit {

  languageName  : string;
  card          : Card;

  constructor(private activatedRouter: ActivatedRoute,
              private dataService: DataService,
              private notificationsService : NotificationsService) {}

  ngOnInit() 
  {
    this.languageName    = this.activatedRouter.snapshot.paramMap.get('languageName');

    let nextCardRequest = new NextCardRequest();
    nextCardRequest.languageName = this.languageName;
    console.log(nextCardRequest);
    this.dataService.getNextCard(nextCardRequest).subscribe(
      card => { this.card = card; console.log(card) },
      error => 
      { 
        this.notificationsService.notifyError(error);
        console.log(error) 
      }
    );
  }

  onConfirm()
  {
    let confirmIntroductionRequest = new ConfirmIntroductionRequest();
    confirmIntroductionRequest.wordTranslationId = this.card.translationIntroductionCard.wordTranslationId;
    this.dataService.confirmIntroduction(confirmIntroductionRequest).subscribe(
      card => { this.card = card; console.log(card) },
      error => 
      { 
        this.notificationsService.notifyError(error);
        console.log(error) 
      }
    );
  }
}