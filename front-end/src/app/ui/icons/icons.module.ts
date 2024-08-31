import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { IconAddToPlayListComponent } from './icon-add-to-playlist/icon-add-to-playlist.component';
import { IconCancelComponent } from './icon-cancel/icon-cancel.component';
import { IconEyeComponent } from './icon-eye/icon-eye.component';
import { IconPlayComponent } from './icon-play/icon-play.component';

@NgModule({
  declarations: [
    IconEyeComponent,
    IconPlayComponent,
    IconAddToPlayListComponent,
    IconCancelComponent,
  ],
  imports: [CommonModule],
  exports: [
    IconEyeComponent,
    IconPlayComponent,
    IconAddToPlayListComponent,
    IconCancelComponent,
  ],
})
export class IconsModule {}
