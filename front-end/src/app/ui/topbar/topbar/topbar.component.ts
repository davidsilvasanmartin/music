import { Component } from '@angular/core';

@Component({
  selector: 'app-topbar',
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
      class="hidden max-w-[40%] items-center rounded bg-slate-100 px-2 py-2 focus-within:border focus-within:border-emerald-600 hover:bg-slate-400 md:flex"
    >
      <!-- TODO search icon goes here -->
      <div class="mr-2 font-bold text-slate-300">I</div>
      <!-- TODO implement global search -->
      <input
        class="bg-transparent focus:outline-none"
        type="text"
        placeholder="Search for artists, albums, songs..."
      />
    </div>
    <div class="">
      <!-- TODO inside this div I would like to put user-specific things -->
      <a [routerLink]="'/'">Albums</a>
    </div>
  </nav>`,
  styles: ':host {display: contents;}',
})
export class TopbarComponent {}
