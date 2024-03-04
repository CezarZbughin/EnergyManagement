export class LoginResponseDTO {
    username : string;
    token : string;
    userId : number;
    role : string;

    constructor(username : string, token : string, userId : number, role : string) {
        this.username = username;
        this.token = token;
        this.userId = userId;
        this.role = role;
    }
}
  