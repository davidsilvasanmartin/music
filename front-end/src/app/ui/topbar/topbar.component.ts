import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconsModule } from '../icons/icons.module';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [IconsModule, RouterModule],
  template: `<nav
    class="z-20 flex h-16 w-full flex-row flex-nowrap items-center justify-between bg-white px-4 shadow-sm"
  >
    <div class="flex items-center gap-3">
      <div
        class="flex h-10 w-10 items-center justify-center rounded-lg bg-white p-1.5 text-emerald-600"
      >
        <app-icon-eighth-note class="size-6" />
      </div>
      <div class="text-lg font-medium text-emerald-600">Music App</div>
    </div>
    <div
      class="hidden min-w-0 max-w-[40%] grow items-center rounded-full bg-slate-100 px-4 py-2 transition-all duration-200 ease-in-out focus-within:bg-white focus-within:shadow-md focus-within:ring-2 focus-within:ring-emerald-500 md:flex"
    >
      <div class="mr-3 text-gray-500">
        <app-icon-search class="size-5" />
      </div>
      <input
        class="w-full bg-transparent text-gray-700 placeholder-gray-400 focus:outline-none"
        type="text"
        placeholder="Search for artists, albums, songs..."
      />
    </div>
    <div class="flex items-center">
      <a
        routerLink="/profile"
        class="group flex h-9 w-9 items-center justify-center rounded-full border border-transparent text-gray-700 transition-all hover:bg-emerald-50 hover:text-emerald-600"
        aria-label="User Profile"
        title="Profile"
      >
        <!-- TODO user icon instead of eye -->
        <app-icon-eye class="size-5 transition-colors" aria-hidden="true" />
      </a>
    </div>
  </nav>`,
  styles: `
    :host {
      display: contents;
    }
  `,
})
export class TopbarComponent {}
