import { Component } from '@angular/core';

@Component({
  selector: 'app-icon-queue-add',
  template: `
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 -960 960 960"
      fill="currentColor"
      stroke-width="1.5"
      stroke="currentColor"
    >
      <path
        d="M120-320v-80h320v80H120Zm0-160v-80h480v80H120Zm0-160v-80h480v80H120Zm520 520v-320l240 160-240 160Z"
      />
    </svg>
  `,
  styles: ':host { display: flex; }',
})
export class IconQueueAddComponent {}
