import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconsModule } from '../../ui/icons/icons.module';

@Component({
  selector: 'app-mb-link',
  template: `@if (mbId()) {
    <div class="flex items-center gap-1">
      <span class="font-medium">MB ID:</span>
      <a
        [href]="'https://musicbrainz.org/' + mbUrl() + '/' + mbId()"
        target="_blank"
        rel="noopener noreferrer"
        class="inline-flex items-center gap-1 hover:text-gray-700 hover:underline"
        title="View on MusicBrainz (external link)"
      >
        {{ mbId() }}
        <app-icon-external-link class="size-3.5" />
      </a>
    </div>
  }`,
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterModule, IconsModule],
})
export class MbLinkComponent {
  mbId = input.required<string | null | undefined>();
  mbUrl = input.required<string>();
}
