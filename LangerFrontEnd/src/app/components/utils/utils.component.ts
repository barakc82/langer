import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';

@Component({
  selector: 'app-utils',
  templateUrl: './utils.component.html',
  styleUrls: ['./utils.component.less']
})
export class UtilsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  public static convertToUserFormat(date : Date)
  {
    return moment(date).format("DD/MM/YYYY");
  }

  public static convertToJsonFormat(date : Date)
  {
    return moment(date).format("YYYY-MM-DD");
  }
}
