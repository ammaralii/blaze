import dayjs from 'dayjs';
import { IProject } from 'app/shared/model/project.model';

export interface ITestPlan {
  id?: number;
  name?: string | null;
  description?: string | null;
  createdBy?: number | null;
  createdAt?: string | null;
  project?: IProject | null;
}

export const defaultValue: Readonly<ITestPlan> = {};
