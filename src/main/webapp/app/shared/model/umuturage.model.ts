import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IVillage } from 'app/shared/model/village.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IUmuturage {
  id?: number;
  indangamuntu?: string;
  amazina?: string;
  dob?: string;
  gender?: Gender;
  ubudeheCategory?: string;
  phone?: string | null;
  email?: string;
  user?: IUser | null;
  village?: IVillage | null;
}

export const defaultValue: Readonly<IUmuturage> = {};
