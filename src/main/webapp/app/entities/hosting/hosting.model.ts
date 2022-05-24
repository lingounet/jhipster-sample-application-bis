import dayjs from 'dayjs/esm';
import { IHostingPlatform } from 'app/entities/hosting-platform/hosting-platform.model';
import { IIdentity } from 'app/entities/identity/identity.model';

export interface IHosting {
  id?: number;
  date?: dayjs.Dayjs | null;
  hostingPlaform?: IHostingPlatform | null;
  identity?: IIdentity | null;
}

export class Hosting implements IHosting {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public hostingPlaform?: IHostingPlatform | null,
    public identity?: IIdentity | null
  ) {}
}

export function getHostingIdentifier(hosting: IHosting): number | undefined {
  return hosting.id;
}
