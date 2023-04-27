import dayjs from 'dayjs';
import { IProject } from 'app/shared/model/project.model';
import { ITestCase } from 'app/shared/model/test-case.model';
import { ITestRun } from 'app/shared/model/test-run.model';

export interface IMilestone {
  id?: number;
  name?: string | null;
  description?: string | null;
  reference?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  isCompleted?: boolean | null;
  parentMilestone?: IMilestone | null;
  project?: IProject | null;
  milestoneParentmilestones?: IMilestone[] | null;
  testcaseMilestones?: ITestCase[] | null;
  testrunMilestones?: ITestRun[] | null;
}

export const defaultValue: Readonly<IMilestone> = {
  isCompleted: false,
};
