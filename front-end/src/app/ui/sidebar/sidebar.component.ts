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
  // Width is calculated so that the icons look more or less centered when the sidebar is collapsed
  template: `<nav
    class="z-10 h-full w-[60px] shrink-0 overflow-y-auto bg-white shadow-md lg:w-40"
    aria-label="Main navigation"
  >
    <ul class="m-0 flex h-full w-full flex-col p-0" role="menubar">
      <li role="none" class="mt-2">
        <a
          class="slate"
          routerLink="/dashboard"
          routerLinkActive="border-slate-400"
          [routerLinkActiveOptions]="{ exact: true }"
          title="Dashboard"
          role="menuitem"
          aria-label="Dashboard"
        >
          <app-icon-home class="size-6" aria-hidden="true" />
          <span class="ml-3 hidden text-sm font-medium lg:block"
            >Dashboard</span
          >
        </a>
      </li>
      <li role="none">
        <a
          class="emerald"
          routerLink="/albums"
          routerLinkActive="active-link"
          title="Albums"
          role="menuitem"
          aria-label="Albums"
        >
          <app-icon-vynil class="size-6" aria-hidden="true" />
          <span class="ml-3 hidden text-sm font-medium lg:block">Albums</span>
        </a>
      </li>
      <li role="none">
        <a
          class="blue"
          routerLink="/artists"
          routerLinkActive="active-link"
          title="Artists"
          role="menuitem"
          aria-label="Artists"
        >
          <app-icon-mic class="size-6" aria-hidden="true" />
          <span class="ml-3 hidden text-sm font-medium lg:block">Artists</span>
        </a>
      </li>
      <li role="none">
        <a
          class="amber"
          routerLink="/genres"
          routerLinkActive="active-link"
          title="Genres"
          role="menuitem"
          aria-label="Genres"
        >
          <app-icon-eighth-note class="size-6" aria-hidden="true" />
          <span class="ml-3 hidden text-sm font-medium lg:block">Genres</span>
        </a>
      </li>
      <li role="none">
        <a
          class="red"
          routerLink="/playlists"
          routerLinkActive="active-link"
          title="Playlists"
          role="menuitem"
          aria-label="Playlists"
        >
          <app-icon-playlist-add class="size-6" aria-hidden="true" />
          <span class="ml-3 hidden text-sm font-medium lg:block"
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

    li a {
      @apply flex cursor-pointer items-center border-l-8 border-transparent px-3 py-3 text-slate-700;

      &.emerald {
        @apply hover:bg-emerald-50 hover:text-emerald-700;
        &.active-link {
          @apply border-emerald-700 bg-emerald-50 text-emerald-700;
        }
      }

      &.blue {
        @apply hover:bg-blue-50 hover:text-blue-600;
        &.active-link {
          @apply border-blue-600 bg-blue-50 text-blue-600;
        }
      }

      &.amber {
        @apply hover:bg-amber-50 hover:text-amber-600;
        &.active-link {
          @apply border-amber-600 bg-amber-50 text-amber-600;
        }
      }

      &.red {
        @apply hover:bg-red-50 hover:text-red-600;
        &.active-link {
          @apply border-red-600 bg-red-50 text-red-600;
        }
      }

      &.slate {
        @apply hover:bg-slate-50 hover:text-slate-500;
        &.active-link {
          @apply border-slate-500 bg-slate-50 text-slate-500;
        }
      }
    }
  `,
})
export class SidebarComponent {}
