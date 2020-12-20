import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { Language } from '../../models/language.model';

@Component({
  selector: 'app-admin-language',
  templateUrl: './admin-language.component.html',
  styleUrls: ['./admin-language.component.less']
})
export class AdminLanguageComponent implements OnInit {

  language       : Language;

  constructor(private adminService: AdminService,
              private notificationsService : NotificationsService,
              private router: ActivatedRoute) {}

  ngOnInit() 
  {
    let languageName = this.router.snapshot.paramMap.get('languageName');
    
    this.adminService.getLanguage(languageName).subscribe(
      language => this.language = language,
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }
}
