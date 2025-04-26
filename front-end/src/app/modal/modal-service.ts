import {
  ApplicationRef,
  createComponent,
  createEnvironmentInjector,
  EmbeddedViewRef,
  EnvironmentInjector,
  Injectable,
  Renderer2,
  RendererFactory2,
} from '@angular/core';

import { MODAL_CONFIG, ModalConfig } from './modal-config';
import { ModalWrapperComponent } from './modal-wrapper.component';

@Injectable({ providedIn: 'root' })
export class ModalService {
  private renderer: Renderer2;

  constructor(
    private readonly _envInjector: EnvironmentInjector,
    private readonly _appRef: ApplicationRef,
    rendererFactory: RendererFactory2,
  ) {
    this.renderer = rendererFactory.createRenderer(null, null);
  }

  open<T = any>(config: ModalConfig<T>) {
    const wrapperInjector = createEnvironmentInjector(
      [{ provide: MODAL_CONFIG, useValue: config }],
      this._envInjector,
    );

    const wrapperRef = createComponent(ModalWrapperComponent, {
      environmentInjector: wrapperInjector,
    });

    // Attach the wrapper component's view to the Angular application's view tree
    this._appRef.attachView(wrapperRef.hostView);

    // Get the DOM element for the wrapper component
    const domElem = (wrapperRef.hostView as EmbeddedViewRef<any>)
      .rootNodes[0] as HTMLElement;

    // Append the DOM element to the body (or a specific modal container)
    this.renderer.appendChild(document.body, domElem);
  }
}
