import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { AsyncPipe, NgIf } from '@angular/common';
import { LoadingService } from './services/loading.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatProgressBarModule, AsyncPipe, NgIf],
  template: `
    <mat-progress-bar *ngIf="(loading$ | async)" mode="indeterminate"></mat-progress-bar>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  private loading = inject(LoadingService);
  loading$ = this.loading.loading$;
}
