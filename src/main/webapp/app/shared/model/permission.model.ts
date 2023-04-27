import { IRole } from 'app/shared/model/role.model';

export interface IPermission {
  id?: number;
  permissionName?: string;
  roles?: IRole[] | null;
}

export const defaultValue: Readonly<IPermission> = {};
