import { IUser } from 'app/shared/model/user.model';
import { IComplain } from 'app/shared/model/complain.model';

export interface IOrganization {
  id?: number;
  name?: string;
  location?: string;
  organization?: IUser | null;
  complains?: IComplain[] | null;
}

export const defaultValue: Readonly<IOrganization> = {};
