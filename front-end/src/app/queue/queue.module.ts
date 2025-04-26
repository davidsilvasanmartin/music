import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { PlaylistComponentsModule } from '../shared/playlist-components/playlist-components.module';
import { QueueComponentsModule } from '../shared/queue-components/queue-components.module';
import { IconsModule } from '../ui/icons/icons.module';
import { QueueListComponent } from './queue-list.component';
import { QueuePlayerComponent } from './queue-player.component';
import { QueueEffects } from './store/effects';
import { queueReducer } from './store/reducers';

@NgModule({
  declarations: [QueuePlayerComponent, QueueListComponent],
  imports: [
    CommonModule,
    RouterModule,
    PlaylistComponentsModule,
    StoreModule.forFeature('queue', queueReducer),
    EffectsModule.forFeature([QueueEffects]),
    IconsModule,
    QueueComponentsModule,
  ],
  exports: [QueuePlayerComponent],
})
export class QueueModule {}
