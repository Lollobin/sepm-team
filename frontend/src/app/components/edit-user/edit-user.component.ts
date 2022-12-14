import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PasswordReset, UserManagementService } from 'src/app/generated-sources/openapi';
import { CustomAuthService } from '../../services/custom-auth.service';
import { faArrowsRotate } from "@fortawesome/free-solid-svg-icons";
import { AuthRequest } from 'src/app/dtos/auth-request';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss'],
})
export class EditUserComponent implements OnInit {

  editForm: FormGroup;
  passwordForm: FormGroup;
  submitted = false;
  user = {
    firstName: "", lastName: "", email: "", gender: "",
    address: { houseNumber: "", street: "", zipCode: "", city: "", country: "" }
  };
  errorMessage = '';
  genders = [{ description: "Female", value: "female" }, {
    description: "Male",
    value: "male"
  }, { description: "Other", value: "other" }];
  faArrowsRotate = faArrowsRotate;
  display = "none";
  action = "none";

  constructor(private formBuilder: FormBuilder, private authService: CustomAuthService,
    private userManagementService: UserManagementService, private router: Router, 
    private toastr: ToastrService) {
    this.editForm = this.formBuilder.group({
      firstName: ["", [Validators.required]],
      lastName: ["", [Validators.required]],
      email: ["", [Validators.required, Validators.email]],
      address: this.formBuilder.group({
        houseNumber: ["", [Validators.required]],
        street: ["", [Validators.required]],
        zipCode: ["", [Validators.required]],
        city: ["", [Validators.required]],
        country: ["", [Validators.required]]
      }),
      gender: ["", [Validators.required]]
    }
    );
    this.passwordForm = this.formBuilder.group({
      password: ["", [Validators.required, Validators.minLength(8)]]
    });
  }

  get f() {
    return this.editForm.controls;
  }

  get p() {
    return this.passwordForm.controls;
  }

  ngOnInit(): void {
    this.getUserInfo();
  }

  getUserInfo() {
    this.userManagementService.userInfoGet().subscribe({
      next: (next) => {
        this.user = next;
        console.log("Succesfully got user with id " + next.userId);
        this.editForm.controls['firstName'].setValue(this.user.firstName);
        this.editForm.controls['lastName'].setValue(this.user.lastName);
        this.editForm.controls['gender'].setValue(this.user.gender);
        this.editForm.controls['email'].setValue(this.user.email);
        const address = {
          houseNumber: this.user.address.houseNumber,
          street: this.user.address.street,
          zipCode: this.user.address.zipCode,
          city: this.user.address.city,
          country: this.user.address.country
        };
        this.editForm.controls['address'].setValue(address);
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });
  }

  putUser() {
    const user = {
      firstName: this.editForm.value.firstName,
      lastName: this.editForm.value.lastName,
      gender: this.editForm.value.gender,
      email: this.editForm.value.email,
      address: {
        houseNumber: this.editForm.value.address.houseNumber,
        street: this.editForm.value.address.street,
        zipCode: this.editForm.value.address.zipCode,
        city: this.editForm.value.address.city,
        country: this.editForm.value.address.country
      },
      password: this.passwordForm.value.password
    };
    this.userManagementService.usersPut(user).subscribe({
      next: (_next) => {
        console.log("Succesfully updated user information");
        this.reloadToken();
        this.toastr.success("Successfully edited account!");
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
        this.passwordForm.reset();
      }
    });
  }

  deleteUser() {
    this.userManagementService.usersDelete().subscribe({
      next: (_next) => {
        console.log("Succesfully deleted user");
        this.authService.logoutUser();
        this.toastr.success("Successfully deleted account!");
        this.router.navigateByUrl("/");
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
        this.passwordForm.reset();
      }
    });
  }

  openModal() {
    this.display = "block";
  }

  onCloseHandled() {
    this.display = "none";
  }

  clearForm() {
    this.editForm.reset();
  }

  isUser() {
    return this.authService.getUserRole() === "USER";
  }

  setAction(text: string) {
    if (text === "edit" || text === "delete" || text === "changePassword") {
      this.action = text;
    } else {
      this.action = "none";
    }
  }

  confirm() {
    if (this.action === "edit") {
      this.putUser();
    } else if (this.action === "delete") {
      this.deleteUser();
    } else if (this.action === "changePassword") {
      this.sendRequest();
    }
  }

  confirmUser() {
    this.submitted = true;
    if (this.passwordForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(this.user.email, this.passwordForm.controls.password.value);
      console.log(this.user.email);
      this.authenticateUser(authRequest);
      this.onCloseHandled();
    } else {
      console.log('Invalid input');
    }
  }

  authenticateUser(authRequest: AuthRequest) {
    console.log('Try to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        this.confirm();
        console.log('Successfully confirmed user: ' + authRequest.email);
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
        this.passwordForm.reset();
      }
    });
  }

  reloadToken() {
    const authRequest: AuthRequest = new AuthRequest(this.editForm.controls.email.value, this.passwordForm.controls.password.value);
    this.passwordForm.reset();
    console.log('Try to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        console.log('Successfully confirmed user: ' + authRequest.email);
        this.getUserInfo();
        this.router.navigateByUrl("/");
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });
  }

  sendRequest() {

    const passwordReset: PasswordReset = {
      email: this.user.email,
      clientURI: 'http://' + window.location.host + '#/passwordUpdate'
    };
    console.log("sending");
    this.userManagementService.passwordResetPost(passwordReset, 'body', true).subscribe(
        {
          next: (response) => {
            console.log(response);
            this.toastr.success("Successfully sent password reset mail!");
            this.authService.logoutUser();
            this.router.navigateByUrl("/");
          },
          error: (error) => {
            console.log(error);
            if (error.status === 0 || error.status === 500) {
              this.toastr.error(error.message);
            } else {
              this.toastr.warning(error.error);
            }
          }
        }
    );
  }
}
