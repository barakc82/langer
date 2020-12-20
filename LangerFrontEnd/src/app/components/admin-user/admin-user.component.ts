import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from "@angular/material"

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { User } from '../../models/user.model';
import { UserLanguageProgress } from '../../models/user-progress/user-language-progress.model';
import { WordTranslationUserState } from '../../models/user-progress/word-translation-user-state.model';


@Component({
  selector: 'app-admin-user',
  templateUrl: './admin-user.component.html',
  styleUrls: ['./admin-user.component.less']
})
export class AdminUserComponent implements OnInit {

  userId                : number;
  userProgress          : UserLanguageProgress[];
  dataSources           : MatTableDataSource<WordTranslationUserState>[];
  tableDisplayedColumns : string[] =  ['index', 'source', 'translation', 'progress-state']

  constructor(private adminService: AdminService,
              private notificationsService : NotificationsService,
              private activatedRouter: ActivatedRoute) { }

  ngOnInit() 
  {
    this.userId = parseInt(this.activatedRouter.snapshot.paramMap.get('userId'));
    this.adminService.getUserProgress(this.userId).subscribe(
      userProgress => 
      {
        this.userProgress = userProgress;
        console.log(userProgress);
        this.dataSources  = userProgress.map(userLanguageProgress => new MatTableDataSource(userLanguageProgress.wordTranslationUserStates));
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  onClearProgress()
  {
    this.adminService.clearUserProgress(this.userId).subscribe(
      () => 
      {
        
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    ); 
  }
}