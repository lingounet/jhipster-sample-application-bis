import dayjs from 'dayjs/esm';
import { IPersonalDataType } from 'app/entities/personal-data-type/personal-data-type.model';
import { IIdentity } from 'app/entities/identity/identity.model';

export interface IPersonalData {
  id?: number;
  date?: dayjs.Dayjs | null;
  personalDataType?: IPersonalDataType | null;
  identity?: IIdentity | null;
}

export class PersonalData implements IPersonalData {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public personalDataType?: IPersonalDataType | null,
    public identity?: IIdentity | null
  ) {}
}

export function getPersonalDataIdentifier(personalData: IPersonalData): number | undefined {
  return personalData.id;
}
