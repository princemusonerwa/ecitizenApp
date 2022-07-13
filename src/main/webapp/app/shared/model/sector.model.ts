import { ICell } from 'app/shared/model/cell.model';
import { IDistrict } from 'app/shared/model/district.model';

export interface ISector {
  id?: number;
  name?: string;
  cells?: ICell[] | null;
  district?: IDistrict | null;
}

export const defaultValue: Readonly<ISector> = {};
