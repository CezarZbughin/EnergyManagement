import { DeviceDTO } from "./DeviceDTO";

export class DeviceListDTO {
    public devices : DeviceDTO[];

    constructor(devices: DeviceDTO[]){
        this.devices = devices;
    }
    
}
  