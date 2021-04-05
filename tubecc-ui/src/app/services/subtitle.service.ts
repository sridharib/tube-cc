import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SubtitleService {

  private url: string;
  constructor(private http: HttpClient) {
    this.url = 'http://localhost:8080/api/v1';
  }

  public searchSubtitle(title: string) {
    return this.http.get(this.url + '/search?query=' + title);
  }
  
}
