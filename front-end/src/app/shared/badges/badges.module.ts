import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GenreBadgeComponent } from './genre-badge.component';

/**
 * TODO maybe this module can be in /ui
 */
@NgModule({
  imports: [RouterModule],
  declarations: [GenreBadgeComponent],
  exports: [GenreBadgeComponent],
})
export class BadgesModule {}
