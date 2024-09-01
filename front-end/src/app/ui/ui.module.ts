import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatPaginatorModule } from '@angular/material/paginator';

import { CardModule } from './card/card.module';
import { IconsModule } from './icons/icons.module';
import { PaginatorModule } from './paginator/paginator.module';
import { TopbarModule } from './topbar/topbar.module';

@NgModule({
  imports: [CommonModule],
  exports: [
    MatPaginatorModule,
    TopbarModule,
    IconsModule,
    CardModule,
    PaginatorModule,
  ],
})
export class UiModule {}
