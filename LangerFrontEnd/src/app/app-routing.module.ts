import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { AdminComponent } from './components/admin/admin.component';
import { AdminLanguageComponent } from './components/admin-language/admin-language.component';
import { AdminResourceComponent } from './components/admin-resource/admin-resource.component';
import { AdminEpisodeComponent } from './components/admin-episode/admin-episode.component';
import { AdminSentenceComponent } from './components/admin-sentence/admin-sentence.component';
import { AuthGuard } from './services/auth/auth.guard';
import { AdminWordComponent } from './components/admin-word/admin-word.component';
import { AdminUserComponent } from './components/admin-user/admin-user.component';
import { BoardComponent } from './components/board/board.component';


const routes: Routes = 
[
  {
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'learn/:languageName',
    component: BoardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/word/:wordId',
    component: AdminWordComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/user/:userId',
    component: AdminUserComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/:languageName',
    component: AdminLanguageComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/:languageName/:title',
    component: AdminResourceComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/:languageName/:resourceTitle/:episodeTitle',
    component: AdminEpisodeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/:languageName/:resourceTitle/:episodeTitle/:paragraphIndex/:sentenceIndex',
    component: AdminSentenceComponent,
    canActivate: [AuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }