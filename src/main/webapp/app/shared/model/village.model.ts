import { IUmuturage } from 'app/shared/model/umuturage.model';
import { ICell } from 'app/shared/model/cell.model';

export interface IVillage {
  id?: number;
  name?: string;
  umuturages?: IUmuturage[] | null;
  cell?: ICell | null;
}

export const defaultValue: Readonly<IVillage> = {};
