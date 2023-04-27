import { ITestRunDetails } from 'app/shared/model/test-run-details.model';
import { ITestCaseField } from 'app/shared/model/test-case-field.model';
import { ITestStatus } from 'app/shared/model/test-status.model';
import { ITestRunStepDetailAttachment } from 'app/shared/model/test-run-step-detail-attachment.model';

export interface ITestRunStepDetails {
  id?: number;
  actualResult?: string | null;
  testRunDetail?: ITestRunDetails | null;
  stepDetail?: ITestCaseField | null;
  status?: ITestStatus | null;
  testrunstepdetailattachmentTestrunstepdetails?: ITestRunStepDetailAttachment[] | null;
}

export const defaultValue: Readonly<ITestRunStepDetails> = {};
