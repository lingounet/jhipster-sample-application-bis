import { IIdentity } from 'app/entities/identity/identity.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IPsat {
  id?: number;
  amlId?: string;
  owner?: string;
  status?: Status;
  identity?: IIdentity | null;
}

export class Psat implements IPsat {
  constructor(
    public id?: number,
    public amlId?: string,
    public owner?: string,
    public status?: Status,
    public identity?: IIdentity | null
  ) {}
}

export function getPsatIdentifier(psat: IPsat): number | undefined {
  return psat.id;
}
