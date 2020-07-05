import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { FuseConfigService } from '@fuse/services/config.service';
import { UserService } from 'app/services/user.service';
import { Router } from '@angular/router';
import { UserLoginRequest } from 'app/models/userLoginRequest';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations,
})
export class LoginComponent implements OnInit {
  
  loginForm: FormGroup;

  /**
   * Constructor
   *
   * @param {FuseConfigService} _fuseConfigService
   * @param {FormBuilder} _formBuilder
   */
  constructor(
      private _fuseConfigService: FuseConfigService,
      private _formBuilder: FormBuilder,
			private _userService: UserService,
			private _toastrService: ToastrService,
      private _router: Router,
  ) {
      // Configure the layout
      this._fuseConfigService.config = {
          layout: {
              navbar   : {
                  hidden: true
              },
              toolbar  : {
                  hidden: true
              },
              footer   : {
                  hidden: true
              },
              sidepanel: {
                  hidden: true
              }
          }
      };
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Lifecycle hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit() : void {
      this.loginForm = this._formBuilder.group({
          username  : ['', Validators.required],
          password  : ['', Validators.required]
			});
			
  }

  login() : void {
		const userLoginRequest = new UserLoginRequest(
			this.loginForm.value.username,
			this.loginForm.value.password,
		);

		this._userService.login(userLoginRequest).subscribe(
			() => {
				this._toastrService.success("Login successful.", "Success");
				this.redirectToHomePage();
			},
			(e: HttpErrorResponse) => {
                if (e.status == 401) {
                    this._toastrService.error("You are banned, you can't login.", "Failed to login");
                    this._router.navigate(['/pages/login']);
                } else {
                    this._toastrService.error(e.message, "Failed to login");
                }
			}
		);
	}
	
	redirectToHomePage() : void {
		if (this._userService.isLoggedIn()) {
			this._router.navigate(['/']);
		}
	}
}