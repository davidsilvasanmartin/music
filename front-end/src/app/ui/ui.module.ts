import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatPaginatorModule } from '@angular/material/paginator';

import { CardModule } from './card/card.module';
import { IconsModule } from './icons/icons.module';
import { PaginationModule } from './pagination/pagination.module';
import { TopbarModule } from './topbar/topbar.module';

@NgModule({
  imports: [CommonModule],
  exports: [
    MatPaginatorModule,
    TopbarModule,
    IconsModule,
    CardModule,
    PaginationModule,
  ],
})
export class UiModule {}
