import { IIdentity } from 'app/entities/identity/identity.model';

export interface IComplementaryQuestion {
  id?: number;
  internet?: boolean | null;
  development?: boolean | null;
  configuration?: boolean | null;
  cloud?: boolean | null;
  internal?: boolean | null;
  partner?: boolean | null;
  users?: number | null;
  identity?: IIdentity | null;
}

export class ComplementaryQuestion implements IComplementaryQuestion {
  constructor(
    public id?: number,
    public internet?: boolean | null,
    public development?: boolean | null,
    public configuration?: boolean | null,
    public cloud?: boolean | null,
    public internal?: boolean | null,
    public partner?: boolean | null,
    public users?: number | null,
    public identity?: IIdentity | null
  ) {
    this.internet = this.internet ?? false;
    this.development = this.development ?? false;
    this.configuration = this.configuration ?? false;
    this.cloud = this.cloud ?? false;
    this.internal = this.internal ?? false;
    this.partner = this.partner ?? false;
  }
}

export function getComplementaryQuestionIdentifier(complementaryQuestion: IComplementaryQuestion): number | undefined {
  return complementaryQuestion.id;
}
