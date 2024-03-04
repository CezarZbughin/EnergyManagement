import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { AuthDTO } from 'src/app/DTO/AuthDTO';
import { AuthResponse } from 'src/app/DTO/AuthResponse';
import { RequestService } from 'src/app/service/RequestService';
import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import { ChatMessageWebSocketDTO } from 'src/app/DTO/ChatMessageWebSocketDTO';
import { UserGetDTO } from 'src/app/DTO/UserGetDTO';
import { UserListDTO } from 'src/app/DTO/UserListDTO';
import { ChatMessage } from 'src/app/model/ChatMessage';

@Component({
  selector: 'app-root',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {
  socket = new SockJS('http://localhost:8083/mess',
  { 
    headers: {
    "Authorization": `Bearer ${sessionStorage.getItem("jwt") ?? ""}`
    }
  });
  stompClient = Stomp.over(this.socket);
  
  currentUserId = sessionStorage.getItem("userId") ?? -1;
  users : UserGetDTO[] = [];
  openedChat : UserGetDTO | null = null;
  chatMessages : ChatMessage[] = [];
  lastTypingTimestamp : number = Date.now() - 1000;
  showTypingNotification : boolean = false;
  seenMessages : boolean = false;
  messageRepo : Map<number, ChatMessage[]> = new Map<number, ChatMessage[]>();

  constructor(
    private requestService : RequestService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  
  ngOnInit(): void {
    var that = this;

    this.requestService.getAllUsers("", "").subscribe({
      complete: () => {},
      error: (error) => {
        console.log(error)
      },
      next: (response : UserListDTO) => {
        this.users = response.users.filter( item => item.id != this.currentUserId);
        if(this.users.length > 0){
          //this.openedChat = this.users[0];
        }
      }
    });

    this.stompClient.connect({}, function (frame : any) {
      that.stompClient.subscribe('/topic/chat/' + sessionStorage.getItem("userId"), 
        function (msgOut) {
            console.log("HOLY SHITTY CHATY CHATY");
          
            const uint8Array = new Uint8Array(msgOut.binaryBody);
            const text = new TextDecoder().decode(uint8Array);
            let chatMessage : ChatMessageWebSocketDTO = JSON.parse(text) as ChatMessageWebSocketDTO;
            
            switch(chatMessage.type) {
              case "MESSAGE":
                if(that.openedChat != null && chatMessage.senderId == that.openedChat.id) {
                  that.chatMessages.push(new ChatMessage("LEFT", chatMessage.message, false));
                  that.requestService.sendSeenNotification(that.openedChat.id).subscribe({
                    complete: () => {}, error: (error) => {}, next: (_ : any) => {}
                  });
                } else {
                  let msgs : ChatMessage[] = that.messageRepo.get(chatMessage.senderId) ?? [];
                  msgs.push(new ChatMessage("LEFT", chatMessage.message, false));
                  that.messageRepo.set(chatMessage.senderId, msgs);
                }
                break;
              case "TYPING":
                if(that.openedChat != null && chatMessage.senderId == that.openedChat.id) {
                  that.showTypingNotification = true;
                  that.lastTypingTimestamp = Date.now();
                  setTimeout(function(){
                    that.updateShowTypingNotification();
                  }, 1000);
                }
                break;
              case "SEEN":
                if(that.openedChat != null && chatMessage.senderId == that.openedChat.id) {
                  for (let e of that.chatMessages){
                    if(e.side === "RIGHT"){
                      e.seen = true;
                    }
                  }
                } else {
                  let msgs : ChatMessage[] = that.messageRepo.get(chatMessage.senderId) ?? [];
                  for (let e of msgs){
                    if(e.side === "RIGHT"){
                      e.seen = true;
                    }
                  }
                  that.messageRepo.set(chatMessage.senderId, msgs);
                }
                break;
            }
            
          }
      );
      }
    );
  }//init

  onUserClick(user : UserGetDTO): void {
    if(this.openedChat != null) {
      this.messageRepo.set(this.openedChat.id, this.chatMessages);
    }
    this.chatMessages = this.messageRepo.get(user.id) ?? [];

    this.lastTypingTimestamp = Date.now() - 10000;
    this.showTypingNotification = false;
    this.openedChat = user;
    this.requestService.sendSeenNotification(this.openedChat.id).subscribe({
      complete: () => {},
      error: (error) => {
        console.log(error)
      },
      next: (_ : any) => {}
    });
  }

  onSendClick(text : string){
    if(this.openedChat == null) return;
    this.requestService.sendChatMessage(text, this.openedChat.id ).subscribe({
      complete: () => {},
      error: (error) => {
        console.log(error)
      },
      next: (_ : any) => {
        this.chatMessages.push(new ChatMessage("RIGHT", text, false));
      }
    });
  }
  
  onTyping() {
    if(this.openedChat == null) return;
    if(Date.now() - this.lastTypingTimestamp < 500) return;

    this.requestService.sendTypingNotification(this.openedChat.id).subscribe({
      complete: () => {},
      error: (error) => {
        console.log(error)
      },
      next: (_ : any) => {}
    });
  }

  updateShowTypingNotification() : void {
    this.showTypingNotification = Date.now() - this.lastTypingTimestamp < 700;
  }
  
   
}//class
