import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  private successMessage = new Subject();
  private infoMessage    = new Subject();
  private errorMessage   = new Subject();
  private clearMessage   = new Subject();

  constructor() { }

  public clear()
  {
    this.clearMessage.next();
  }

  public getClearMessageSubject()
  {
    return this.clearMessage;
  }

  public notifySuccess(message : string)
  {
    this.successMessage.next(message);
  }

  public getSuccessMessageSubject()
  {
    return this.successMessage;
  }

  public notifyProcessing()
  {
    this.infoMessage.next("Processing...");
  }

  public getInfoMessageSubject()
  {
    return this.infoMessage;
  }

  public notifyError(message : string)
  {
    this.errorMessage.next(message);
  }

  public getErrorMessageSubject()
  {
    return this.errorMessage;
  }
}