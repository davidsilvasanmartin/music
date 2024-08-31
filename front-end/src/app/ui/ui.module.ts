import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';

import { CardModule } from './card/card.module';
import { IconsModule } from './icons/icons.module';
import { TopbarModule } from './topbar/topbar.module';

@NgModule({
  imports: [CommonModule],
  exports: [
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatExpansionModule,
    MatPaginatorModule,
    MatDialogModule,
    TopbarModule,
    IconsModule,
    CardModule,
  ],
})
export class UiModule {}
