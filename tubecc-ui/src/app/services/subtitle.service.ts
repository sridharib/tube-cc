import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

import { SearchResult } from 'src/app/model/search-result';

@Injectable({
  providedIn: 'root'
})
export class SubtitleService {

  private url: string;
  constructor(private http: HttpClient) {
    this.url = '/api/v1';
  }

  public searchSubtitle(title: string) {
    return this.http.get<Array<SearchResult>>(this.url + '/search?query=' + title);
  }

  public loadSubtitle(row: SearchResult) {
    return this.http.post(this.url + '/loadSubtitle', row, { responseType: 'text' });
  }

}
