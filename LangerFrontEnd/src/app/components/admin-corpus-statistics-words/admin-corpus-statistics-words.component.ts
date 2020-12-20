import { Component, OnInit, Input } from '@angular/core';
import { MatTableDataSource } from "@angular/material"

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { Language } from '../../models/language.model';
import { WordStatistics } from '../../models/word-statistics.model';
import { CorpusStatistics } from '../../models/corpus-statistics.model';

@Component({
  selector: 'app-admin-corpus-statistics-words',
  templateUrl: './admin-corpus-statistics-words.component.html',
  styleUrls: ['./admin-corpus-statistics-words.component.less']
})
export class AdminCorpusStatisticsWords implements OnInit {

  @Input() language   : Language;

  corpusStatistics  : CorpusStatistics;
  displayedColumns  : string[] =  ['index', 'value', 'count', 'inverse-frequency']
  dataSource        : MatTableDataSource<WordStatistics>

  constructor(private adminService: AdminService,
              private notificationsService : NotificationsService) { }

  ngOnInit() 
  {
    this.adminService.getCourpusStatistics(this.language.id).subscribe(
      corpusStatistics => 
      {
        this.corpusStatistics = corpusStatistics;
        this.dataSource = new MatTableDataSource(corpusStatistics.wordTranslationStatisticsItems);
        console.log(corpusStatistics.wordTranslationStatisticsItems);
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }
}
