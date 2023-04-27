import { ITestRun } from 'app/shared/model/test-run.model';
import { ITestCase } from 'app/shared/model/test-case.model';

export interface ITestLevel {
  id?: number;
  name?: string | null;
  testrunTestlevels?: ITestRun[] | null;
  testCases?: ITestCase[] | null;
}

export const defaultValue: Readonly<ITestLevel> = {};
