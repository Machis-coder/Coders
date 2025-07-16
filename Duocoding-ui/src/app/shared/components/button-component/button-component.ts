
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';


export enum ButtonType {
  PRIMARY,
  SECONDARY
}

export enum ButtonSize {
  LARGE = 'LARGE',
  MEDIUM = 'MEDIUM',
  SMALL = 'SMALL'
}
@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './button-component.html',
  styleUrls: ['./button-component.css']
})

export class ButtonComponent implements OnInit {
  @Input() text!: string;
  @Input() clazz?: string;
  @Input() type: ButtonType = ButtonType.PRIMARY;
  @Input() size: ButtonSize = ButtonSize.LARGE;

  @Output() buttonClick = new EventEmitter<MouseEvent>();
  @Output('click') click = this.buttonClick;

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

  onClick(event: MouseEvent) {
    event.stopPropagation();
    this.buttonClick.emit(event);
  }
}