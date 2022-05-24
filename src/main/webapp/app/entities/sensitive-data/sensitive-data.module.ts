import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SensitiveDataComponent } from './list/sensitive-data.component';
import { SensitiveDataDetailComponent } from './detail/sensitive-data-detail.component';
import { SensitiveDataUpdateComponent } from './update/sensitive-data-update.component';
import { SensitiveDataDeleteDialogComponent } from './delete/sensitive-data-delete-dialog.component';
import { SensitiveDataRoutingModule } from './route/sensitive-data-routing.module';

@NgModule({
  imports: [SharedModule, SensitiveDataRoutingModule],
  declarations: [SensitiveDataComponent, SensitiveDataDetailComponent, SensitiveDataUpdateComponent, SensitiveDataDeleteDialogComponent],
  entryComponents: [SensitiveDataDeleteDialogComponent],
})
export class SensitiveDataModule {}
