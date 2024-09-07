import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { CardModule } from './card/card.module';
import { IconsModule } from './icons/icons.module';
import { PaginationModule } from './pagination/pagination.module';
import { PlayerComponentsModule } from './player-components/player-components.module';
import { TopbarModule } from './topbar/topbar.module';

@NgModule({
  imports: [CommonModule],
  exports: [
    TopbarModule,
    IconsModule,
    CardModule,
    PaginationModule,
    PlayerComponentsModule,
  ],
})
export class UiModule {}
