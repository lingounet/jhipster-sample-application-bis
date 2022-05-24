import { IHostingPlatform } from 'app/entities/hosting-platform/hosting-platform.model';

export interface IHostingType {
  id?: number;
  name?: string | null;
  hostingPlatforms?: IHostingPlatform[] | null;
}

export class HostingType implements IHostingType {
  constructor(public id?: number, public name?: string | null, public hostingPlatforms?: IHostingPlatform[] | null) {}
}

export function getHostingTypeIdentifier(hostingType: IHostingType): number | undefined {
  return hostingType.id;
}
