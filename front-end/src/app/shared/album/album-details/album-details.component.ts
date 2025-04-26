import { ChangeDetectionStrategy, Component, input } from '@angular/core';

import type { Album } from '../../../albums/album';
import { PlaylistComponentsModule } from '../../../ui/playlist-components/playlist-components.module';
import { UiModule } from '../../../ui/ui.module';

@Component({
  selector: 'app-album-details',
  templateUrl: './album-details.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [UiModule, PlaylistComponentsModule],
})
export class AlbumDetailsComponent {
  album = input.required<Album>();
}
