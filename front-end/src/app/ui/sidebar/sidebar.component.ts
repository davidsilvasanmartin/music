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
  template: `<aside
    class="duration-400 m-0 h-full w-16 shrink-0 bg-white transition-all lg:w-64"
  >
    <ul
      class="z-10 m-0 flex h-full w-full list-none flex-col flex-nowrap overflow-y-auto"
    >
      <li>
        <a
          class="flex flex-row flex-nowrap"
          routerLink="/dashboard"
          routerLinkActive="active"
        >
          <app-icon-home class="size-6" />
          <span class="hidden lg:inline-block">Dashboard</span>
        </a>
      </li>
      <li>
        <a
          class="flex flex-row flex-nowrap"
          routerLink="/albums"
          routerLinkActive="active"
        >
          <app-icon-vynil class="size-6" />
          <span class="hidden lg:inline-block">Albums</span>
        </a>
      </li>
      <li>
        <a
          class="flex flex-row flex-nowrap"
          routerLink="/artists"
          routerLinkActive="active"
        >
          <app-icon-mic class="size-6" />
          <span class="hidden lg:inline-block">Artists</span>
        </a>
      </li>
      <li>
        <a
          class="flex flex-row flex-nowrap"
          routerLink="/genres"
          routerLinkActive="active"
        >
          <app-icon-eighth-note class="size-6" />
          <span class="hidden lg:inline-block">Genres</span>
        </a>
      </li>
      <li>
        <a
          class="flex flex-row flex-nowrap"
          routerLink="/playlists"
          routerLinkActive="active"
        >
          <app-icon-menu-list class="size-6" />
          <span class="hidden lg:inline-block">Playlists</span>
        </a>
      </li>
    </ul>
  </aside>`,
  styles: `
    :host {
      display: contents;
    }
  `,
})
export class SidebarComponent {}
