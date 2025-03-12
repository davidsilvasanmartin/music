import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import { UiModule } from '../ui.module';

// TODO is it better to replace ul by <nav> for accessibility ??
// TODO better indication of active link (thicker left bar + background color)
// TODO need to change the accent color to emerald or whichever I choose for the app

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, UiModule],
  template: `<nav
    class="duration-400 h-full w-14 shrink-0 bg-white shadow-sm transition-all lg:w-64"
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

    .active-link app-icon-home,
    .active-link app-icon-vynil,
    .active-link app-icon-mic,
    .active-link app-icon-eighth-note,
    .active-link app-icon-menu-list {
      @apply text-emerald-600;
    }
  `,
})
export class SidebarComponent {}
