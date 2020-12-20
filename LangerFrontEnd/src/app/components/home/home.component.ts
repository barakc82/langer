import { Component, OnInit } from '@angular/core';
import { Language } from '../../models/language.model';
import { DataService } from '../../services/data/data.service';
import { NotificationsService } from '../../services/notifications/notifications.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.less']
})
export class HomeComponent implements OnInit 
{
  languages       : Language[];

  constructor(private dataService: DataService,
              private notificationsService : NotificationsService) {}

  ngOnInit() 
  {
    this.dataService.getLanguages().subscribe(
      languages => { this.languages = languages },
      error => 
      { 
        this.notificationsService.notifyError(error);
        console.log(error) 
      }
    );
  }
}