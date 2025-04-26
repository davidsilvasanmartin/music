import { ChangeDetectionStrategy, Component, input } from '@angular/core';

import type { Album } from '../../../albums/album';
import { PlaylistComponentsModule } from '../../playlist-components/playlist-components.module';
import { QueueComponentsModule } from '../../queue-components/queue-components.module';

@Component({
  selector: 'app-album-details',
  templateUrl: './album-details.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [PlaylistComponentsModule, QueueComponentsModule],
})
export class AlbumDetailsComponent {
  album = input.required<Album>();
}
