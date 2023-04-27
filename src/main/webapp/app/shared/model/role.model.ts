import { IPermission } from 'app/shared/model/permission.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';

export interface IRole {
  id?: number;
  roleName?: string;
  isdefault?: boolean | null;
  permissions?: IPermission[] | null;
  users?: IApplicationUser[] | null;
}

export const defaultValue: Readonly<IRole> = {
  isdefault: false,
};
