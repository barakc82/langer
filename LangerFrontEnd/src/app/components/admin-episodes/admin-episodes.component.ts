import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { Language } from '../../models/language.model';
import { Resource } from '../../models/resource.model';
import { Episode } from '../../models/episode.model';

@Component({
  selector: 'app-admin-episodes',
  templateUrl: './admin-episodes.component.html',
  styleUrls: ['./admin-episodes.component.less']
})
export class AdminEpisodesComponent implements OnInit {

  @Input() language   : Language;
  @Input() resource   : Resource;

  episodes   : Episode[];
  form       : FormGroup;

  constructor(private adminService: AdminService,
              private notificationsService : NotificationsService,
              private formBuilder: FormBuilder,) {}

  ngOnInit() 
  {
    this.form = this.formBuilder.group(
      {
        title        : ['', [Validators.required]],
        text         : ['', [Validators.required]],
      });

      this.adminService.getEpisodes(this.resource.id).subscribe(
        episodes => this.episodes = episodes,
        err => {
          console.log('Operation failed: ' + err.error.message);
        }
      );
  }

  onUpload()
  {
    let episode        = new Episode();
    episode.title      = this.form.controls.title.value;
    episode.text       = this.form.controls.text.value;
    episode.languageId = this.language.id;
    episode.resourceId = this.resource.id;

    this.adminService.createEpisode(episode).subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("The resource " + episode.title + " was created successfully");
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  encodeUri(uri : string) { return encodeURI(uri); }
}
