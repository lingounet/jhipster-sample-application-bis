export interface IAml {
  id?: number;
  name?: string | null;
}

export class Aml implements IAml {
  constructor(public id?: number, public name?: string | null) {}
}

export function getAmlIdentifier(aml: IAml): number | undefined {
  return aml.id;
}
