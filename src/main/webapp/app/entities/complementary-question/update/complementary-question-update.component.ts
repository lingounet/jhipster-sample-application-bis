import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IComplementaryQuestion, ComplementaryQuestion } from '../complementary-question.model';
import { ComplementaryQuestionService } from '../service/complementary-question.service';
import { IIdentity } from 'app/entities/identity/identity.model';
import { IdentityService } from 'app/entities/identity/service/identity.service';

@Component({
  selector: 'jhi-complementary-question-update',
  templateUrl: './complementary-question-update.component.html',
})
export class ComplementaryQuestionUpdateComponent implements OnInit {
  isSaving = false;

  identitiesSharedCollection: IIdentity[] = [];

  editForm = this.fb.group({
    id: [],
    internet: [],
    development: [],
    configuration: [],
    cloud: [],
    internal: [],
    partner: [],
    users: [],
    identity: [],
  });

  constructor(
    protected complementaryQuestionService: ComplementaryQuestionService,
    protected identityService: IdentityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ complementaryQuestion }) => {
      this.updateForm(complementaryQuestion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const complementaryQuestion = this.createFromForm();
    if (complementaryQuestion.id !== undefined) {
      this.subscribeToSaveResponse(this.complementaryQuestionService.update(complementaryQuestion));
    } else {
      this.subscribeToSaveResponse(this.complementaryQuestionService.create(complementaryQuestion));
    }
  }

  trackIdentityById(_index: number, item: IIdentity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComplementaryQuestion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(complementaryQuestion: IComplementaryQuestion): void {
    this.editForm.patchValue({
      id: complementaryQuestion.id,
      internet: complementaryQuestion.internet,
      development: complementaryQuestion.development,
      configuration: complementaryQuestion.configuration,
      cloud: complementaryQuestion.cloud,
      internal: complementaryQuestion.internal,
      partner: complementaryQuestion.partner,
      users: complementaryQuestion.users,
      identity: complementaryQuestion.identity,
    });

    this.identitiesSharedCollection = this.identityService.addIdentityToCollectionIfMissing(
      this.identitiesSharedCollection,
      complementaryQuestion.identity
    );
  }

  protected loadRelationshipsOptions(): void {
    this.identityService
      .query()
      .pipe(map((res: HttpResponse<IIdentity[]>) => res.body ?? []))
      .pipe(
        map((identities: IIdentity[]) =>
          this.identityService.addIdentityToCollectionIfMissing(identities, this.editForm.get('identity')!.value)
        )
      )
      .subscribe((identities: IIdentity[]) => (this.identitiesSharedCollection = identities));
  }

  protected createFromForm(): IComplementaryQuestion {
    return {
      ...new ComplementaryQuestion(),
      id: this.editForm.get(['id'])!.value,
      internet: this.editForm.get(['internet'])!.value,
      development: this.editForm.get(['development'])!.value,
      configuration: this.editForm.get(['configuration'])!.value,
      cloud: this.editForm.get(['cloud'])!.value,
      internal: this.editForm.get(['internal'])!.value,
      partner: this.editForm.get(['partner'])!.value,
      users: this.editForm.get(['users'])!.value,
      identity: this.editForm.get(['identity'])!.value,
    };
  }
}
