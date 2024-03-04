import { ConsumptionDTO } from "./ConsumptionDTO";

export class ConsumptionListDTO {
    public consumptions : ConsumptionDTO[] = [];

    constructor(consumptions: ConsumptionDTO[]){
        this.consumptions = consumptions;
    }
}