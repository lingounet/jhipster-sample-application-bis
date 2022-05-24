import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'aml',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.aml.home.title' },
        loadChildren: () => import('./aml/aml.module').then(m => m.AmlModule),
      },
      {
        path: 'application-type',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.applicationType.home.title' },
        loadChildren: () => import('./application-type/application-type.module').then(m => m.ApplicationTypeModule),
      },
      {
        path: 'hosting-type',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.hostingType.home.title' },
        loadChildren: () => import('./hosting-type/hosting-type.module').then(m => m.HostingTypeModule),
      },
      {
        path: 'hosting-platform',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.hostingPlatform.home.title' },
        loadChildren: () => import('./hosting-platform/hosting-platform.module').then(m => m.HostingPlatformModule),
      },
      {
        path: 'personal-data-region',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.personalDataRegion.home.title' },
        loadChildren: () => import('./personal-data-region/personal-data-region.module').then(m => m.PersonalDataRegionModule),
      },
      {
        path: 'personal-data-type',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.personalDataType.home.title' },
        loadChildren: () => import('./personal-data-type/personal-data-type.module').then(m => m.PersonalDataTypeModule),
      },
      {
        path: 'icrf-assessment',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.icrfAssessment.home.title' },
        loadChildren: () => import('./icrf-assessment/icrf-assessment.module').then(m => m.IcrfAssessmentModule),
      },
      {
        path: 'sensitive-data-type',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.sensitiveDataType.home.title' },
        loadChildren: () => import('./sensitive-data-type/sensitive-data-type.module').then(m => m.SensitiveDataTypeModule),
      },
      {
        path: 'psat',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.psat.home.title' },
        loadChildren: () => import('./psat/psat.module').then(m => m.PsatModule),
      },
      {
        path: 'identity',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.identity.home.title' },
        loadChildren: () => import('./identity/identity.module').then(m => m.IdentityModule),
      },
      {
        path: 'hosting',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.hosting.home.title' },
        loadChildren: () => import('./hosting/hosting.module').then(m => m.HostingModule),
      },
      {
        path: 'personal-data',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.personalData.home.title' },
        loadChildren: () => import('./personal-data/personal-data.module').then(m => m.PersonalDataModule),
      },
      {
        path: 'icrf',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.icrf.home.title' },
        loadChildren: () => import('./icrf/icrf.module').then(m => m.IcrfModule),
      },
      {
        path: 'sensitive-data',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.sensitiveData.home.title' },
        loadChildren: () => import('./sensitive-data/sensitive-data.module').then(m => m.SensitiveDataModule),
      },
      {
        path: 'availability',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.availability.home.title' },
        loadChildren: () => import('./availability/availability.module').then(m => m.AvailabilityModule),
      },
      {
        path: 'complementary-question',
        data: { pageTitle: 'jhipsterSampleApplicationBisApp.complementaryQuestion.home.title' },
        loadChildren: () => import('./complementary-question/complementary-question.module').then(m => m.ComplementaryQuestionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
