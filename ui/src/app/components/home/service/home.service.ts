import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SearchResult } from '../model/search-result';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private http: HttpClient) {
  }

  public searchSubtitle(title: string): Observable<SearchResult[]> {
    return this.http.get<SearchResult[]>(environment.URL.SEARCH_SUBTITLE + '?query=' + title);
  }

  public loadSubtitle(row: SearchResult): Observable<string> {
    return this.http.post(environment.URL.LOAD_SUBTITLE, row, { responseType: 'text' });
  }

}
