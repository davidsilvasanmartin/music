import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconsModule } from '../icons/icons.module';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [IconsModule, RouterModule],
  template: `<nav
    class="z-20 flex h-16 w-full flex-row flex-nowrap items-center justify-between bg-white px-4 shadow-lg"
  >
    <div class="flex items-center gap-3">
      <div
        class="flex h-10 w-10 items-center justify-center rounded-lg bg-emerald-600 p-1.5 text-white shadow-md"
      >
        <app-icon-eighth-note class="size-6" />
      </div>
      <div class="text-lg font-medium text-emerald-700">Music App</div>
    </div>
    <div
      class="hidden min-w-0 max-w-[40%] grow items-center rounded-full bg-slate-100 px-4 py-2 transition-all duration-200 ease-in-out focus-within:bg-white focus-within:shadow-md focus-within:ring-2 focus-within:ring-emerald-500 md:flex"
    >
      <div class="mr-3 text-slate-500">
        <app-icon-search class="size-5" />
      </div>
      <input
        class="w-full bg-transparent text-slate-700 placeholder-slate-400 focus:outline-none"
        type="text"
        placeholder="Search for artists, albums, songs..."
      />
    </div>
    <div class="flex items-center">
      <a
        routerLink="/profile"
        class="flex h-9 w-9 items-center justify-center rounded-full text-slate-600 hover:text-slate-400"
        aria-label="User Profile"
        title="Profile"
      >
        <!-- TODO user icon instead of eye -->
        <app-icon-user class="size-6" aria-hidden="true" />
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
