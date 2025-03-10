import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [],
  template: `<aside class="side-nav">
    <div class="side-nav-title">Main Menu</div>
    <ul class="nav-links">
      <li>
        <a class="nav-link active" href="#">
          <i class="fas fa-home"></i>
          <span>Dashboard</span>
        </a>
      </li>
      <li>
        <a class="nav-link" href="#">
          <i class="fas fa-compact-disc"></i>
          <span>Albums</span>
        </a>
      </li>
      <li>
        <a class="nav-link" href="#">
          <i class="fas fa-microphone"></i>
          <span>Artists</span>
        </a>
      </li>
      <li>
        <a class="nav-link" href="#">
          <i class="fas fa-guitar"></i>
          <span>Genres</span>
        </a>
      </li>
      <li>
        <a class="nav-link" href="#">
          <i class="fas fa-list"></i>
          <span>Playlists</span>
        </a>
      </li>
    </ul>
  </aside>`,
  styles: `
    .side-nav {
      position: fixed;
      top: var(--nav-height);
      left: 0;
      width: var(--side-nav-width);
      height: calc(100vh - var(--nav-height));
      background-color: var(--background-card);
      border-right: 1px solid var(--border-color);
      padding: 24px 0;
      overflow-y: auto;
    }

    .side-nav-title {
      padding: 0 24px;
      margin-bottom: 16px;
      font-size: 12px;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 1px;
      color: var(--text-secondary);
    }

    .nav-links {
      list-style: none;
    }

    .nav-link {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 24px;
      color: var(--text-primary);
      text-decoration: none;
      transition: all 0.2s;
      border-left: 3px solid transparent;
    }

    .nav-link.active {
      background-color: var(--hover-color);
      color: var(--primary-color);
      border-left-color: var(--primary-color);
    }

    .nav-link:hover {
      background-color: var(--hover-color);
    }

    .nav-link i {
      width: 20px;
      text-align: center;
    }
  `,
})
export class SidebarComponent {}
