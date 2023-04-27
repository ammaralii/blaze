import dayjs from 'dayjs';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IProject } from 'app/shared/model/project.model';
import { ITemplate } from 'app/shared/model/template.model';
import { ITemplateField } from 'app/shared/model/template-field.model';

export interface ICompany {
  id?: number;
  country?: string | null;
  companyAddress?: string | null;
  organization?: string | null;
  expectedNoOfUsers?: number | null;
  createdAt?: string | null;
  applicationuserCompanies?: IApplicationUser[] | null;
  projectCompanies?: IProject[] | null;
  templateCompanies?: ITemplate[] | null;
  templatefieldCompanies?: ITemplateField[] | null;
}

export const defaultValue: Readonly<ICompany> = {};
