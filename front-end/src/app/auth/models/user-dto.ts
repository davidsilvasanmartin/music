import type { RoleDto } from './role-dto';

export interface UserDto {
  username: string;
  email: string;
  isEnabled: boolean;
  createdAt: string;
  updatedAt: string;
  roles: RoleDto[];
}
