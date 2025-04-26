import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { IconsModule } from '../../ui/icons/icons.module';
import { QueueAddComponent } from './queue-add.component';
import { QueueRemoveComponent } from './queue-remove.component';
import { QueueRemoveNoBgComponent } from './queue-remove-no-bg.component';
import { QueueReplaceComponent } from './queue-replace.component';

/**
 * This module exports components to modify the current play queue. These components
 * dispatch actions to the Queue store
 */
@NgModule({
  declarations: [
    QueueAddComponent,
    QueueRemoveComponent,
    QueueReplaceComponent,
    QueueRemoveNoBgComponent,
  ],
  imports: [CommonModule, IconsModule],
  exports: [
    QueueAddComponent,
    QueueRemoveComponent,
    QueueReplaceComponent,
    QueueRemoveNoBgComponent,
  ],
})
export class QueueComponentsModule {}
