import { Component, OnInit } from '@angular/core';

import { SubtitleService } from 'src/app/services/subtitle.service';
import { SearchResult } from 'src/app/model/search-result';

import * as Plyr from 'plyr';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  private player: Plyr = new Plyr('#plyrID');
  public vidSrc: string = "";
  public searchTitle: string = "";
  public searchResults: Array<SearchResult> = [];
  public displayedColumns: string[] = ['subFileName', 'languageName'];

  constructor(private subtitleService: SubtitleService) { }

  ngOnInit(): void {
    this.player = new Plyr('#plyrID', { captions: { active: true } });
  }

  public updatePlyrURL() {
    const vidProvider = this.vidSrc.includes('youtube') ? 'youtube' : 'vimeo';
    this.player.source = {
      type: 'video',
      sources: [
        {
          src: this.vidSrc,
          provider: vidProvider,
        },
      ],
    };
    this.vidSrc = '';
  }

  public searchSubtitle() {
    this.subtitleService.searchSubtitle(this.searchTitle).subscribe((data: Array<SearchResult>) => {
      this.searchResults = data;
    });
  }

}
