import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { OfficeType } from 'app/shared/model/enumerations/office-type.model';

export interface IOffice {
  id?: number;
  officeType?: OfficeType;
  name?: string;
  createdAt?: string;
  user?: IUser | null;
  children?: IOffice[] | null;
  parent?: IOffice | null;
}

export const defaultValue: Readonly<IOffice> = {};
