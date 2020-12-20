import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService} from '../../services/auth/auth.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {

  loginUserData : any = {};
  error               = '';

  constructor(private authService : AuthService,
              private router : Router) { }

  ngOnInit() {
  }

  loginUser()
  {
    this.error = '';
    this.authService.loginUser(this.loginUserData).subscribe(
      res => 
      {
        localStorage.setItem('token', res.token);
        this.router.navigate(['/']);
      },
      err =>
      {
        this.error = err.error.message;
      }
    )
  }
}