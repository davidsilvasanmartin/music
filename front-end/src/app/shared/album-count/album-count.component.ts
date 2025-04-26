import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconsModule } from '../../ui/icons/icons.module';

@Component({
  selector: 'app-album-count',
  template: `<div class="flex items-center gap-1.5" title="Total Albums">
    <app-icon-vynil class="size-4 text-slate-500" />
    <span>{{ albumCount() ?? 0 }} Albums</span>
  </div>`,
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterModule, IconsModule],
})
export class AlbumCountComponent {
  albumCount = input.required<number | null | undefined>();
}
