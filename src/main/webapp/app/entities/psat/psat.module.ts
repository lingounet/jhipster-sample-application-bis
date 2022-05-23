import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PsatComponent } from './list/psat.component';
import { PsatDetailComponent } from './detail/psat-detail.component';
import { PsatUpdateComponent } from './update/psat-update.component';
import { PsatDeleteDialogComponent } from './delete/psat-delete-dialog.component';
import { PsatRoutingModule } from './route/psat-routing.module';

@NgModule({
  imports: [SharedModule, PsatRoutingModule],
  declarations: [PsatComponent, PsatDetailComponent, PsatUpdateComponent, PsatDeleteDialogComponent],
  entryComponents: [PsatDeleteDialogComponent],
})
export class PsatModule {}
