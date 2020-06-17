import { Routes, RouterModule } from "@angular/router";
import { LoginComponent } from "./main/login/login.component";
import { HomeComponent } from "./main/home/home.component";
import { NgModule } from '@angular/core';
import { ErrorComponent } from './main/errors/error/error.component';
import { ErrorInternalServerComponent } from './main/errors/error-internal-server/error-internal-server.component';
import { ErrorPageNotFoundComponent } from './main/errors/error-page-not-found/error-page-not-found.component';
import { ErrorUnauthorizedComponent } from './main/errors/error-unauthorized/error-unauthorized.component';
import { ErrorUnauthenticatedComponent } from './main/errors/error-unauthenticated/error-unauthenticated.component';
import { RegisterComponent } from './main/register/register.component';

const appRoutes: Routes = [


    //==============================================================================//
    // INITIAL
    //==============================================================================//
    {
        path        : 'pages/login',
        component   : LoginComponent,
    },


    //==============================================================================//
    // REDIRECTIONS
    //==============================================================================//
    {
        path        : '',
        redirectTo  : 'pages/home',
        pathMatch   : 'full',
    },


    //==============================================================================//
    // PAGES
    //==============================================================================//
    {
        path        : 'pages/home',
        component   : HomeComponent,
        // canActivate : [AdminGuard],
    },
    {
        path        : 'pages/register',
        component   : RegisterComponent,
        // canActivate : [AdminGuard],
    },


    //==============================================================================//
    // ERRORS
    //==============================================================================//
    {
        // 401
        path        : 'errors/unauthenticated',
        component   : ErrorUnauthenticatedComponent,
    },
    {
        // 403
        path        : 'errors/unauthorized',
        component   : ErrorUnauthorizedComponent,
    },
    {
        // 404
        path        : 'errors/page-not-found',
        component   : ErrorPageNotFoundComponent,
    },
    {
        // 500
        path        : 'errors/internal-server',
        component   : ErrorInternalServerComponent,
    },
    {
        // Unrecognized endpoint
        path        : '**',
        component   : ErrorComponent,
    },
];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule],
})
export class AppRoutingModule { }
