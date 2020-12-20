import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.less']
})
export class RegisterComponent implements OnInit {

  registerUserData : any = {};
  success                = '';
  error                  = '';

  constructor(private authService : AuthService,
              private router : Router) { }

  ngOnInit() {
  }

  registerUser() 
  {
    this.success = '';
    this.error   = '';
    this.authService.registerUser(this.registerUserData)
      .subscribe(
        res => 
        {
          console.log(res);
          this.success = res.message;
        },
        err =>
        {
          console.log(err);
          this.error = err.error.message;
        }
      );
  }
}