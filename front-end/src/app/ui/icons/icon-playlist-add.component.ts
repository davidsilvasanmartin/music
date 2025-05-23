import { Component } from '@angular/core';

@Component({
  selector: 'app-icon-playlist-add',
  template: `
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 -960 960 960"
      fill="currentColor"
      stroke-width="1.5"
      stroke="currentColor"
    >
      <path
        d="M120-320v-80h280v80H120Zm0-160v-80h440v80H120Zm0-160v-80h440v80H120Zm520 480v-160H480v-80h160v-160h80v160h160v80H720v160h-80Z"
      />
    </svg>
  `,
  styles: ':host { display: flex; }',
})
export class IconPlaylistAddComponent {}
