import { OfficeType } from 'app/shared/model/enumerations/office-type.model';

export interface IUmurimo {
  id?: number;
  umurimo?: string;
  officeType?: OfficeType | null;
}

export const defaultValue: Readonly<IUmurimo> = {};
