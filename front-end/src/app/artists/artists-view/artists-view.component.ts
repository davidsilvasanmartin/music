import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { Store } from '@ngrx/store';

import { filter } from 'rxjs';
import { map } from 'rxjs/operators';

import { loadArtist } from './store/actions';
import { selectArtist } from './store/selectors';

@UntilDestroy()
@Component({
  selector: 'app-artists-view',
  templateUrl: './artists-view.component.html',
  styles: ':host { display: contents; }',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ArtistsViewComponent implements OnInit {
  artist = this._store.selectSignal(selectArtist);

  constructor(
    private readonly _store: Store,
    private readonly _route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this._route.params
      .pipe(
        map((params: Params) => params['artistId']),
        filter((artistId): artistId is string => !!artistId),
        map((artistId: string) => parseInt(artistId, 10)),
        filter((artistId: number) => !isNaN(artistId)),
        untilDestroyed(this),
      )
      .subscribe((id: number) => {
        this._store.dispatch(loadArtist({ id }));
      });
  }
}
