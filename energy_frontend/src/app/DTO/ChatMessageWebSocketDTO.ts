export class ChatMessageWebSocketDTO {
    type : string = "";
    message : string = "";
    senderId : number = 0;

    constructor(type : string, message : string, receiverId : number, senderId : number) {
        this.type = type;
        this.message = message;
        this.senderId = senderId;
    }
}
  