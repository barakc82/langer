import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { AuthService } from './services/auth/auth.service';
import { AuthGuard } from './services/auth/auth.guard';
import { DataService } from './services/data/data.service';
import { HomeComponent } from './components/home/home.component';
import { TokenInterceptorService } from './services/token-interceptor/token-interceptor.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule, MatFormFieldModule, MatOptionModule, MatDatepickerModule, MatAutocompleteModule,
  MatNativeDateModule, MatInputModule, MatGridListModule, MatRadioModule, MatSelectModule, MatTableModule, MatIconModule, MAT_DATE_LOCALE, MatTabsModule, MatCheckboxModule } from '@angular/material';
import { UtilsComponent } from './components/utils/utils.component';
import { AdminComponent } from './components/admin/admin.component';
import { AdminLanguageComponent } from './components/admin-language/admin-language.component';
import { AdminResourcesComponent } from './components/admin-resources/admin-resources.component';
import { AdminCorpusStatisticsWords } from './components/admin-corpus-statistics-words/admin-corpus-statistics-words.component';
import { AdminResourceComponent } from './components/admin-resource/admin-resource.component';
import { AdminEpisodesComponent } from './components/admin-episodes/admin-episodes.component';
import { AdminEpisodeComponent } from './components/admin-episode/admin-episode.component';
import { AdminCorpusStatisticsSentencesComponent } from './components/admin-corpus-statistics-sentences/admin-corpus-statistics-sentences.component';
import { AdminSentenceComponent } from './components/admin-sentence/admin-sentence.component';
import { AdminWordComponent } from './components/admin-word/admin-word.component';
import { BoardComponent } from './components/board/board.component';
import { AdminUserComponent } from './components/admin-user/admin-user.component';


@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    HomeComponent,
    UtilsComponent,
    AdminComponent,
    AdminLanguageComponent,
    AdminResourcesComponent,
    AdminCorpusStatisticsWords,
    AdminResourceComponent,
    AdminEpisodesComponent,
    AdminEpisodeComponent,
    AdminCorpusStatisticsSentencesComponent,
    AdminSentenceComponent,
    AdminWordComponent,
    BoardComponent,
    AdminUserComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatOptionModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    MatGridListModule,
    MatRadioModule,
    MatAutocompleteModule,
    MatSelectModule,
    MatTableModule,
    MatIconModule,
    MatCheckboxModule,
    MatTabsModule
  ],
  exports: [
    MatFormFieldModule,
    MatOptionModule,
    MatDatepickerModule,
    MatGridListModule,
    MatRadioModule,
    MatAutocompleteModule
  ],
  providers: [AuthService, AuthGuard, DataService, 
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptorService,
      multi: true
    },
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
    MatDatepickerModule
  ],
  bootstrap: [AppComponent],
  entryComponents: [],
})
export class AppModule { }