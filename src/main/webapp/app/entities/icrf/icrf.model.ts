import dayjs from 'dayjs/esm';
import { IIcrfAssessment } from 'app/entities/icrf-assessment/icrf-assessment.model';
import { IIdentity } from 'app/entities/identity/identity.model';

export interface IIcrf {
  id?: number;
  date?: dayjs.Dayjs | null;
  icrfAssessment?: IIcrfAssessment | null;
  identity?: IIdentity | null;
}

export class Icrf implements IIcrf {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public icrfAssessment?: IIcrfAssessment | null,
    public identity?: IIdentity | null
  ) {}
}

export function getIcrfIdentifier(icrf: IIcrf): number | undefined {
  return icrf.id;
}
