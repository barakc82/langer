import { Component, OnInit, Input, HostListener, ChangeDetectorRef } from '@angular/core';
import { MatPaginator, MatTableDataSource, MatPaginatorIntl } from '@angular/material';


import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { Language } from '../../models/language.model';
import { SentenceStatistics } from '../../models/sentence-statistics.model';
import { CorpusStatistics } from '../../models/corpus-statistics.model';


@Component({
  selector: 'app-admin-corpus-statistics-sentences',
  templateUrl: './admin-corpus-statistics-sentences.component.html',
  styleUrls: ['./admin-corpus-statistics-sentences.component.less']
})
export class AdminCorpusStatisticsSentencesComponent implements OnInit {

  @Input() language   : Language;

  corpusStatistics  : CorpusStatistics;
  displayedColumns  : string[] =  ['index', 'value', 'translation', 'ranking'];
  dataSource        : MatTableDataSource<SentenceStatistics>;
  dataLength        : number = this.dataSource ? this.dataSource.data.length : 0;
  pageLength        : number = 30;
  paginator         : MatPaginator = new MatPaginator(new MatPaginatorIntl(), this.ref);

  constructor(private adminService: AdminService,
              private notificationsService : NotificationsService,
              private ref: ChangeDetectorRef) { }

  ngOnInit()
  {
    this.adminService.getCourpusStatistics(this.language.id).subscribe(
      corpusStatistics => 
      {
        this.corpusStatistics = corpusStatistics;
        console.log(corpusStatistics)
        this.dataSource = new MatTableDataSource(corpusStatistics.sentenceStatisticsItems);

        this.paginator.pageSize = this.pageLength;
        this.paginator.ngOnInit();
        this.dataSource.paginator = this.paginator;
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  @HostListener('window:scroll', ['$event'])
  onscroll(event) {
    const elem = event.currentTarget;
    if ((elem.innerHeight + elem.pageYOffset + 200) >= document.body.offsetHeight && this.pageLength <= this.dataLength) {
      this.pageLength += 30;
      this.paginator._changePageSize(this.pageLength);
    }
  }
}