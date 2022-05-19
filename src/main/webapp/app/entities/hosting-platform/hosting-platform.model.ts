import { IHosting } from 'app/entities/hosting/hosting.model';
import { IHostingType } from 'app/entities/hosting-type/hosting-type.model';

export interface IHostingPlatform {
  id?: number;
  name?: string | null;
  hostings?: IHosting[] | null;
  hostingType?: IHostingType | null;
}

export class HostingPlatform implements IHostingPlatform {
  constructor(
    public id?: number,
    public name?: string | null,
    public hostings?: IHosting[] | null,
    public hostingType?: IHostingType | null
  ) {}
}

export function getHostingPlatformIdentifier(hostingPlatform: IHostingPlatform): number | undefined {
  return hostingPlatform.id;
}
