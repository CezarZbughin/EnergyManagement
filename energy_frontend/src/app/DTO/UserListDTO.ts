import { UserGetDTO } from "./UserGetDTO";

export class UserListDTO {
    public users : UserGetDTO[];

    constructor(users: UserGetDTO[]){
        this.users = users;
    }
    
}
  