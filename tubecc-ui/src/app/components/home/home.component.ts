import { Component, OnInit } from '@angular/core';
import {SubtitleService} from '../../services/subtitle.service';
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

  constructor(private subtitleService : SubtitleService) {}

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
    this.subtitleService.searchSubtitle(this.searchTitle).subscribe((data: any) => {
      console.log(data);
    });
  }

}
