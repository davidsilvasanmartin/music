import { Component } from '@angular/core';

@Component({
  selector: 'app-card',
  template: `
    <div class="rounded-md bg-white p-2 shadow-md">
      <ng-content></ng-content>
    </div>
  `,
  styles: ':host {display: contents;}',
})
export class CardComponent {}
