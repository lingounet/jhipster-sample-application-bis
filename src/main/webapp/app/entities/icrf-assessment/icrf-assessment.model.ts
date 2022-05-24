import { IIcrf } from 'app/entities/icrf/icrf.model';

export interface IIcrfAssessment {
  id?: number;
  code?: string | null;
  description?: string | null;
  status?: boolean | null;
  icrfs?: IIcrf[] | null;
}

export class IcrfAssessment implements IIcrfAssessment {
  constructor(
    public id?: number,
    public code?: string | null,
    public description?: string | null,
    public status?: boolean | null,
    public icrfs?: IIcrf[] | null
  ) {
    this.status = this.status ?? false;
  }
}

export function getIcrfAssessmentIdentifier(icrfAssessment: IIcrfAssessment): number | undefined {
  return icrfAssessment.id;
}
