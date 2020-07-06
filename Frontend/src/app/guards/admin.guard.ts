import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from 'app/services/user.service';
import { UserTokenState } from 'app/models/userTokenState';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  public userTokenState: UserTokenState;

  constructor(
    private router: Router,
    private userService: UserService,
  ) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.userTokenState = this.userService.getLoggedInUser();

    if (this.userTokenState) {
      if (this.userTokenState.role == "ROLE_ADMIN") {
        return true;
      } else {
        this.router.navigate(['/errors/unauthorized']);
        return false;
      }
    }

    this.router.navigate(['/errors/unauthenticated']);
    return false;
  }
  
}