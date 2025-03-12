import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import { UiModule } from '../ui.module';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, UiModule],
  // Width of 60px is calculated so that the icons are centered when the sidebar is collapsed
  template: `<nav
    class="duration-400 h-full w-[60px] shrink-0 overflow-y-auto bg-white shadow-sm transition-all lg:w-36"
  >
    <ul class="m-0 flex h-full w-full flex-col p-0">
      <li>
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/dashboard"
          routerLinkActive="active-link"
          [routerLinkActiveOptions]="{ exact: true }"
        >
          <app-icon-home
            class="size-5 transition-colors group-hover:text-emerald-600"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Dashboard</span
          >
        </a>
      </li>
      <li>
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/albums"
          routerLinkActive="active-link"
        >
          <app-icon-vynil
            class="size-5 transition-colors group-hover:text-emerald-600"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Albums</span
          >
        </a>
      </li>
      <li>
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/artists"
          routerLinkActive="active-link"
        >
          <app-icon-mic
            class="size-5 transition-colors group-hover:text-emerald-600"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Artists</span
          >
        </a>
      </li>
      <li>
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/genres"
          routerLinkActive="active-link"
        >
          <app-icon-eighth-note
            class="size-5 transition-colors group-hover:text-emerald-600"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Genres</span
          >
        </a>
      </li>
      <li>
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/playlists"
          routerLinkActive="active-link"
        >
          <app-icon-menu-list
            class="size-5 transition-colors group-hover:text-emerald-600"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Playlists</span
          >
        </a>
      </li>
    </ul>
  </nav>`,
  styles: `
    :host {
      display: contents;
    }

    .active-link {
      @apply border-emerald-500 bg-emerald-50 text-emerald-600;
    }
  `,
})
export class SidebarComponent {}
