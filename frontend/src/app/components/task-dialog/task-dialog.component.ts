import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { TaskRequest, TaskStatus } from '../../models/task';
import { MatIconModule } from '@angular/material/icon';

export interface TaskDialogData {
  mode: 'create' | 'edit';
  initial?: TaskRequest;
}

@Component({
  selector: 'app-task-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
  ],
  templateUrl: './task-dialog.component.html',
  styleUrls: ['./task-dialog.component.scss'],
})
export class TaskDialogComponent {
  data = inject<TaskDialogData>(MAT_DIALOG_DATA);
  private ref = inject(MatDialogRef<TaskDialogComponent>);
  private fb = inject(FormBuilder);

  readonly statuses: TaskStatus[] = ['TODO', 'DOING', 'DONE'];

  readonly form = this.fb.group({
    title: [
      this.data.initial?.title ?? '',
      [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
    ],
    description: [this.data.initial?.description ?? '', [Validators.maxLength(500)]],
    status: [this.data.initial?.status ?? 'TODO', [Validators.required]],
    dueDate: [this.data.initial?.dueDate ? new Date(this.data.initial.dueDate) : null],
  });

  get isCreate() {
    return this.data.mode === 'create';
  }

  close() {
    this.ref.close();
  }

  save() {
    if (this.form.invalid) return;

    const raw = this.form.getRawValue();
    const payload: TaskRequest = {
      title: raw.title!,
      description: raw.description || undefined,
      status: raw.status!,
      dueDate: raw.dueDate ? new Date(raw.dueDate).toISOString().slice(0, 10) : null,
    };

    this.ref.close(payload);
  }

  blockNonDateKeys(ev: KeyboardEvent) {
    const allowed = ['Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete', '/', '-'];
    if (allowed.includes(ev.key)) return;
    if (/^\d$/.test(ev.key)) return;
    ev.preventDefault();
  }

}
