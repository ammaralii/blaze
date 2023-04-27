import dayjs from 'dayjs';
import { ITemplate } from 'app/shared/model/template.model';
import { ICompany } from 'app/shared/model/company.model';
import { IMilestone } from 'app/shared/model/milestone.model';
import { ITestPlan } from 'app/shared/model/test-plan.model';
import { ITestSuite } from 'app/shared/model/test-suite.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';

export interface IProject {
  id?: number;
  projectName?: string;
  description?: string | null;
  isactive?: boolean | null;
  createdBy?: number | null;
  createdAt?: string | null;
  updatedBy?: number | null;
  updatedAt?: string | null;
  defaultTemplate?: ITemplate | null;
  company?: ICompany | null;
  milestoneProjects?: IMilestone[] | null;
  testplanProjects?: ITestPlan[] | null;
  testsuiteProjects?: ITestSuite[] | null;
  users?: IApplicationUser[] | null;
}

export const defaultValue: Readonly<IProject> = {
  isactive: false,
};
