import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';

export interface ITestRunStepDetailAttachment {
  id?: number;
  url?: string;
  testRunStepDetail?: ITestRunStepDetails;
}

export const defaultValue: Readonly<ITestRunStepDetailAttachment> = {};
