export class DeviceDTO {
    public id : number;
    public description : string;
    public address : string;
    public maxHourlyConsumption : number;
    public owner : number;

    constructor(id : number, description : string, address : string, maxHourlyConsumption : number, owner : number){
        this.id = id;
        this.description = description;
        this. address = address;
        this.maxHourlyConsumption = maxHourlyConsumption;
        this.owner = owner;
    }
}
  