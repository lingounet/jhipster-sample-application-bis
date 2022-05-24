import { IIcrf } from 'app/entities/icrf/icrf.model';

export interface IIcrfStatus {
  id?: number;
  name?: string | null;
  icrf?: IIcrf | null;
}

export class IcrfStatus implements IIcrfStatus {
  constructor(public id?: number, public name?: string | null, public icrf?: IIcrf | null) {}
}

export function getIcrfStatusIdentifier(icrfStatus: IIcrfStatus): number | undefined {
  return icrfStatus.id;
}
