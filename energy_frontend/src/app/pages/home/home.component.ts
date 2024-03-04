import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { AuthDTO } from 'src/app/DTO/AuthDTO';
import { AuthResponse } from 'src/app/DTO/AuthResponse';
import { LoginResponseDTO } from 'src/app/DTO/LoginResponseDTO';
import { RequestService } from 'src/app/service/RequestService';

@Component({
  selector: 'app-root',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
 
  constructor(
    private requestService : RequestService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  
  ngOnInit(): void {}

  onLoginClick(username: string, password : string){
    sessionStorage.setItem("username", username);
    sessionStorage.setItem("password", password);
    
    this.requestService.login(username, password).subscribe({
      complete: () => {},
      error: (error) => { console.log(error) },
      next: (response : LoginResponseDTO) => {
        console.log(response);
        sessionStorage.setItem("role", response.role); 
        sessionStorage.setItem("userId", response.userId.toString());
        sessionStorage.setItem("jwt", response.token);
        
        if(response.role == "USER"){
          this.router.navigate(['/user'],{ skipLocationChange: false });
        } else if(response.role == "ADMIN"){
          this.router.navigate(['/admin'],{ skipLocationChange: false });
        }
      }
    });
  }

  onSignUpClick(name: string, username:string, password:string){
    console.log(name + " " + username + " " + password);
  }

  


  
}
