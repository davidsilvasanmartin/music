import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { IconsModule } from '../../ui/icons/icons.module';
import { AddToPlaylistModalComponent } from './add-to-playlist-modal.component';
import { PlaylistAddComponent } from './playlist-add.component';

/**
 * This module exports components to modify user playlists
 */
@NgModule({
  declarations: [PlaylistAddComponent, AddToPlaylistModalComponent],
  imports: [CommonModule, IconsModule, ReactiveFormsModule],
  exports: [PlaylistAddComponent],
})
export class PlaylistComponentsModule {}
