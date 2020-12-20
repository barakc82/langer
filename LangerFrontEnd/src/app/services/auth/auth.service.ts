import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment'

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl = environment.host + "/auth";
  private registerUrl = this.authUrl + "/register";
  private loginUrl    = this.authUrl + "/login";

  constructor(private http : HttpClient,
              private router : Router) { }

  registerUser(user)
  {
    return this.http.post<any>(this.registerUrl, user);
  }

  loginUser(user)
  {
    return this.http.post<any>(this.loginUrl, user);
  }

  isLoggedIn()
  {
    return !!localStorage.getItem('token'); 
  }

  getToken()
  {
    return localStorage.getItem('token');
  }

  logoutUser()
  {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  isAdmin()
  {
    return true;
  }
}