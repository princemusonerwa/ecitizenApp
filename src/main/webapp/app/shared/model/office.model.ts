import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { OfficeType } from 'app/shared/model/enumerations/office-type.model';

export interface IOffice {
  id?: number;
  parentId?: string | null;
  name?: string;
  officeType?: OfficeType;
  createdAt?: string;
  office?: IUser | null;
  parents?: IOffice[] | null;
  children?: IOffice | null;
}

export const defaultValue: Readonly<IOffice> = {};
