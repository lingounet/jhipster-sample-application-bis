import dayjs from 'dayjs/esm';
import { ISensitiveDataType } from 'app/entities/sensitive-data-type/sensitive-data-type.model';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';

export interface ISensitiveData {
  id?: number;
  date?: dayjs.Dayjs | null;
  sensitiveDataType?: ISensitiveDataType | null;
  securityInterview?: ISecurityInterview | null;
}

export class SensitiveData implements ISensitiveData {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public sensitiveDataType?: ISensitiveDataType | null,
    public securityInterview?: ISecurityInterview | null
  ) {}
}

export function getSensitiveDataIdentifier(sensitiveData: ISensitiveData): number | undefined {
  return sensitiveData.id;
}
