export type TaskStatus = 'TODO' | 'DOING' | 'DONE';

export interface Task {
  id: string;
  title: string;
  description?: string;
  status: TaskStatus;
  dueDate?: string | null;
  createdAt: string;
}

export interface TaskRequest {
  title: string;
  description?: string;
  status: TaskStatus;
  dueDate?: string | null;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
