import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { SortComponent } from './sort.component';

@NgModule({
  declarations: [SortComponent],
  imports: [CommonModule, FormsModule],
  exports: [SortComponent],
})
export class SortModule {}
