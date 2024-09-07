import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { IconsModule } from '../icons/icons.module';
import { PaginatorComponent } from './paginator.component';

@NgModule({
  declarations: [PaginatorComponent],
  imports: [CommonModule, FormsModule, IconsModule],
  exports: [PaginatorComponent],
})
export class PaginationModule {}
