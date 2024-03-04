export class ChatMessageRequestDTO {
    message : string = "";
    receiverId : number = 0;

    constructor(message : string, receiverId : number) {
        this.message = message;
        this.receiverId = receiverId;
    }
}
  