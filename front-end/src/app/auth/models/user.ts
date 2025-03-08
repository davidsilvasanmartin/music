import type { Role } from './role';

export interface User {
  username: string;
  email: string;
  isEnabled: boolean;
  createdAt: string;
  updatedAt: string;
  roles: Role[];
}
