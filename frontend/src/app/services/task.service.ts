import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Page, Task, TaskRequest, TaskStatus } from '../models/task';

@Injectable({ providedIn: 'root' })
export class TaskService {
  constructor(private http: HttpClient) {}

  list(page = 0, size = 10, status?: TaskStatus) {
    let params = new HttpParams().set('page', page).set('size', size);
    if (status) params = params.set('status', status);
    return this.http.get<Page<Task>>('/api/tasks', { params });
  }

  create(req: TaskRequest) {
    return this.http.post<Task>('/api/tasks', req);
  }

  update(id: string, req: TaskRequest) {
    return this.http.put<Task>(`/api/tasks/${id}`, req);
  }

  delete(id: string) {
    return this.http.delete<void>(`/api/tasks/${id}`);
  }
}
