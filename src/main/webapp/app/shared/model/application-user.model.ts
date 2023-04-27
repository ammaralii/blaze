import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';
import { IProject } from 'app/shared/model/project.model';
import { IRole } from 'app/shared/model/role.model';

export interface IApplicationUser {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  password?: string | null;
  lastActive?: string | null;
  status?: string | null;
  createdBy?: number | null;
  createdAt?: string | null;
  updatedBy?: number | null;
  updatedAt?: string | null;
  userEmail?: string | null;
  isDeleted?: boolean;
  company?: ICompany | null;
  projects?: IProject[] | null;
  roles?: IRole[] | null;
}

export const defaultValue: Readonly<IApplicationUser> = {
  isDeleted: false,
};
