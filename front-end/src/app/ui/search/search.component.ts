import { Component, Input, input, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { BehaviorSubject } from 'rxjs';

import type { Search } from './search';
import { SearchMapperService } from './search-mapper.service';

@Component({
  selector: 'app-search',
  template: `
    <div
      class="flex flex-row flex-wrap items-center gap-1.5 italic text-gray-700"
    >
      <span>Search by</span>
      <select
        (ngModelChange)="selectedField.set($event)"
        [ngModel]="selectedField()"
        class="rounded-sm bg-white text-center italic"
      >
        <option [value]="null">-</option>
        @for (field of searchableFields(); track field) {
          <option [value]="field">{{ field }}</option>
        }
      </select>
      <span>containing</span>
      <input
        #valueInput
        (keydown.enter)="onValueChanged(valueInput.value)"
        [ngModel]="selectedValue$ | async"
        [disabled]="!selectedField()"
        class="block w-32 rounded-sm px-1 text-center italic focus:border-blue-500 focus:ring-blue-500"
      />
      @if (selectedField() && (selectedValue$ | async)) {
        <app-icon-cancel
          class="size-4 cursor-pointer"
          (click)="clearSearch()"
        />
      }
    </div>
  `,
})
export class SearchComponent {
  searchableFields = input.required<string[]>();
  selectedValue$ = new BehaviorSubject<string | null>(null);
  selectedField = signal<string | null>(null);

  constructor(
    private readonly _router: Router,
    private readonly _activatedRoute: ActivatedRoute,
    private readonly _searchMapperService: SearchMapperService,
  ) {}

  @Input({ required: true })
  set search(s: Search | null) {
    if (s && s.field && s.value) {
      this.selectedField.set(s.field);
      this.selectedValue$.next(s.value);
    } else {
      this.selectedField.set(null);
      this.selectedValue$.next(null);
    }
  }

  onValueChanged(value: string) {
    if (!this.selectedField() || !value) {
      return;
    }

    this._router.navigate([], {
      relativeTo: this._activatedRoute,
      queryParams: {
        search: this._searchMapperService.toQueryParam({
          field: this.selectedField()!,
          condition: 'contains',
          value,
        }),
        page: 1,
      },
      queryParamsHandling: 'merge',
    });
  }

  clearSearch() {
    this._router.navigate([], {
      relativeTo: this._activatedRoute,
      queryParams: {
        search: null,
        page: 1,
      },
      queryParamsHandling: 'merge',
    });
  }
}
