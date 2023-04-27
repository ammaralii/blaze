import { ICompany } from 'app/shared/model/company.model';
import { ITemplateFieldType } from 'app/shared/model/template-field-type.model';
import { ITestCaseField } from 'app/shared/model/test-case-field.model';
import { ITemplate } from 'app/shared/model/template.model';

export interface ITemplateField {
  id?: number;
  fieldName?: string | null;
  company?: ICompany;
  templateFieldType?: ITemplateFieldType;
  testcasefieldTemplatefields?: ITestCaseField[] | null;
  templates?: ITemplate[] | null;
}

export const defaultValue: Readonly<ITemplateField> = {};
