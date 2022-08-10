import dayjs from 'dayjs';
import { OfficeType } from 'app/shared/model/enumerations/office-type.model';

export interface IOffice {
  id?: number;
  officeType?: OfficeType;
  name?: string;
  createdAt?: string;
  children?: IOffice[] | null;
  parent?: IOffice | null;
}

export const defaultValue: Readonly<IOffice> = {};
