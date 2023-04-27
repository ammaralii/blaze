import dayjs from 'dayjs';
import { ITestSuite } from 'app/shared/model/test-suite.model';
import { ITestCase } from 'app/shared/model/test-case.model';

export interface ISection {
  id?: number;
  name?: string | null;
  description?: string | null;
  createdAt?: string | null;
  createdBy?: number | null;
  updatedAt?: string | null;
  updatedBy?: number | null;
  testSuite?: ITestSuite | null;
  parentSection?: ISection | null;
  sectionParentsections?: ISection[] | null;
  testcaseSections?: ITestCase[] | null;
}

export const defaultValue: Readonly<ISection> = {};
