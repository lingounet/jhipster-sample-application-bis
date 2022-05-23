import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { Criticality } from 'app/entities/enumerations/criticality.model';

export interface IAvailability {
  id?: number;
  financial?: Criticality | null;
  legal?: Criticality | null;
  image?: Criticality | null;
  strategy?: Criticality | null;
  operational?: Criticality | null;
  traceabilityContentType?: string | null;
  traceability?: string | null;
  confidentialityContentType?: string | null;
  confidentiality?: string | null;
  integrityContentType?: string | null;
  integrity?: string | null;
  critical?: boolean | null;
  securityInterview?: ISecurityInterview | null;
}

export class Availability implements IAvailability {
  constructor(
    public id?: number,
    public financial?: Criticality | null,
    public legal?: Criticality | null,
    public image?: Criticality | null,
    public strategy?: Criticality | null,
    public operational?: Criticality | null,
    public traceabilityContentType?: string | null,
    public traceability?: string | null,
    public confidentialityContentType?: string | null,
    public confidentiality?: string | null,
    public integrityContentType?: string | null,
    public integrity?: string | null,
    public critical?: boolean | null,
    public securityInterview?: ISecurityInterview | null
  ) {
    this.critical = this.critical ?? false;
  }
}

export function getAvailabilityIdentifier(availability: IAvailability): number | undefined {
  return availability.id;
}
