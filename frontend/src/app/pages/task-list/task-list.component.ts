import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';

import { Task, TaskRequest, TaskStatus } from '../../models/task';
import { TaskService } from '../../services/task.service';
import { TaskDialogComponent } from '../../components/task-dialog/task-dialog.component';
import { AuthService } from '../../services/auth.service';
import { ConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatSelectModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatCardModule,
    MatMenuModule
  ],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
})
export class TaskListComponent {
  private taskService = inject(TaskService);
  private dialog = inject(MatDialog);
  private auth = inject(AuthService);

  readonly statuses: TaskStatus[] = ['TODO', 'DOING', 'DONE'];
  readonly cols = ['title', 'status', 'dueDate', 'actions'];

  tasks: Task[] = [];
  totalElements = 0;
  pageIndex = 0;
  pageSize = 10;
  filterStatus: TaskStatus | null = null;

  savingIds = new Set<string>();
  loading = false;

  constructor() {
    this.load();
  }

  logout() {
    this.auth.logout();
  }

  load() {
    this.loading = true;
    this.taskService.list(this.pageIndex, this.pageSize, this.filterStatus ?? undefined).subscribe({
      next: (page) => {
        this.tasks = page.content;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  onPage(ev: PageEvent) {
    this.pageIndex = ev.pageIndex;
    this.pageSize = ev.pageSize;
    this.load();
  }

  onFilter(status: TaskStatus | null) {
    this.filterStatus = status;
    this.pageIndex = 0;
    this.load();
  }

  openCreate() {
    const ref = this.dialog.open(TaskDialogComponent, {
      width: '560px',
      data: { mode: 'create' },
    });

    ref.afterClosed().subscribe((payload?: TaskRequest) => {
      if (!payload) return;
      this.taskService.create(payload).subscribe(() => this.load());
    });
  }

  openEdit(t: Task) {
    const initial: TaskRequest = {
      title: t.title,
      description: t.description,
      status: t.status,
      dueDate: t.dueDate ?? null,
    };

    const ref = this.dialog.open(TaskDialogComponent, {
      width: '560px',
      data: { mode: 'edit', initial },
    });

    ref.afterClosed().subscribe((payload?: TaskRequest) => {
      if (!payload) return;

      this.savingIds.add(t.id);
      this.taskService.update(t.id, payload).subscribe({
        next: () => {
          this.savingIds.delete(t.id);
          this.load();
        },
        error: () => {
          this.savingIds.delete(t.id);
        },
      });
    });
  }

  changeStatus(t: Task, status: TaskStatus) {
    const payload: TaskRequest = {
      title: t.title,
      description: t.description,
      status,
      dueDate: t.dueDate ?? null,
    };

    this.savingIds.add(t.id);
    this.taskService.update(t.id, payload).subscribe({
      next: () => {
        this.savingIds.delete(t.id);
        t.status = status;
      },
      error: () => {
        this.savingIds.delete(t.id);
      },
    });
  }

  remove(t: Task) {
    const ref = this.dialog.open(ConfirmDialogComponent, {
      width: '460px',
      data: {
        title: 'Excluir tarefa',
        message: `Tem certeza que deseja excluir "${t.title}"? Essa ação não pode ser desfeita.`,
        confirmText: 'Excluir',
        cancelText: 'Cancelar',
        tone: 'danger',
        icon: 'delete',
      },
    });

    ref.afterClosed().subscribe((ok: boolean) => {
      if (!ok) return;

      this.savingIds.add(t.id);
      this.taskService.delete(t.id).subscribe({
        next: () => { this.savingIds.delete(t.id); this.load(); },
        error: () => { this.savingIds.delete(t.id); },
      });
    });
  }
}
