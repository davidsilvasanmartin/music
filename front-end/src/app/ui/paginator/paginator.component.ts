import { Component, input, output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss',
})
export class PaginatorComponent {
  totalElements = input.required<number>();
  pageIndex = input.required<number>();
  size = input.required<number>();
  page = output<PageEvent>();
}
