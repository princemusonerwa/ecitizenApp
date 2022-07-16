import dayjs from 'dayjs';
import { ICategory } from 'app/shared/model/category.model';
import { IUmuturage } from 'app/shared/model/umuturage.model';
import { IUser } from 'app/shared/model/user.model';
import { IOrganization } from 'app/shared/model/organization.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { Priority } from 'app/shared/model/enumerations/priority.model';

export interface IComplain {
  id?: number;
  ikibazo?: string;
  icyakozwe?: string;
  icyakorwa?: string | null;
  umwanzuro?: string | null;
  date?: string;
  status?: Status | null;
  priority?: Priority;
  category?: ICategory | null;
  umuturage?: IUmuturage | null;
  user?: IUser | null;
  organizations?: IOrganization[] | null;
}

export const defaultValue: Readonly<IComplain> = {};
