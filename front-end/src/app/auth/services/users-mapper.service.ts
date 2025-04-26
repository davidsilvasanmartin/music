import { Injectable } from '@angular/core';

import type { Role, RoleDto, User, UserDto } from '../models';

@Injectable({ providedIn: 'root' })
export class UsersMapperService {
  fromDto(userDto: UserDto): User {
    return {
      username: userDto.username,
      email: userDto.email,
      isEnabled: userDto.isEnabled,
      createdAt: userDto.createdAt,
      updatedAt: userDto.updatedAt,
      roles: userDto.roles?.map((r) => this.fromRoleDto(r)) || [],
    };
  }

  fromRoleDto(roleDto: RoleDto): Role {
    return {
      name: roleDto.name,
    };
  }
}
