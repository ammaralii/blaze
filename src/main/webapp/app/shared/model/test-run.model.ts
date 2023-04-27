import dayjs from 'dayjs';
import { ITestLevel } from 'app/shared/model/test-level.model';
import { IMilestone } from 'app/shared/model/milestone.model';
import { ITestRunDetails } from 'app/shared/model/test-run-details.model';

export interface ITestRun {
  id?: number;
  name?: string | null;
  description?: string | null;
  createdAt?: string | null;
  createdBy?: number | null;
  testLevel?: ITestLevel | null;
  mileStone?: IMilestone | null;
  testrundetailsTestruns?: ITestRunDetails[] | null;
}

export const defaultValue: Readonly<ITestRun> = {};
