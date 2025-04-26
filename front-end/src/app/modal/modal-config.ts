import { InjectionToken, Type } from '@angular/core';

export interface ModalConfig<T = undefined> {
  /** The component to open */
  component: Type<any>;

  /** Data to pass to the modal component. */
  data?: T;

  // Add other config options here if needed later (e.g., size, disableClose)
}

export const MODAL_CONFIG = new InjectionToken<ModalConfig>('MODAL_CONFIG');
