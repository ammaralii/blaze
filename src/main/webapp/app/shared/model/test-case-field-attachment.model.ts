import { ITestCaseField } from 'app/shared/model/test-case-field.model';

export interface ITestCaseFieldAttachment {
  id?: number;
  url?: string;
  testCaseField?: ITestCaseField;
}

export const defaultValue: Readonly<ITestCaseFieldAttachment> = {};
