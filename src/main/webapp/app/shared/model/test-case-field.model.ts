import { ITemplateField } from 'app/shared/model/template-field.model';
import { ITestCase } from 'app/shared/model/test-case.model';
import { ITestCaseFieldAttachment } from 'app/shared/model/test-case-field-attachment.model';
import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';

export interface ITestCaseField {
  id?: number;
  expectedResult?: string | null;
  value?: string | null;
  templateField?: ITemplateField;
  testCase?: ITestCase;
  testcasefieldattachmentTestcasefields?: ITestCaseFieldAttachment[] | null;
  testrunstepdetailsStepdetails?: ITestRunStepDetails[] | null;
}

export const defaultValue: Readonly<ITestCaseField> = {};
