import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IconsModule } from '../icons/icons.module';
import { SearchComponent } from './search.component';

@NgModule({
  declarations: [SearchComponent],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, IconsModule],
  exports: [SearchComponent],
})
export class SearchModule {}
