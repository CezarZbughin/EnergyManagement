import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { AuthDTO } from 'src/app/DTO/AuthDTO';
import { AuthResponse } from 'src/app/DTO/AuthResponse';
import { UserDevicesDTO } from 'src/app/DTO/UserDevicesDTO';
import { RequestService } from 'src/app/service/RequestService';
import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import { NotificationType, NotificationsService } from 'angular2-notifications';
import { NotificationDTO } from 'src/app/DTO/NotificationDTO';


@Component({
    selector: 'app-root',
    templateUrl: './user.component.html',
    styleUrls:  ['./user.component.css']
  })
export class UserComponent {
  socket = new SockJS('http://localhost:8082/mess', 
    { 
      headers: {
      "Authorization": `Bearer ${sessionStorage.getItem("jwt") ?? ""}`
      }
    }
  );
  stompClient = Stomp.over(this.socket);
  sessionId = "";
  userDevicesDTO : UserDevicesDTO = new UserDevicesDTO(0,"",[]);
  lastNotificationTimestamp = 0;
  constructor(
    private requestService : RequestService,
    private router: Router,
    private route: ActivatedRoute,
    private notificationService : NotificationsService
  ) {
    
  }

  ngOnInit(): void {
    if(sessionStorage.getItem("role") != "USER"){
      this.router.navigate(['/home'],{ skipLocationChange: false });
    }

    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    if(username != null && password != null){
      this.requestService.getCurrentUser(username, password).subscribe({
        complete: () => {},
        error: (error) => {
          console.log(error)
        },
        next: (response : UserDevicesDTO) => {
          this.userDevicesDTO =response;
        }
      }); 
    }
  
    var that = this;
    this.stompClient.connect({}, function (frame : any) {
      that.stompClient.subscribe('/topic/llll/' + sessionStorage.getItem("userId"), function (msgOut) {
        console.log("HOLY SHITTY NIOTFY");
        
        const uint8Array = new Uint8Array(msgOut.binaryBody);
        const text = new TextDecoder().decode(uint8Array);
        let notification : NotificationDTO = JSON.parse(text) as NotificationDTO;
        let message = "Device with id " + notification.deviceId + "has exeeded the limit." + notification.message;
        if(new Date().getTime() - that.lastNotificationTimestamp > 20 * 1000 ){
          that.notificationService.success("Device consumption limit exceeded!" , notification.message, {
            position: ['bottom', 'right'],
            timeOut : 10000,
            animate : 'fade',
            showProgressBar: true
          });
          that.lastNotificationTimestamp = new Date().getTime();
        }
       })
   });
  }

  onDeviceClick(deviceId : number) : void {
    this.router.navigate(['/consumption/' + deviceId],{ skipLocationChange: false });
  }


}
  