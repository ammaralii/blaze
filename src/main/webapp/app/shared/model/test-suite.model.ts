import dayjs from 'dayjs';
import { IProject } from 'app/shared/model/project.model';
import { ISection } from 'app/shared/model/section.model';
import { ITestCase } from 'app/shared/model/test-case.model';

export interface ITestSuite {
  id?: number;
  testSuiteName?: string | null;
  description?: string | null;
  createdBy?: number | null;
  createdAt?: string | null;
  updatedBy?: number | null;
  updatedAt?: string | null;
  project?: IProject | null;
  sectionTestsuites?: ISection[] | null;
  testcaseTestsuites?: ITestCase[] | null;
}

export const defaultValue: Readonly<ITestSuite> = {};
