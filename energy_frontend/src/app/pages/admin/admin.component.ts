import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { UserGetDTO } from 'src/app/DTO/UserGetDTO';
import { UserListDTO } from 'src/app/DTO/UserListDTO';
import { UserPostDTO } from 'src/app/DTO/UserPostDTO';
import { RequestService } from 'src/app/service/RequestService';

@Component({
    selector: 'app-root',
    templateUrl: './admin.component.html',
    styleUrls:  ['./admin.component.css']
  })
export class AdminComponent {
  users : UserGetDTO[] = []

  constructor(
    private requestService : RequestService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    if(sessionStorage.getItem("role") != "ADMIN"){
      this.router.navigate(['/home'],{ skipLocationChange: false });
    }
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username != null && password != null){
      this.requestService.getAllUsers(username, password).subscribe({
        complete: () => {},
        error: (error) => {
          console.log(error)
        },
        next: (response : UserListDTO) => {
          this.users = response.users;
        }
      }); 
    }
  }

  onNameChange(e : Event, user : UserGetDTO){
    const value : string = (e.target as HTMLInputElement).value;
    if(value == ""){ return; }
    user.name = value;
    var insertUser = new UserPostDTO(user.name, user.username, "", user.role);
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username == null || password == null){return;}
    this.requestService.updateUser(username, password, user.id, insertUser).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : any) => {}
    });
  }

  onRoleChange(e : Event, user : UserGetDTO){
    const value : string = (e.target as HTMLInputElement).value;
    if(value == ""){ return; }
    user.role = value;
    var insertUser = new UserPostDTO(user.name, user.username, "", user.role);
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username == null || password == null){return;}
    this.requestService.updateUser(username, password, user.id, insertUser).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : any) => {}
    });
  }

  onDeleteUser(user : UserGetDTO){
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username == null || password == null){return;}
    this.requestService.deleteUser(username, password, user.id).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : any) => {
        for(let i = 0; i < this.users.length; i++){
          if(this.users[i].id == user.id){
            this.users.splice(i,1);
            break;
          }
        }
      }
    });
  }

  onUserAdd(name : string, username: string, password: string, role:string){
    var agent_username = sessionStorage.getItem("username");
    var agent_password = sessionStorage.getItem("password");
    let userPostDTO = new UserPostDTO(name , username, password, role)
    if(agent_username == null || agent_password == null){return;}
    this.requestService.createUser(agent_username, agent_password, userPostDTO).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : UserGetDTO) => {
        this.users.push(response);
      }
    });
  }

  onDevicesClick(){
    this.router.navigate(['/device'],{ skipLocationChange: false });
  }

}
  