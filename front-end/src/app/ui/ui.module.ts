import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDialogModule } from '@angular/material/dialog';
import { IconsModule } from './icons/icons.module';
import { TopbarModule } from './topbar/topbar.module';
import { CardModule } from './card/card.module';

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
