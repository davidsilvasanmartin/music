import { Component, Input } from "@angular/core";

import { UntilDestroy } from "@ngneat/until-destroy";

import { Song } from "../../songs/song";

/**
 * @TODO remove this component. Hard-code song in PlayerPlaylistComponent
 */
@UntilDestroy()
@Component({
  selector: 'app-song',
  templateUrl: './song.component.html',
  styleUrls: ['./song.component.scss'],
})
export class SongComponent {
  @Input() song: Song = null as any;
}
