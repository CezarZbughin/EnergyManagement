import {Component} from '@angular/core';
import {ActivatedRoute, Route, Router} from "@angular/router";
import { ConsumptionDTO } from 'src/app/DTO/ConsumptionDTO';
import { ConsumptionListDTO } from 'src/app/DTO/ConsumptionListDTO';
import { RequestService } from 'src/app/service/RequestService';


@Component({
    selector: 'app-root',
    templateUrl: './consumption.component.html',
    styleUrls:  ['./consumption.component.css']
  })
export class ConsumptionComponent {
  deviceId : number = -1;
  consumptions : ConsumptionDTO[] = [];

  constructor(
    private route: Router,
    private activatedRoute : ActivatedRoute,
    private requestService : RequestService
    ){}

  ngOnInit(): void {
    let deviceIdStr = this.activatedRoute.snapshot.paramMap.get("id") ?? "-1";
    this.deviceId = parseInt(deviceIdStr);
    
    this.requestService.getDeviceConsumption(this.deviceId).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : ConsumptionListDTO) => {
        this.consumptions = response.consumptions;
      }
    })

  }
}
  