import { ITestRun } from 'app/shared/model/test-run.model';
import { ITestCase } from 'app/shared/model/test-case.model';
import { ITestStatus } from 'app/shared/model/test-status.model';
import { ITestRunDetailAttachment } from 'app/shared/model/test-run-detail-attachment.model';
import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';

export interface ITestRunDetails {
  id?: number;
  resultDetail?: string | null;
  jiraId?: string | null;
  createdBy?: number | null;
  executedBy?: number | null;
  testRun?: ITestRun | null;
  testCase?: ITestCase | null;
  status?: ITestStatus | null;
  testrundetailattachmentTestrundetails?: ITestRunDetailAttachment[] | null;
  testrunstepdetailsTestrundetails?: ITestRunStepDetails[] | null;
}

export const defaultValue: Readonly<ITestRunDetails> = {};
