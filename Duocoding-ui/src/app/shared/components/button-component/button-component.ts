
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './button-component.html',
  styleUrls: ['./button-component.css']   // ← plural
})
export class ButtonComponent implements OnInit {
  @Input() text!: string;
  @Input() clazz?: string;
  @Input() type: ButtonType = ButtonType.PRIMARY;
  @Input() size: ButtonSize = ButtonSize.LARGE;

  @Output('click') click = new EventEmitter<MouseEvent>();

  styleClass = '';

  ngOnInit(): void {
    let cls = (this.clazz ?? '') +
        (this.type === ButtonType.PRIMARY
            ? ' button_primary'
            : ' button_secondary');
    if (this.size !== ButtonSize.LARGE) {
      cls += ' ' + (
          this.size === ButtonSize.MEDIUM
              ? 'size_medium'
              : 'size_small'
      );
    }
    this.styleClass = cls;
  }

  // Recibe el MouseEvent nativo y lo re-emite
  onClick(event: MouseEvent) {
    // Si quieres, aquí puedes aplicar event.stopPropagation()
    this.click.emit(event);
  }
}

export enum ButtonType {
  PRIMARY,
  SECONDARY
}

export enum ButtonSize {
  LARGE = 'LARGE',
  MEDIUM = 'MEDIUM',
  SMALL = 'SMALL'
}
