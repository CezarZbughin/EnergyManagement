export class UserPostDTO {
    public name : string;
    public username : string;
    public password : string;
    public role : string;    

    constructor(name: string, username : string, password : string, role : string){
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
}
  