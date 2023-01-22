import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthModule } from './modules/auth/auth.module';
import { SharedModule } from './modules/shared/shared.module';
import { RouterModule } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { MessagesModule } from 'primeng/messages';
import { HomeModule } from './modules/home/home.module';
import { AuthGuard } from './guards/auth.guard';
import { CoursesModule } from './modules/courses/courses.module';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RadioButtonModule } from 'primeng/radiobutton';
import { RippleModule } from 'primeng/ripple';
import { FormsModule } from '@angular/forms';
import { UserService } from './services/user.service';
import { MentorGuard } from './guards/mentor.guard';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { RecipesModule } from './modules/recipes/recipe.module';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    AuthModule,
    SharedModule,
    RouterModule,
    HttpClientModule,
    MessagesModule,
    HomeModule,
    CoursesModule,
    TableModule,
    ButtonModule,
    RippleModule,
    RadioButtonModule,
    FormsModule,
    RecipesModule
  ],
  providers: [
    AuthGuard,
    MentorGuard,
    MessageService,
    UserService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
