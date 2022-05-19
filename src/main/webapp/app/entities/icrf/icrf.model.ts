import { IIcrfStatus } from 'app/entities/icrf-status/icrf-status.model';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';

export interface IIcrf {
  id?: number;
  code?: string | null;
  description?: string | null;
  icrfStatus?: IIcrfStatus | null;
  securityInterview?: ISecurityInterview | null;
}

export class Icrf implements IIcrf {
  constructor(
    public id?: number,
    public code?: string | null,
    public description?: string | null,
    public icrfStatus?: IIcrfStatus | null,
    public securityInterview?: ISecurityInterview | null
  ) {}
}

export function getIcrfIdentifier(icrf: IIcrf): number | undefined {
  return icrf.id;
}
