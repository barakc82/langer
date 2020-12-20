import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { Language } from '../../models/language.model';
import { Resource } from '../../models/resource.model';
import { FindEpisodeRequest } from '../../models/find-episode-request.model';
import { Episode } from 'src/app/models/episode.model';

@Component({
  selector: 'app-admin-episode',
  templateUrl: './admin-episode.component.html',
  styleUrls: ['./admin-episode.component.less']
})
export class AdminEpisodeComponent implements OnInit {

  language      : Language;
  resource      : Resource;
  episode       : Episode;

  constructor(private adminService: AdminService,
              private notificationsService : NotificationsService,
              private router: Router,
              private activatedRouter: ActivatedRoute) {}

  ngOnInit() 
  {
    let languageName  = this.activatedRouter.snapshot.paramMap.get('languageName');
    let resourceTitle = this.activatedRouter.snapshot.paramMap.get('resourceTitle');
    let episodeTitle  = this.activatedRouter.snapshot.paramMap.get('episodeTitle');
    
    let findEpisodeRequest           = new FindEpisodeRequest();
    
    findEpisodeRequest.languageName  = languageName;
    findEpisodeRequest.resourceTitle = resourceTitle;
    findEpisodeRequest.episodeTitle  = episodeTitle;

    this.adminService.findEpisode(findEpisodeRequest).subscribe(
      findEpisodeResponse => 
      {
        this.language = findEpisodeResponse.language;
        this.resource = findEpisodeResponse.resource;
        console.log(findEpisodeResponse)
        this.episode  = findEpisodeResponse.episode;
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  onUpdateText(text : string)
  {
    this.adminService.updateEpisodeText(this.episode.id, text).subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("The text was set successfully");
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  delete()
  {
    this.adminService.deleteEpisode(this.episode.id).subscribe(
      () => 
      {
        this.router.navigate(['/' + this.language.name + '/' + this.resource.title]);
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }
}