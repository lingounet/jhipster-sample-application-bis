import dayjs from 'dayjs/esm';
import { IPersonalDataType } from 'app/entities/personal-data-type/personal-data-type.model';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';

export interface IPersonalData {
  id?: number;
  date?: dayjs.Dayjs | null;
  personalDataType?: IPersonalDataType | null;
  securityInterview?: ISecurityInterview | null;
}

export class PersonalData implements IPersonalData {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public personalDataType?: IPersonalDataType | null,
    public securityInterview?: ISecurityInterview | null
  ) {}
}

export function getPersonalDataIdentifier(personalData: IPersonalData): number | undefined {
  return personalData.id;
}
