import { ITemplateField } from 'app/shared/model/template-field.model';

export interface ITemplateFieldType {
  id?: number;
  type?: string;
  isList?: boolean;
  attachments?: boolean;
  templatefieldTemplatefieldtypes?: ITemplateField[] | null;
}

export const defaultValue: Readonly<ITemplateFieldType> = {
  isList: false,
  attachments: false,
};
