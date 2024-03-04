export class ChatMessage {
    side: string;
    message : string;
    seen : boolean = false;

    constructor(side: string, message : string, seen : boolean) {
        this.side = side;
        this.message = message;
        this.seen = seen;

    }
}
  