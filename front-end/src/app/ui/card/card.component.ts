import { Component } from '@angular/core';

@Component({
  selector: 'app-card',
  template: `
    <div
      class="rounded-lg border border-slate-200 bg-white p-4 shadow-md transition-shadow hover:shadow-lg"
    >
      <ng-content></ng-content>
    </div>
  `,
  styles: ':host {display: contents;}',
})
export class CardComponent {}
