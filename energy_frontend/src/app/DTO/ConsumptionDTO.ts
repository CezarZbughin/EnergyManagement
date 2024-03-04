export class ConsumptionDTO {
    public id : number = 0;
    public device : number = 0;
    public timestamp : number = 0;
    public measure : number = 0;

    constructor(id : number, device : number, timestamp : number, measure: number){
        this.id = id;
        this.device = device;
        this.timestamp = timestamp;
        this.measure = measure;
    }
    
}