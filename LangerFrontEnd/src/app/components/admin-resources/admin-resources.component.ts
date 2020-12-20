import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AdminService } from '../../services/admin/admin.service';
import { NotificationsService } from '../../services/notifications/notifications.service';
import { Language } from '../../models/language.model';
import { Resource } from '../../models/resource.model';

@Component({
  selector: 'app-admin-resources',
  templateUrl: './admin-resources.component.html',
  styleUrls: ['./admin-resources.component.less']
})
export class AdminResourcesComponent implements OnInit {

  @Input() language   : Language;

  resources  : Resource[];
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

      this.adminService.getResources(this.language.id).subscribe(
        resources => this.resources = resources,
        err => {
          console.log('Operation failed: ' + err.error.message);
        }
      );
  }

  onUpload()
  {
    let resource        = new Resource();
    resource.title      = this.form.controls.title.value;
    resource.text       = this.form.controls.text.value;
    resource.languageId = this.language.id;

    this.adminService.createResource(resource).subscribe(
      () => 
      {
        this.notificationsService.notifySuccess("The resource " + resource.title + " was created successfully");
      },
      err => {
        console.log('Operation failed: ' + err.error.message);
      }
    );
  }

  encodeUri(uri : string) { return encodeURI(uri); }
}
