export class NotificationDTO {
    public deviceId : number;
    public userId : number;
    public message : string;

    constructor(deviceId : number, userId : number, message : string){
        this.deviceId = deviceId;
        this.userId = userId;
        this.message = message;
    }
}