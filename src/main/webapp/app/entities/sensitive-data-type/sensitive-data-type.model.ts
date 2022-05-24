import { ISensitiveData } from 'app/entities/sensitive-data/sensitive-data.model';

export interface ISensitiveDataType {
  id?: number;
  name?: string | null;
  sensitiveData?: ISensitiveData[] | null;
}

export class SensitiveDataType implements ISensitiveDataType {
  constructor(public id?: number, public name?: string | null, public sensitiveData?: ISensitiveData[] | null) {}
}

export function getSensitiveDataTypeIdentifier(sensitiveDataType: ISensitiveDataType): number | undefined {
  return sensitiveDataType.id;
}
