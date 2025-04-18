import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconsModule } from '../../ui/icons/icons.module';
import { GenreBadgeComponent } from './genre-badge.component';
import { YearBadgeComponent } from './year-badge.component';

/**
 * TODO maybe this module can be in /ui
 */
@NgModule({
  imports: [RouterModule, IconsModule],
  declarations: [GenreBadgeComponent, YearBadgeComponent],
  exports: [GenreBadgeComponent, YearBadgeComponent],
})
export class BadgesModule {}
