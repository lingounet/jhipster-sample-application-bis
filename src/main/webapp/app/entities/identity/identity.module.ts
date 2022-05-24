import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IdentityComponent } from './list/identity.component';
import { IdentityDetailComponent } from './detail/identity-detail.component';
import { IdentityUpdateComponent } from './update/identity-update.component';
import { IdentityDeleteDialogComponent } from './delete/identity-delete-dialog.component';
import { IdentityRoutingModule } from './route/identity-routing.module';

@NgModule({
  imports: [SharedModule, IdentityRoutingModule],
  declarations: [IdentityComponent, IdentityDetailComponent, IdentityUpdateComponent, IdentityDeleteDialogComponent],
  entryComponents: [IdentityDeleteDialogComponent],
})
export class IdentityModule {}
