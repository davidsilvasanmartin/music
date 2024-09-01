import { Component, input, output } from "@angular/core";

import { PaginationParams } from "./pagination-params";

/**
 * @TODO use this component and replace Material's
 */

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss',
})
export class PaginatorComponent {
  totalElements = input.required<number>();
  page = input.required<number>();
  size = input.required<number>();
  paginationChanged = output<Pick<PaginationParams, 'page' | 'size'>>();
}
