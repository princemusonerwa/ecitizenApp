import { IDistrict } from 'app/shared/model/district.model';

export interface IProvince {
  id?: number;
  name?: string;
  districts?: IDistrict[] | null;
}

export const defaultValue: Readonly<IProvince> = {};
