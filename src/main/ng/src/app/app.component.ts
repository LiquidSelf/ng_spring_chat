import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from "./auth.service";
import {AppMessageService} from "./app-message.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private app: AuthService,
              private http: HttpClient,
              private router: Router,
              private auth: AuthService,
              public appMsgs: AppMessageService,
  ) {
  }
}
