import { IVillage } from 'app/shared/model/village.model';
import { ISector } from 'app/shared/model/sector.model';

export interface ICell {
  id?: number;
  sectorCode?: string;
  name?: string;
  villages?: IVillage[] | null;
  sector?: ISector | null;
}

export const defaultValue: Readonly<ICell> = {};
