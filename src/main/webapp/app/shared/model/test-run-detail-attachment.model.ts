import { ITestRunDetails } from 'app/shared/model/test-run-details.model';

export interface ITestRunDetailAttachment {
  id?: number;
  url?: string;
  testRunDetail?: ITestRunDetails;
}

export const defaultValue: Readonly<ITestRunDetailAttachment> = {};
