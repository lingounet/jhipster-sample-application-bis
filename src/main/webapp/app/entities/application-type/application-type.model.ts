import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';

export interface IApplicationType {
  id?: number;
  name?: string | null;
  securityInterview?: ISecurityInterview | null;
}

export class ApplicationType implements IApplicationType {
  constructor(public id?: number, public name?: string | null, public securityInterview?: ISecurityInterview | null) {}
}

export function getApplicationTypeIdentifier(applicationType: IApplicationType): number | undefined {
  return applicationType.id;
}
