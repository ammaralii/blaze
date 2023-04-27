import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';
import { ITemplateField } from 'app/shared/model/template-field.model';
import { IProject } from 'app/shared/model/project.model';
import { ITestCase } from 'app/shared/model/test-case.model';

export interface ITemplate {
  id?: number;
  templateName?: string | null;
  createdAt?: string | null;
  createdBy?: number | null;
  company?: ICompany;
  templateFields?: ITemplateField[] | null;
  projectDefaulttemplates?: IProject[] | null;
  testcaseTemplates?: ITestCase[] | null;
}

export const defaultValue: Readonly<ITemplate> = {};
