import dayjs from 'dayjs/esm';
import { ISensitiveDataType } from 'app/entities/sensitive-data-type/sensitive-data-type.model';
import { IIdentity } from 'app/entities/identity/identity.model';

export interface ISensitiveData {
  id?: number;
  date?: dayjs.Dayjs | null;
  sensitiveDataType?: ISensitiveDataType | null;
  identity?: IIdentity | null;
}

export class SensitiveData implements ISensitiveData {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public sensitiveDataType?: ISensitiveDataType | null,
    public identity?: IIdentity | null
  ) {}
}

export function getSensitiveDataIdentifier(sensitiveData: ISensitiveData): number | undefined {
  return sensitiveData.id;
}
