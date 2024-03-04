import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthResponse } from '../DTO/AuthResponse';
import { Observable } from 'rxjs';
import { DeviceDTO } from '../DTO/DeviceDTO';
import { UserDevicesDTO } from '../DTO/UserDevicesDTO';
import { UserListDTO } from '../DTO/UserListDTO';
import { UserPostDTO } from '../DTO/UserPostDTO';
import { UserGetDTO } from '../DTO/UserGetDTO';
import { DeviceListDTO } from '../DTO/DeviceListDTO';
import { DevicePostDTO } from '../DTO/DevicePostDTO';
import { ConsumptionListDTO } from '../DTO/ConsumptionListDTO';
import { LoginRequestDTO } from '../DTO/LoginRequestDTO';
import { LoginResponseDTO } from '../DTO/LoginResponseDTO';
import { ChatMessageRequestDTO } from '../DTO/ChatMessageRequestDTO';

@Injectable({
    providedIn: 'root'
  })
export class RequestService {
    userURL   : string = "http://localhost:8080";
    deviceURL : string = "http://localhost:8081";
    monitoringURL : string = "http://localhost:8082";
    chatURL : string = "http://localhost:8083";
    
    constructor(
        private http: HttpClient,
    ){}
    
    public login(username: string, password: string) : Observable<LoginResponseDTO> {
        return this.http.post<LoginResponseDTO>(
            `${this.userURL}/api/v1/auth`,
            new LoginRequestDTO(username, password)
        );
    }

    public getCurrentUser(username: string, password: string) : Observable<UserDevicesDTO> {
        return this.http.get<UserDevicesDTO>(
            `${this.deviceURL}/api/v1/user/self`,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        );
    }

    public getAllUsers(username: string, password: string) : Observable<UserListDTO> {
        return this.http.get<UserListDTO>(
            `${this.userURL}/api/v1/user`,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        );
    }

    public updateUser(username : string, password: string, id: number, userPostDTO : UserPostDTO){
        return this.http.put<UserGetDTO>(
            `${this.userURL}/api/v1/user/${id}`,
            userPostDTO,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        );
    }

    public deleteUser(username :string, password : string, id : number) {
        return this.http.delete<any>(
            `${this.userURL}/api/v1/user/${id}`,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        );
    }

    public createUser(agent_username : string, agent_password: string, userPostDTO : UserPostDTO ){
        return this.http.post<UserGetDTO>(
            `${this.userURL}/api/v1/user`,
            userPostDTO,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        );
    }

    public getAllDevices(agent_username : string, agent_password: string){
        return this.http.get<DeviceListDTO>(
            `${this.deviceURL}/api/v1/device`,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        );
    }

    public createDevice(agent_username : string, agent_password : string, devicePostDTO : DevicePostDTO) {
        return this.http.post<DeviceDTO>(
            `${this.deviceURL}/api/v1/device`,
            devicePostDTO,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        )
    }

    public updateDevice(agent_username : string, agent_password : string, id: number, device : DevicePostDTO){
        return this.http.put<DeviceDTO>(
            `${this.deviceURL}/api/v1/device/${id}`,
            device,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        )
    }

    public deleteDevice(agent_username : string, agent_password : string, id : number) {
        return this.http.delete<DeviceDTO>(
            `${this.deviceURL}/api/v1/device/${id}`,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        )
    }

    //the whole username password idea is shit. ill refactor it for the next project. but from now I will properly make it.
    public getDeviceConsumption(deviceId : number) {
        let username = sessionStorage.getItem("username") ?? "";
        let password = sessionStorage.getItem("password") ?? "";

        return this.http.get<ConsumptionListDTO>(
            `${this.monitoringURL}/api/v1/consumption-last-hour/device/${deviceId}`,

            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        )
    } 

    public sendChatMessage(text: string, receiverId : number){
        let dto = new ChatMessageRequestDTO(text, receiverId);
        return this.http.post<any>(
            `${this.chatURL}/api/v1/chat-message`,
            dto,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        )
    }

    public sendTypingNotification(receiverId : number){
        return this.http.get<any>(
            `${this.chatURL}/api/v1/chat-message/typing/${receiverId}`,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        )
    }
    
    public sendSeenNotification(receiverId : number){
        return this.http.get<any>(
            `${this.chatURL}/api/v1/chat-message/seen/${receiverId}`,
            {headers: new HttpHeaders({
                'Authorization' : this.makeAuthHeader(),
            })}
        )
    }

    private makeAuthHeader() : string {
        return 'Bearer ' + sessionStorage.getItem("jwt") ?? "";
    }


}
  