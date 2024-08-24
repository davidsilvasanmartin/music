import { Component } from '@angular/core';

@Component({
  selector: 'app-button',
  template: `
    <button class="btn">
      <ng-content></ng-content>
    </button>
  `,
  styles: ':host {display: contents;}',
})
export class ButtonComponent {}
