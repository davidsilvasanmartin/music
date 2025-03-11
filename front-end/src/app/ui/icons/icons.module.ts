import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { IconCancelComponent } from './icon-cancel.component';
import { IconChevronDoubleLeftComponent } from './icon-chevron-double-left.component';
import { IconChevronDoubleRightComponent } from './icon-chevron-double-right.component';
import { IconChevronDownComponent } from './icon-chevron-down.component';
import { IconChevronLeftComponent } from './icon-chevron-left.component';
import { IconChevronRightComponent } from './icon-chevron-right.component';
import { IconChevronUpComponent } from './icon-chevron-up.component';
import { IconEighthNoteComponent } from './icon-eighth-note.component';
import { IconEyeComponent } from './icon-eye.component';
import { IconHomeComponent } from './icon-home';
import { IconMenuListComponent } from './icon-menu-list';
import { IconMicComponent } from './icon-mic';
import { IconPlayComponent } from './icon-play.component';
import { IconPlayNextComponent } from './icon-play-next.component';
import { IconPlayPreviousComponent } from './icon-play-previous.component';
import { IconPlaylistAddComponent } from './icon-playlist-add.component';
import { IconSearchComponent } from './icon-search.component';
import { IconUserPlaylistAddComponent } from './icon-user-playlist-add.component';
import { IconVynilComponent } from './icon-vynil';

// Some are Feather Icons, others are random open source icons I found
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
    IconSearchComponent,
    IconEighthNoteComponent,
    IconMenuListComponent,
    IconHomeComponent,
    IconVynilComponent,
    IconMicComponent,
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
    IconSearchComponent,
    IconEighthNoteComponent,
    IconMenuListComponent,
    IconHomeComponent,
    IconVynilComponent,
    IconMicComponent,
  ],
})
export class IconsModule {}
