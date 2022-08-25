import { ISector } from 'app/shared/model/sector.model';
import { IProvince } from 'app/shared/model/province.model';

export interface IDistrict {
  id?: number;
  districtCode?: string;
  name?: string;
  sectors?: ISector[] | null;
  province?: IProvince | null;
}

export const defaultValue: Readonly<IDistrict> = {};
