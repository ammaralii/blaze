import { ITestCase } from 'app/shared/model/test-case.model';

export interface ITestCasePriority {
  id?: number;
  name?: string;
  testcasePriorities?: ITestCase[] | null;
}

export const defaultValue: Readonly<ITestCasePriority> = {};
