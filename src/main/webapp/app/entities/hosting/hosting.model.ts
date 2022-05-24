import dayjs from 'dayjs/esm';
import { IHostingPlatform } from 'app/entities/hosting-platform/hosting-platform.model';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';

export interface IHosting {
  id?: number;
  date?: dayjs.Dayjs | null;
  hostingPlaform?: IHostingPlatform | null;
  securityInterview?: ISecurityInterview | null;
}

export class Hosting implements IHosting {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public hostingPlaform?: IHostingPlatform | null,
    public securityInterview?: ISecurityInterview | null
  ) {}
}

export function getHostingIdentifier(hosting: IHosting): number | undefined {
  return hosting.id;
}
