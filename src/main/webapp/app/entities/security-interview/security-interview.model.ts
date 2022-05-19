import { IAvailability } from 'app/entities/availability/availability.model';
import { IApplicationType } from 'app/entities/application-type/application-type.model';
import { IHosting } from 'app/entities/hosting/hosting.model';
import { IPersonalData } from 'app/entities/personal-data/personal-data.model';
import { IIcrf } from 'app/entities/icrf/icrf.model';
import { ISensitiveData } from 'app/entities/sensitive-data/sensitive-data.model';
import { IComplementaryQuestion } from 'app/entities/complementary-question/complementary-question.model';
import { IPsat } from 'app/entities/psat/psat.model';
import { Process } from 'app/entities/enumerations/process.model';

export interface ISecurityInterview {
  id?: number;
  applicationName?: string | null;
  so?: string | null;
  process?: Process | null;
  availability?: IAvailability | null;
  applicationTypes?: IApplicationType[] | null;
  hostings?: IHosting[] | null;
  personalData?: IPersonalData[] | null;
  icrfs?: IIcrf[] | null;
  sensitiveData?: ISensitiveData[] | null;
  complementaryQuestions?: IComplementaryQuestion[] | null;
  psat?: IPsat | null;
}

export class SecurityInterview implements ISecurityInterview {
  constructor(
    public id?: number,
    public applicationName?: string | null,
    public so?: string | null,
    public process?: Process | null,
    public availability?: IAvailability | null,
    public applicationTypes?: IApplicationType[] | null,
    public hostings?: IHosting[] | null,
    public personalData?: IPersonalData[] | null,
    public icrfs?: IIcrf[] | null,
    public sensitiveData?: ISensitiveData[] | null,
    public complementaryQuestions?: IComplementaryQuestion[] | null,
    public psat?: IPsat | null
  ) {}
}

export function getSecurityInterviewIdentifier(securityInterview: ISecurityInterview): number | undefined {
  return securityInterview.id;
}