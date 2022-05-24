import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IPsat {
  id?: number;
  amlId?: string;
  owner?: string;
  status?: Status;
  securityInterview?: ISecurityInterview | null;
}

export class Psat implements IPsat {
  constructor(
    public id?: number,
    public amlId?: string,
    public owner?: string,
    public status?: Status,
    public securityInterview?: ISecurityInterview | null
  ) {}
}

export function getPsatIdentifier(psat: IPsat): number | undefined {
  return psat.id;
}
