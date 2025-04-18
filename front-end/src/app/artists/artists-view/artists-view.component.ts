import { Component, input } from '@angular/core';

import { UntilDestroy } from '@ngneat/until-destroy';
import { Store } from '@ngrx/store';

import type { Artist } from '../artist';

@UntilDestroy()
@Component({
  selector: 'app-artists-view',
  templateUrl: './artists-view.component.html',
  styles: ':host { display: contents;}',
})
export class ArtistsViewComponent {
  artist = input.required<Artist>();

  constructor(private readonly _store: Store) {}
}
