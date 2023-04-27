import dayjs from 'dayjs';
import { ITestSuite } from 'app/shared/model/test-suite.model';
import { ISection } from 'app/shared/model/section.model';
import { ITestCasePriority } from 'app/shared/model/test-case-priority.model';
import { ITemplate } from 'app/shared/model/template.model';
import { IMilestone } from 'app/shared/model/milestone.model';
import { ITestLevel } from 'app/shared/model/test-level.model';
import { ITestCaseAttachment } from 'app/shared/model/test-case-attachment.model';
import { ITestCaseField } from 'app/shared/model/test-case-field.model';
import { ITestRunDetails } from 'app/shared/model/test-run-details.model';

export interface ITestCase {
  id?: number;
  title?: string | null;
  estimate?: string | null;
  createdBy?: number | null;
  createdAt?: string | null;
  updatedBy?: number | null;
  updatedAt?: string | null;
  precondition?: string | null;
  description?: string | null;
  isAutomated?: boolean | null;
  testSuite?: ITestSuite | null;
  section?: ISection | null;
  priority?: ITestCasePriority;
  template?: ITemplate | null;
  milestone?: IMilestone | null;
  testLevels?: ITestLevel[] | null;
  testcaseattachmentTestcases?: ITestCaseAttachment[] | null;
  testcasefieldTestcases?: ITestCaseField[] | null;
  testrundetailsTestcases?: ITestRunDetails[] | null;
}

export const defaultValue: Readonly<ITestCase> = {
  isAutomated: false,
};
