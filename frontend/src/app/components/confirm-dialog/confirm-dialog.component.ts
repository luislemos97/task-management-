import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

export type ConfirmDialogData = {
  title?: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  tone?: 'danger' | 'default';
  icon?: string; 
};

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent {
  private ref = inject(MatDialogRef<ConfirmDialogComponent>);
  data = inject<ConfirmDialogData>(MAT_DIALOG_DATA);

  cancel() { this.ref.close(false); }
  confirm() { this.ref.close(true); }

  get title() { return this.data.title ?? 'Confirmar ação'; }
  get confirmText() { return this.data.confirmText ?? 'Confirmar'; }
  get cancelText() { return this.data.cancelText ?? 'Cancelar'; }
  get icon() { return this.data.icon ?? 'help'; }
  get isDanger() { return (this.data.tone ?? 'default') === 'danger'; }
}
