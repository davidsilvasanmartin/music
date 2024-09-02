import { Component, input } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";

/**
 * @TODO use this component and replace Material's
 */

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss',
})
export class PaginatorComponent {
  totalElements = input.required<number | null>();
  page = input.required<number | null>();
  size = input.required<number | null>();

  constructor(
    private readonly _router: Router,
    private readonly _activatedRoute: ActivatedRoute,
  ) {}

  onPageChanged(page: number) {
    this._router.navigate([], {
      relativeTo: this._activatedRoute,
      queryParams: { page },
      queryParamsHandling: 'merge',
    });
  }

  onSizeChanged(size: number) {
    this._router.navigate([], {
      relativeTo: this._activatedRoute,
      queryParams: { size },
      queryParamsHandling: 'merge',
    });
  }
}
