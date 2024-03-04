export class AuthResponse {
    valid : boolean;
    role : string;
    id : number;
    
    constructor(valid : boolean, role : string, id : number) {
        this.valid = valid;
        this.role = role;
        this.id = id;
    }
}
  