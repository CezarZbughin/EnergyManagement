export class DevicePostDTO {
    public description : string;
    public address : string;
    public maxHourlyConsumption : number;
    public ownerId : number;

    constructor(description : string, address : string, maxHourlyConsumption : number, owner : number){
        this.description = description;
        this. address = address;
        this.maxHourlyConsumption = maxHourlyConsumption;
        this.ownerId = owner;
    }
}
  