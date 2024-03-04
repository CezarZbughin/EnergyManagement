import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { UserComponent } from './pages/user/user.component';
import { AppComponent } from './app.component';
import { AdminComponent } from './pages/admin/admin.component';
import { DevicesComponent } from './pages/devices/devices.component';
import { ConsumptionComponent } from './pages/consumption/consumption.component';
import { ChatComponent } from './pages/chat/chat.component';


const routes: Routes = [
  { path: '',   redirectTo: '/home', pathMatch: 'full' },
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'user',
    component: UserComponent
  },
  {
    path: 'admin',
    component: AdminComponent
  },
  {
    path: 'device',
    component: DevicesComponent
  },
  {
    path: 'consumption/:id',
    component: ConsumptionComponent
  },
  {
    path: 'chat',
    component: ChatComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
