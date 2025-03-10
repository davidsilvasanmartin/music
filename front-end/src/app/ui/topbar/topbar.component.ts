import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconsModule } from '../icons/icons.module';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [IconsModule, RouterModule],
  template: `<nav
    class="flex h-16 w-full flex-row flex-nowrap items-center justify-between bg-white px-4 shadow-md"
  >
    <div class="flex items-center gap-3">
      <div
        class="flex h-8 w-8 items-center justify-center rounded bg-emerald-600 font-bold text-white"
      >
        M
      </div>
      <div class="text-lg font-bold text-emerald-600">Music App</div>
    </div>
    <div
      class="hidden min-w-0 max-w-[40%] grow items-center rounded-full bg-slate-50 px-3 py-2 transition-all duration-200 ease-in-out focus-within:ring-2 focus-within:ring-emerald-500 hover:bg-white hover:shadow-sm md:flex"
    >
      <div class="mr-2 text-emerald-500">
        <app-icon-search class="size-4" />
      </div>
      <!-- TODO implement global search -->
      <input
        class="w-full bg-transparent text-gray-700 placeholder-gray-400 focus:outline-none"
        type="text"
        placeholder="Search for artists, albums, songs..."
      />
    </div>
    <div class="">
      <!-- TODO inside this div I would like to put user-specific things -->
      <a
        [routerLink]="'/'"
        class="rounded-md px-4 py-2 font-medium text-emerald-600 transition-colors hover:bg-emerald-50"
        >Albums</a
      >
    </div>
  </nav>`,
  styles: `
    :host {
      display: contents;
    }

    input::placeholder {
      opacity: 0.6;
      transition: opacity 0.2s ease;
    }

    input:focus::placeholder {
      opacity: 0.4;
    }
  `,
})
export class TopbarComponent {}
