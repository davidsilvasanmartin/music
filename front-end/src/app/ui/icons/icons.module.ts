import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { IconAddToPlayListComponent } from './icon-add-to-playlist/icon-add-to-playlist.component';
import { IconCancelComponent } from './icon-cancel/icon-cancel.component';
import { IconEyeComponent } from './icon-eye/icon-eye.component';
import { IconFirstComponent } from './icon-first/icon-first.component';
import { IconLastComponent } from './icon-last/icon-last.component';
import { IconNextComponent } from './icon-next/icon-next.component';
import { IconPlayComponent } from './icon-play/icon-play.component';
import { IconPreviousComponent } from './icon-previous/icon-previous.component';

@NgModule({
  declarations: [
    IconEyeComponent,
    IconPlayComponent,
    IconAddToPlayListComponent,
    IconCancelComponent,
    IconFirstComponent,
    IconLastComponent,
    IconPreviousComponent,
    IconNextComponent,
  ],
  imports: [CommonModule],
  exports: [
    IconEyeComponent,
    IconPlayComponent,
    IconAddToPlayListComponent,
    IconCancelComponent,
    IconFirstComponent,
    IconLastComponent,
    IconPreviousComponent,
    IconNextComponent,
  ],
})
export class IconsModule {}
