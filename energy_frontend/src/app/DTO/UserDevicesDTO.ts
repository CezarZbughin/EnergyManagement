import { DeviceDTO } from "./DeviceDTO";

export class UserDevicesDTO {
    public id : number = 0;
    public username : string = "";
    public devices : DeviceDTO[] = [];

    constructor(id: number, username : string, devices: DeviceDTO[]){
        this.id = id;
        this.username = username;
        this.devices = devices;
    }
    
}
  