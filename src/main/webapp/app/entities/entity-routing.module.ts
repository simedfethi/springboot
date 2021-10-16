import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'publisher',
        data: { pageTitle: 'tripApp.publisher.home.title' },
        loadChildren: () => import('./publisher/publisher.module').then(m => m.PublisherModule),
      },
      {
        path: 'application-user',
        data: { pageTitle: 'tripApp.applicationUser.home.title' },
        loadChildren: () => import('./application-user/application-user.module').then(m => m.ApplicationUserModule),
      },
      {
        path: 'user-phone',
        data: { pageTitle: 'tripApp.userPhone.home.title' },
        loadChildren: () => import('./user-phone/user-phone.module').then(m => m.UserPhoneModule),
      },
      {
        path: 'user-preferences',
        data: { pageTitle: 'tripApp.userPreferences.home.title' },
        loadChildren: () => import('./user-preferences/user-preferences.module').then(m => m.UserPreferencesModule),
      },
      {
        path: 'user-government-id',
        data: { pageTitle: 'tripApp.userGovernmentID.home.title' },
        loadChildren: () => import('./user-government-id/user-government-id.module').then(m => m.UserGovernmentIDModule),
      },
      {
        path: 'notifications',
        data: { pageTitle: 'tripApp.notifications.home.title' },
        loadChildren: () => import('./notifications/notifications.module').then(m => m.NotificationsModule),
      },
      {
        path: 'trip-groups',
        data: { pageTitle: 'tripApp.tripGroups.home.title' },
        loadChildren: () => import('./trip-groups/trip-groups.module').then(m => m.TripGroupsModule),
      },
      {
        path: 'trip-group-members',
        data: { pageTitle: 'tripApp.tripGroupMembers.home.title' },
        loadChildren: () => import('./trip-group-members/trip-group-members.module').then(m => m.TripGroupMembersModule),
      },
      {
        path: 'tripdetail',
        data: { pageTitle: 'tripApp.tripdetail.home.title' },
        loadChildren: () => import('./tripdetail/tripdetail.module').then(m => m.TripdetailModule),
      },
      {
        path: 'trip-category',
        data: { pageTitle: 'tripApp.tripCategory.home.title' },
        loadChildren: () => import('./trip-category/trip-category.module').then(m => m.TripCategoryModule),
      },
      {
        path: 'adress-list',
        data: { pageTitle: 'tripApp.adressList.home.title' },
        loadChildren: () => import('./adress-list/adress-list.module').then(m => m.AdressListModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
