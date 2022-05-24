import { IPersonalData } from 'app/entities/personal-data/personal-data.model';
import { IPersonalDataRegion } from 'app/entities/personal-data-region/personal-data-region.model';

export interface IPersonalDataType {
  id?: number;
  name?: string | null;
  personalData?: IPersonalData[] | null;
  personalDataRegion?: IPersonalDataRegion | null;
}

export class PersonalDataType implements IPersonalDataType {
  constructor(
    public id?: number,
    public name?: string | null,
    public personalData?: IPersonalData[] | null,
    public personalDataRegion?: IPersonalDataRegion | null
  ) {}
}

export function getPersonalDataTypeIdentifier(personalDataType: IPersonalDataType): number | undefined {
  return personalDataType.id;
}
