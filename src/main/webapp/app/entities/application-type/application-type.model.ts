import { IIdentity } from 'app/entities/identity/identity.model';

export interface IApplicationType {
  id?: number;
  name?: string | null;
  identities?: IIdentity[] | null;
}

export class ApplicationType implements IApplicationType {
  constructor(public id?: number, public name?: string | null, public identities?: IIdentity[] | null) {}
}

export function getApplicationTypeIdentifier(applicationType: IApplicationType): number | undefined {
  return applicationType.id;
}
