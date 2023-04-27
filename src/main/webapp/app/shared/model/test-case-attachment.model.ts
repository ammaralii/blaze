import { ITestCase } from 'app/shared/model/test-case.model';

export interface ITestCaseAttachment {
  id?: number;
  url?: string;
  testCase?: ITestCase;
}

export const defaultValue: Readonly<ITestCaseAttachment> = {};
