import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { CardModule } from './card/card.module';
import { IconsModule } from './icons/icons.module';
import { PaginationModule } from './pagination/pagination.module';
import { QueueComponentsModule } from './queue-components/queue-components.module';

@NgModule({
  imports: [CommonModule],
  exports: [IconsModule, CardModule, PaginationModule, QueueComponentsModule],
})
export class UiModule {}
