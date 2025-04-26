import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Inject,
  Injector,
  OnInit,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';

import { IconsModule } from '../icons/icons.module';
import { MODAL_CONFIG, ModalConfig } from './modal-config';
import { MODAL_DATA } from './modal-data';

@Component({
  selector: 'app-modal-wrapper',
  template: `<dialog
    #dialog
    class="flex flex-col flex-nowrap rounded-md shadow-lg backdrop:bg-black/50"
  >
    <div class="flex flex-row flex-nowrap justify-end">
      <button
        (click)="closeDialog()"
        aria-label="Close modal"
        class="btn rounded-full p-1 font-bold text-slate-600 hover:text-slate-400"
        title="Close modal"
      >
        <app-icon-cancel class="size-5" />
      </button>
    </div>
    <ng-container #contentContainer></ng-container>
  </dialog>`,
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [IconsModule],
})
export class ModalWrapperComponent implements OnInit, AfterViewInit {
  @ViewChild('dialog', { static: true })
  dialogRef!: ElementRef<HTMLDialogElement>;

  @ViewChild('contentContainer', { read: ViewContainerRef, static: true })
  contentContainer!: ViewContainerRef;

  constructor(
    private readonly _injector: Injector,
    private readonly _cdRef: ChangeDetectorRef,
    private readonly _elementRef: ElementRef,
    @Inject(MODAL_CONFIG)
    private readonly _modalConfig: ModalConfig<any>,
  ) {}

  ngOnInit(): void {
    this.loadContentComponent();
  }

  ngAfterViewInit(): void {
    this.dialogRef.nativeElement.showModal();
    this.dialogRef.nativeElement.addEventListener('close', () => {
      this._elementRef.nativeElement.remove();
    });
  }

  closeDialog(): void {
    if (this.dialogRef.nativeElement.open) {
      this.dialogRef.nativeElement.close();
    }
  }

  private loadContentComponent(): void {
    this.contentContainer.clear();

    const contentInjector = Injector.create({
      providers: [{ provide: MODAL_DATA, useValue: this._modalConfig.data }],
      parent: this._injector,
    });

    this.contentContainer.createComponent(this._modalConfig.component, {
      injector: contentInjector,
    });

    this._cdRef.markForCheck();
  }
}
