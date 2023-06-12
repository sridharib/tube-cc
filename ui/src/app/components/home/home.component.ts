import { Component, OnInit } from '@angular/core';
import { SearchResult } from './model/search-result';
import { HomeService } from './service/home.service';

declare var MediaElementPlayer: any;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  private _vidSrc: string = "https://www.youtube.com/watch?v=q3ZubzERSmY";
  private _trackSrc: string = "";
  private _searchTitle: string = "";
  private _searchResults: SearchResult[] = [];
  private _displayedColumns: string[] = ['subFileName', 'languageName'];
  private _player: any;

  constructor(private homeService: HomeService) { }

  ngOnInit(): void {
    var self = this;
    self._player = new MediaElementPlayer('player', {
      pluginPath: 'https://cdn.jsdelivr.net/npm/mediaelement@5.1.1/build/',
      stretching: 'fill',
      enableAutosize: false,
      startLanguage: 'en',
      success: function () { }
    });
    this.updatePlyrURL();
  }

  get vidSrc(): string {
    return this._vidSrc;
  }

  set vidSrc(vidSrc: string) {
    this._vidSrc = vidSrc;
  }

  get trackSrc(): string {
    return this._trackSrc;
  }

  get searchTitle(): string {
    return this._searchTitle;
  }

  set searchTitle(searchTitle: string) {
    this._searchTitle = searchTitle;
  }

  get searchResults(): SearchResult[] {
    return this._searchResults;
  }

  set searchResults(searchResults: SearchResult[]) {
    this._searchResults = searchResults;
  }

  get displayedColumns(): string[] {
    return this._displayedColumns;
  }

  set displayedColumns(displayedColumns: string[]) {
    this._displayedColumns = displayedColumns;
  }

  updatePlyrURL() {
    this._player.setSrc(this._vidSrc);
    this._player.setPoster('');
    this._player.load();
  }

  searchSubtitle() {
    this.homeService.searchSubtitle(this._searchTitle).subscribe((data: SearchResult[]) => {
      console.log(data);
      this._searchResults = data;
    });
  }

  loadSubtitle(row: SearchResult) {
    console.log(row);
    var self = this;
    self.homeService.loadSubtitle(row).subscribe((data: string) => {
      var vttText = self.convertSrtCue(data);
      var vttBlob = new Blob([vttText], { type: 'text/vtt' });
      var blobURL = URL.createObjectURL(vttBlob);
      console.log(blobURL);
      self._trackSrc = blobURL;
      console.log(self._player);
      self._player.rebuildtracks();
    });

  }

  private convertSrtCue(caption: string) {
    // remove all html tags for security reasons
    //srt = srt.replace(/<[a-zA-Z\/][^>]*>/g, '');

    let cue = "";
    let s = caption.split(/\n/);

    // concatenate muilt-line string separated in array into one
    while (s.length > 3) {
      for (var i = 3; i < s.length; i++) {
        s[2] += "\n" + s[i]
      }
      s.splice(3, s.length - 3);
    }

    let line = 0;

    // detect identifier
    if (!s[0].match(/\d+:\d+:\d+/) && s[1].match(/\d+:\d+:\d+/)) {
      cue += s[0].match(/\w+/) + "\n";
      line += 1;
    }

    // get time strings
    if (s[line].match(/\d+:\d+:\d+/)) {
      // convert time string
      let m = s[1].match(/(\d+):(\d+):(\d+)(?:,(\d+))?\s*--?>\s*(\d+):(\d+):(\d+)(?:,(\d+))?/);
      if (m) {
        cue += m[1] + ":" + m[2] + ":" + m[3] + "." + m[4] + " --> "
          + m[5] + ":" + m[6] + ":" + m[7] + "." + m[8] + "\n";
        line += 1;
      } else {
        // Unrecognized timestring
        return "";
      }
    } else {
      // file format error or comment lines
      return "";
    }

    // get cue text
    if (s[line]) {
      cue += s[line] + "\n\n";
    }

    return cue;
  }

}
