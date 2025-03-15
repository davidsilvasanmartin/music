import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { UiModule } from '../ui/ui.module';
import { PlayerComponent } from './player.component';
import { PlayerPlaylistComponent } from './player-playlist/player-playlist.component';
import { PlayerEffects } from './store/effects';
import { playlistReducer } from './store/reducers';

@NgModule({
  declarations: [PlayerComponent, PlayerPlaylistComponent],
  imports: [
    CommonModule,
    UiModule,
    RouterModule,
    StoreModule.forFeature('playlist', playlistReducer),
    EffectsModule.forFeature([PlayerEffects]),
  ],
  exports: [PlayerComponent],
})
export class PlayerModule {}
