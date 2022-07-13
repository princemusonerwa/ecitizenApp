import { IUser } from 'app/shared/model/user.model';

export interface IUserExtended {
  id?: number;
  phone?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserExtended> = {};
