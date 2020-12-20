import { Component, OnInit } from '@angular/core';

import { Language } from '../../models/language.model';
import { AdminService } from '../../services/admin/admin.service';
import { DataService } from '../../services/data/data.service';
import { NotificationsService } from '../../services/notifications/notifications.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.less']
})
export class AdminComponent implements OnInit {

  languages       : Language[];
  newLanguageName : string;

  constructor(private adminService: AdminService,
              private dataService: DataService,
              private notificationsService : NotificationsService) {}

  ngOnInit() 
  {
    this.dataService.getLanguages().subscribe(
      languages => { this.languages = languages },
      error => { console.log(error) }
    );
  }

  onCreateLanguage()
  {
    let newLanguage  = new Language();
    newLanguage.name = this.newLanguageName;
    this.adminService.createLanguage(newLanguage).subscribe( 
      () => 
      {
        this.languages.push(newLanguage);
        this.notificationsService.notifySuccess("The language " + this.newLanguageName + " was created successfully");
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  onRestoreFromEpisodes()
  {
    this.adminService.restoreFromEpisodes().subscribe( 
      () => 
      {
        this.notificationsService.notifySuccess("The language " + this.newLanguageName + " was created successfully");
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );   
  }

  onVerifyConsistency()
  {
    this.adminService.verifyConsistency().subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("Consistency check passed successfully");
      },
      err => 
      {
        this.notificationsService.notifyError(err);
        this.notificationsService.notifyError(err.error);
      }
    );   
  }

  onRebuildSentencesGraph()
  {
    this.adminService.rebuildSentencesGraph().subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("Sentences graph was rebuilt successfully");
      },
      err => 
      {
        this.notificationsService.notifyError(err);
        this.notificationsService.notifyError(err.error);
      }
    );   
  }
}