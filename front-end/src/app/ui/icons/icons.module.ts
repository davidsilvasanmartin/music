import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { IconCancelComponent } from './icon-cancel.component';
import { IconChevronDoubleLeftComponent } from './icon-chevron-double-left.component';
import { IconChevronDoubleRightComponent } from './icon-chevron-double-right.component';
import { IconChevronDownComponent } from './icon-chevron-down.component';
import { IconChevronLeftComponent } from './icon-chevron-left.component';
import { IconChevronRightComponent } from './icon-chevron-right.component';
import { IconChevronUpComponent } from './icon-chevron-up.component';
import { IconEyeComponent } from './icon-eye.component';
import { IconPlayComponent } from './icon-play.component';
import { IconPlayNextComponent } from './icon-play-next.component';
import { IconPlayPreviousComponent } from './icon-play-previous.component';
import { IconPlaylistAddComponent } from './icon-playlist-add.component';
import { IconUserPlaylistAddComponent } from './icon-user-playlist-add.component';

@NgModule({
  declarations: [
    IconEyeComponent,
    IconPlayComponent,
    IconPlaylistAddComponent,
    IconUserPlaylistAddComponent,
    IconCancelComponent,
    IconChevronDoubleLeftComponent,
    IconChevronDoubleRightComponent,
    IconChevronLeftComponent,
    IconChevronRightComponent,
    IconChevronUpComponent,
    IconChevronDownComponent,
    IconPlayNextComponent,
    IconPlayPreviousComponent,
  ],
  imports: [CommonModule],
  exports: [
    IconEyeComponent,
    IconPlayComponent,
    IconPlaylistAddComponent,
    IconUserPlaylistAddComponent,
    IconCancelComponent,
    IconChevronDoubleLeftComponent,
    IconChevronDoubleRightComponent,
    IconChevronLeftComponent,
    IconChevronRightComponent,
    IconChevronUpComponent,
    IconChevronDownComponent,
    IconPlayNextComponent,
    IconPlayPreviousComponent,
  ],
})
export class IconsModule {}
