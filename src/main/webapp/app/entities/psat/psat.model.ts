import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IPsat {
  id?: number;
  amlId?: string | null;
  owner?: string | null;
  status?: Status | null;
  securityInterview?: ISecurityInterview | null;
}

export class Psat implements IPsat {
  constructor(
    public id?: number,
    public amlId?: string | null,
    public owner?: string | null,
    public status?: Status | null,
    public securityInterview?: ISecurityInterview | null
  ) {}
}

export function getPsatIdentifier(psat: IPsat): number | undefined {
  return psat.id;
}
