import { Component, OnInit } from '@angular/core';
import { Router, Event, NavigationStart, NavigationEnd, NavigationError } from '@angular/router';

import { AuthService } from './services/auth/auth.service';
import { DataService } from './services/data/data.service';
import { NotificationsService } from './services/notifications/notifications.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent implements OnInit {
  
  title = 'swingItCrm';

  successMessage = '';
  infoMessage    = '';
  errorMessage   = '';

  constructor(public authService : AuthService,
              private notificationsService : NotificationsService,
              private dataService: DataService,
              private router: Router) 
  {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationStart) 
      {
        notificationsService.clear();
        // Show loading indicator
      }

      if (event instanceof NavigationEnd) 
      {
          // Hide loading indicator
      }

      if (event instanceof NavigationError) {
          // Hide loading indicator

          // Present error to user
          console.log(event.error);
      }
    });
  }

  ngOnInit() 
  {
      this.notificationsService.getClearMessageSubject().subscribe(() => {
        this.clear();
      })

      this.notificationsService.getSuccessMessageSubject().subscribe((successMessage : string) => {
        this.clear();
        this.successMessage = successMessage;
      })
  
      this.notificationsService.getInfoMessageSubject().subscribe((infoMessage : string) => {
        this.clear();
        this.infoMessage    = infoMessage;
      })
  
      this.notificationsService.getErrorMessageSubject().subscribe((errorMessage : string) => {
        this.clear();
        this.errorMessage   = errorMessage;
        console.log(event);
      })  
  }

  clear()
  {
    this.successMessage = '';
    this.infoMessage    = '';
    this.errorMessage   = '';
  }
}