import { Component, computed, input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
})
export class PaginatorComponent {
  totalElements = input.required<number | null>();
  page = input.required<number>();
  size = input.required<number>();
  totalPages = computed(() => {
    const safeTotalElements = this.totalElements() || 0;
    const safeSize = this.size() || 10;
    return Math.ceil(safeTotalElements / safeSize);
  });

  constructor(
    private readonly _router: Router,
    private readonly _activatedRoute: ActivatedRoute,
  ) {}

  onPageChanged(page: number | string) {
    this._router.navigate([], {
      relativeTo: this._activatedRoute,
      queryParams: {
        page: Math.max(1, Math.min(Number(page), this.totalPages())),
      },
      queryParamsHandling: 'merge',
    });
  }

  goToPreviousPage() {
    this.onPageChanged(Math.max(1, this.page() - 1));
  }

  goToNextPage() {
    this.onPageChanged(Math.min(this.totalPages(), this.page() + 1));
  }

  goToFirstPage() {
    this.onPageChanged(1);
  }

  goToLastPage() {
    this.onPageChanged(this.totalPages());
  }

  onSizeChanged(size: number) {
    this._router.navigate([], {
      relativeTo: this._activatedRoute,
      queryParams: { page: 1, size },
      queryParamsHandling: 'merge',
    });
  }
}
