import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { DeviceListDTO } from 'src/app/DTO/DeviceListDTO';
import { DeviceDTO } from 'src/app/DTO/DeviceDTO';
import { UserListDTO } from 'src/app/DTO/UserListDTO';
import { UserPostDTO } from 'src/app/DTO/UserPostDTO';
import { RequestService } from 'src/app/service/RequestService';
import { DevicePostDTO } from 'src/app/DTO/DevicePostDTO';
import { UserGetDTO } from 'src/app/DTO/UserGetDTO';

@Component({
    selector: 'app-root',
    templateUrl: './devices.component.html',
    styleUrls:  ['./devices.component.css']
  })
export class DevicesComponent {
  devices : DeviceDTO[] = [];
  users : UserGetDTO[] = [];
  nrSelect = {
    id : 10,
    username : "Karma"
  };
  
  constructor(
    private requestService : RequestService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    
  }

  ngOnInit(): void {
    if(sessionStorage.getItem("role") != "ADMIN"){
      this.router.navigate(['/home'],{ skipLocationChange: false });
    }
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    
    if(username != null && password != null){
      this.requestService.getAllDevices(username, password).subscribe({
        complete: () => {},
        error: (error) => {
          console.log(error)
        },
        next: (response : DeviceListDTO) => {
          this.devices = response.devices;
        }
      });
      
      this.requestService.getAllUsers(username, password).subscribe({
        complete: () => {},
        error: (error) => {
          console.log(error)
        },
        next: (response : UserListDTO) => {
          this.users = response.users;
        }
      }); 
    }
    
  }
  
  onDescriptionChange(e : Event, device : DeviceDTO){
    const value : string = (e.target as HTMLInputElement).value;
    if(value == ""){ return; }
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username == null || password == null){return;}

    device.description = value;
    let insertDevice : DevicePostDTO = new DevicePostDTO(
      value, 
      device.address,
      device.maxHourlyConsumption,
      device.owner
    );

    this.requestService.updateDevice(username, password, device.id, insertDevice).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : any) => {}
    });
  }

  onAddressChange(e : Event, device : DeviceDTO){
    const value : string = (e.target as HTMLInputElement).value;
    if(value == ""){ return; }
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username == null || password == null){return;}

    device.address = value;
    let insertDevice : DevicePostDTO = new DevicePostDTO(
      device.description, 
      value,
      device.maxHourlyConsumption,
      device.owner
    );

    this.requestService.updateDevice(username, password, device.id, insertDevice).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : any) => {}
    });
  }

  onMaxHourlyConsumptionChange(e : Event, device : DeviceDTO){
    const value : string = (e.target as HTMLInputElement).value;
    if(value == ""){ return; }
    const valueN = Number(value);
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username == null || password == null){return;}

    device.maxHourlyConsumption = valueN;
    let insertDevice : DevicePostDTO = new DevicePostDTO(
      device.description, 
      device.address,
      valueN,
      device.owner
    );
    
    this.requestService.updateDevice(username, password, device.id, insertDevice).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : any) => {}
    });
  } 

  onOwnerChange(e : Event, device : DeviceDTO){
    const value : string = (e.target as HTMLInputElement).value;
    if(value == ""){ return; }
    const valueN = Number(value);
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username == null || password == null){return;}

    device.owner = valueN;
    let insertDevice : DevicePostDTO = new DevicePostDTO(
      device.description, 
      device.address,
      device.maxHourlyConsumption,
      valueN
    );
    
    this.requestService.updateDevice(username, password, device.id, insertDevice).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : any) => {}
    });
  } 

  onDeleteDevice(device : DeviceDTO){
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username == null || password == null){return;}
    this.requestService.deleteDevice(username, password, device.id).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : any) => {
        for(let i = 0; i < this.devices.length; i++){
          if(this.devices[i].id == device.id){
            this.devices.splice(i,1);
            break;
          }
        }
      }
    });
  }

  onDeviceAdd(description : string, address: string, maxHourlyConsumption: string, ownerId : string){
    var agent_username = sessionStorage.getItem("username");
    var agent_password = sessionStorage.getItem("password");
    if(agent_username == null || agent_password == null){return;}
    
    let maxHourlyConsumptionN : number = Number(maxHourlyConsumption);
    let ownerIdN : number = Number(ownerId);

    if(Number.isNaN(maxHourlyConsumption) || Number.isNaN(ownerId)){
      return;
    }

    let insertDevice : DevicePostDTO = new DevicePostDTO(
      description,
      address,
      maxHourlyConsumptionN,
      ownerIdN
    );
    this.requestService.createDevice(agent_username, agent_password, insertDevice).subscribe({
      complete: () => {},
      error: (error) => { console.log(error)},
      next: (response : DeviceDTO) => {
        this.devices.push(response);
      }
    });
  }

  onUsersClick(){
    this.router.navigate(['/admin'],{ skipLocationChange: false });
  }
  
}
  