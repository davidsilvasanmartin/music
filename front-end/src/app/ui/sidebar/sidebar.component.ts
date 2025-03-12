import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import { UiModule } from '../ui.module';

/**
 * Possible further improvements:
 *
 * 1. Toggle Sidebar Button: Add a button to manually expand/collapse
 *    the sidebar, giving users control over their workspace layout.
 * 2. State Persistence: Remember the sidebar state (expanded/collapsed)
 *    between page reloads using local storage.
 * 3. Keyboard Navigation: Implement keyboard navigation with arrow
 *    keys between menu items to improve accessibility.
 * 4. Section Grouping: Group related menu items into
 *    sections with headings if the navigation grows more complex.
 * 5. Nested Navigation: Support for sub-menu items that expand when
 *    a parent item is clicked.
 * 6. Badge Indicators: Add notification badges for areas that need
 *    attention (e.g., number of unread messages).
 * 7. Dark Mode support/Custom theming. Allow users to select theme.
 */

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, UiModule],
  // Width of 60px is calculated so that the icons are centered when the sidebar is collapsed
  template: `<nav
    class="duration-400 h-full w-[60px] shrink-0 overflow-y-auto bg-white shadow-sm transition-all lg:w-40"
    aria-label="Main navigation"
  >
    <ul class="m-0 flex h-full w-full flex-col p-0" role="menubar">
      <li role="none">
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/dashboard"
          routerLinkActive="active-link"
          [routerLinkActiveOptions]="{ exact: true }"
          title="Dashboard"
          role="menuitem"
          aria-label="Dashboard"
        >
          <app-icon-home
            class="size-5 transition-colors group-hover:text-emerald-600"
            aria-hidden="true"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Dashboard</span
          >
        </a>
      </li>
      <li role="none">
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/albums"
          routerLinkActive="active-link"
          title="Albums"
          role="menuitem"
          aria-label="Albums"
        >
          <app-icon-vynil
            class="size-5 transition-colors group-hover:text-emerald-600"
            aria-hidden="true"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Albums</span
          >
        </a>
      </li>
      <li role="none">
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/artists"
          routerLinkActive="active-link"
          title="Artists"
          role="menuitem"
          aria-label="Artists"
        >
          <app-icon-mic
            class="size-5 transition-colors group-hover:text-emerald-600"
            aria-hidden="true"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Artists</span
          >
        </a>
      </li>
      <li role="none">
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/genres"
          routerLinkActive="active-link"
          title="Genres"
          role="menuitem"
          aria-label="Genres"
        >
          <app-icon-eighth-note
            class="size-5 transition-colors group-hover:text-emerald-600"
            aria-hidden="true"
          />
          <span
            class="ml-3 hidden text-sm font-medium group-hover:text-emerald-600 lg:block"
            >Genres</span
          >
        </a>
      </li>
      <li role="none">
        <a
          class="group flex items-center border-l-8 border-transparent px-3 py-3 text-gray-700 transition-all hover:bg-emerald-50"
          routerLink="/playlists"
          routerLinkActive="active-link"
          title="Playlists"
          role="menuitem"
          aria-label="Playlists"
        >
          <app-icon-menu-list
            class="size-5 transition-colors group-hover:text-emerald-600"
            aria-hidden="true"
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
