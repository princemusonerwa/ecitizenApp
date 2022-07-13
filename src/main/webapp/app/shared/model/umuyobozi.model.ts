import { IUmurimo } from 'app/shared/model/umurimo.model';

export interface IUmuyobozi {
  id?: number;
  firstName?: string;
  lastName?: string;
  phoneOne?: string;
  phoneTwo?: string;
  email?: string;
  umurimo?: IUmurimo | null;
}

export const defaultValue: Readonly<IUmuyobozi> = {};
