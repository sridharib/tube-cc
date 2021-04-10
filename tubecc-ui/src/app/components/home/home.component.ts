import { Component, OnInit } from '@angular/core';

import { SubtitleService } from 'src/app/services/subtitle.service';
import { SearchResult } from 'src/app/model/search-result';

import videojs from 'video.js';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  public vidSrc: string = "";
  public searchTitle: string = "";
  public searchResults: Array<SearchResult> = [];
  public displayedColumns: string[] = ['subFileName', 'languageName'];

  constructor(private subtitleService: SubtitleService) { }

  ngOnInit(): void {  
  //var player = videojs('vid1');
    console.log();
  }

  public updatePlyrURL() {
    const vidProvider = this.vidSrc.includes('youtube') ? 'youtube' : 'vimeo';
  }

  public searchSubtitle() {
    this.subtitleService.searchSubtitle(this.searchTitle).subscribe((data: Array<SearchResult>) => {
      this.searchResults = data;
    });
  }

  public loadSubtitle(row: SearchResult) {
    console.log(row);
    this.subtitleService.loadSubtitle(row).subscribe((data: string) => {
      var vttText = this.convertSrtCue(data);
      var vttBlob = new Blob([vttText], { type: 'text/vtt' });
      var blobURL = URL.createObjectURL(vttBlob);

      console.log(blobURL);
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
