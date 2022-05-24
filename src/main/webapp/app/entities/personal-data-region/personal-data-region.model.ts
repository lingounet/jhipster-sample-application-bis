import { IPersonalDataType } from 'app/entities/personal-data-type/personal-data-type.model';

export interface IPersonalDataRegion {
  id?: number;
  name?: string | null;
  personalDataTypes?: IPersonalDataType[] | null;
}

export class PersonalDataRegion implements IPersonalDataRegion {
  constructor(public id?: number, public name?: string | null, public personalDataTypes?: IPersonalDataType[] | null) {}
}

export function getPersonalDataRegionIdentifier(personalDataRegion: IPersonalDataRegion): number | undefined {
  return personalDataRegion.id;
}
