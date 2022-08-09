import { IUmurimo } from 'app/shared/model/umurimo.model';
import { IOffice } from 'app/shared/model/office.model';

export interface IUmuyobozi {
  id?: number;
  firstName?: string;
  lastName?: string;
  phoneOne?: string;
  phoneTwo?: string;
  email?: string;
  umurimo?: IUmurimo | null;
  office?: IOffice | null;
}

export const defaultValue: Readonly<IUmuyobozi> = {};
