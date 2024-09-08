import { Component } from '@angular/core';

@Component({
  selector: 'app-icon-play-next',
  template: ` <svg
    viewBox="0 0 24 24"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    stroke-width="1.5"
    stroke="currentColor"
  >
    <path
      d="M19 6V18M5 18L5 6L15 12L5 18Z"
      stroke-linecap="round"
      stroke-linejoin="round"
    />
  </svg>`,
  styles: ':host { display:flex; }',
})
export class IconPlayNextComponent {}
