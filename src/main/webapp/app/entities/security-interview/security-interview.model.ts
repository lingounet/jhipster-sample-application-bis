import { IPsat } from 'app/entities/psat/psat.model';
import { IAvailability } from 'app/entities/availability/availability.model';
import { IHosting } from 'app/entities/hosting/hosting.model';
import { IPersonalData } from 'app/entities/personal-data/personal-data.model';
import { IIcrf } from 'app/entities/icrf/icrf.model';
import { ISensitiveData } from 'app/entities/sensitive-data/sensitive-data.model';
import { IComplementaryQuestion } from 'app/entities/complementary-question/complementary-question.model';
import { IApplicationType } from 'app/entities/application-type/application-type.model';
import { Process } from 'app/entities/enumerations/process.model';

export interface ISecurityInterview {
  id?: number;
  applicationName?: string;
  so?: string;
  process?: Process | null;
  psat?: IPsat | null;
  availability?: IAvailability | null;
  hostings?: IHosting[] | null;
  personalData?: IPersonalData[] | null;
  icrfs?: IIcrf[] | null;
  sensitiveData?: ISensitiveData[] | null;
  complementaryQuestions?: IComplementaryQuestion[] | null;
  applicationType?: IApplicationType | null;
}

export class SecurityInterview implements ISecurityInterview {
  constructor(
    public id?: number,
    public applicationName?: string,
    public so?: string,
    public process?: Process | null,
    public psat?: IPsat | null,
    public availability?: IAvailability | null,
    public hostings?: IHosting[] | null,
    public personalData?: IPersonalData[] | null,
    public icrfs?: IIcrf[] | null,
    public sensitiveData?: ISensitiveData[] | null,
    public complementaryQuestions?: IComplementaryQuestion[] | null,
    public applicationType?: IApplicationType | null
  ) {}
}

export function getSecurityInterviewIdentifier(securityInterview: ISecurityInterview): number | undefined {
  return securityInterview.id;
}
