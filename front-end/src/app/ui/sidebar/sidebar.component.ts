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
  template: `<aside class="h-full w-full bg-white">
    <ul class="m-0 flex h-full w-full list-none flex-col flex-nowrap">
      <li>
        <a class="nav-link" routerLink="/dashboard" routerLinkActive="active">
          <app-icon-home class="size-6" />
          <span>Dashboard</span>
        </a>
      </li>
      <li>
        <a class="nav-link" routerLink="/albums" routerLinkActive="active">
          <app-icon-vynil class="size-6" />
          <span>Albums</span>
        </a>
      </li>
      <li>
        <a class="nav-link" routerLink="/artists" routerLinkActive="active">
          <app-icon-mic class="size-6" />
          <span>Artists</span>
        </a>
      </li>
      <li>
        <a class="nav-link" routerLink="/genres" routerLinkActive="active">
          <app-icon-eighth-note class="size-6" />
          <span>Genres</span>
        </a>
      </li>
      <li>
        <a class="nav-link" routerLink="/playlists" routerLinkActive="active">
          <app-icon-menu-list class="size-6" />
          <span>Playlists</span>
        </a>
      </li>
    </ul>
  </aside>`,
  styles: `
    .side-nav-header {
      padding: 24px;
      border-bottom: 1px solid var(--border-color);
    }

    .side-nav-title {
      font-size: 18px;
      font-weight: 700;
      color: var(--primary-color);
      margin: 0;
    }

    .nav-link {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 14px 20px;
      color: var(--text-primary);
      text-decoration: none;
      transition: all 0.2s ease;
      border-left: 3px solid transparent;
      font-weight: 500;
    }

    .nav-link.active {
      background-color: rgba(var(--primary-color-rgb), 0.1);
      color: var(--primary-color);
      border-left-color: var(--primary-color);
    }

    .nav-link:hover:not(.active) {
      background-color: var(--hover-color);
      border-left-color: var(--border-color);
    }

    .nav-icon {
      width: 20px;
      height: 20px;
      flex-shrink: 0;
    }

    li {
      margin-bottom: 4px;
    }

    li:last-child {
      margin-bottom: 0;
    }

    @media (max-width: 768px) {
      .nav-link span {
        display: none;
      }

      .side-nav {
        width: 60px;
      }

      .side-nav-header {
        display: none;
      }

      .nav-link {
        justify-content: center;
        padding: 14px 0;
      }

      .nav-icon {
        margin: 0;
      }
    }
  `,
})
export class SidebarComponent {}
