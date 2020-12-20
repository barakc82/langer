import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { isDefined } from '@angular/compiler/src/util';
import { environment } from '../../../environments/environment'
import { NextCardRequest } from '../../models/next-card-request.model';
import { ConfirmIntroductionRequest } from '../../models/confirm-introduction-request.model';


@Injectable({
  providedIn: 'root'
})
export class DataService 
{
  private dataUrl = environment.host + "/data";

  constructor(private http : HttpClient) { }

  getLanguages()
  {
    return this.http.get<any>(this.dataUrl + "/language");
  }

  getNextCard(nextCardRequest : NextCardRequest)
  {
    return this.http.post<any>(this.dataUrl + "/next-card", nextCardRequest);
  }

  confirmIntroduction(confirmIntroductionRequest : ConfirmIntroductionRequest)
  {
    return this.http.post<any>(this.dataUrl + "/confirm-introduction", confirmIntroductionRequest);
  }
}