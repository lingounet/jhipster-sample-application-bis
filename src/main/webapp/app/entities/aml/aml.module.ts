import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmlComponent } from './list/aml.component';
import { AmlDetailComponent } from './detail/aml-detail.component';
import { AmlUpdateComponent } from './update/aml-update.component';
import { AmlDeleteDialogComponent } from './delete/aml-delete-dialog.component';
import { AmlRoutingModule } from './route/aml-routing.module';

@NgModule({
  imports: [SharedModule, AmlRoutingModule],
  declarations: [AmlComponent, AmlDetailComponent, AmlUpdateComponent, AmlDeleteDialogComponent],
  entryComponents: [AmlDeleteDialogComponent],
})
export class AmlModule {}
