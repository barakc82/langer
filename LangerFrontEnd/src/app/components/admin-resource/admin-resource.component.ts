import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { Language } from '../../models/language.model';
import { Resource } from '../../models/resource.model';
import { FindResourceRequest } from '../../models/find-resource-request.model';

@Component({
  selector: 'app-admin-resource',
  templateUrl: './admin-resource.component.html',
  styleUrls: ['./admin-resource.component.less']
})
export class AdminResourceComponent implements OnInit {

  language      : Language;
  resource      : Resource;

  constructor(private adminService: AdminService,
              private notificationsService : NotificationsService,
              private router: Router,
              private activatedRouter: ActivatedRoute) {}

  ngOnInit() 
  {
    let languageName = this.activatedRouter.snapshot.paramMap.get('languageName');
    let title        = this.activatedRouter.snapshot.paramMap.get('title');
    
    let findResourceRequest           = new FindResourceRequest();
    
    findResourceRequest.languageName  = languageName;
    findResourceRequest.resourceTitle = title;

    this.adminService.findResource(findResourceRequest).subscribe(
      findResourceResponse => 
      {
        this.language = findResourceResponse.language;
        this.resource = findResourceResponse.resource;
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  delete()
  {
    this.adminService.deleteResource(this.resource.id).subscribe(
      () => 
      {
        this.router.navigate(['/' + this.language.name]);
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }
}