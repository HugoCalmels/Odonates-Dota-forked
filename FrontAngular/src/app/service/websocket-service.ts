import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import {environment} from "../../environment/environment";
const backendUrl = environment.backendUrl;
@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  private client: Client;
  private connected: boolean = false;
  private pendingSubscriptions: Array<{ roomId: string, callback: (message: IMessage) => void }> = [];
  private subscriptions: Map<string, StompSubscription> = new Map();
  private isSubToARoom: boolean = false;

  constructor() {
    this.client = new Client({
      webSocketFactory: () => new SockJS(`${backendUrl}/ws`),
      onConnect: () => {
        console.log('Connected!');
        this.connected = true;
        this.pendingSubscriptions.forEach(sub => this.subscribeToRoom(sub.roomId, sub.callback));
        this.pendingSubscriptions = [];
      },
      onDisconnect: (receipt) => {
        console.log('Disconnected!', receipt);
        this.connected = false;
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      }
    });
  }

  activate() {
    try {
      this.client.activate();
    } catch (error) {
      console.error('Error during WebSocket activation:', error);
    }
  }

  subscribeToRoom(roomId: string, callback: (message: IMessage) => void) {
    if (this.connected) {
      if (!this.isSubToARoom) {
        const subscription = this.client.subscribe(`/topic/rooms/${roomId}`, callback);
        this.subscriptions.set(roomId, subscription);
        this.isSubToARoom = true;
      }
    } else {
      this.pendingSubscriptions.push({ roomId, callback });
      if (!this.client.active) {
        this.activate();
      }
    }
  }

  unsubscribeFromRoom(roomId: string) {
    const subscription = this.subscriptions.get(roomId);
    if (subscription) {
      subscription.unsubscribe();
      this.subscriptions.delete(roomId);
      this.isSubToARoom = false;
      console.log(`Unsubscribed from room ${roomId}`);
    } else {
      console.warn(`No subscription found for room ${roomId}`);
    }
  }

  sendMessage(roomId: string, message: string) {
    if (this.client.active) {
      this.client.publish({ destination: `/app/room/${roomId}/chat`, body: message });
    } else {
      console.error('Client is not active. Unable to send message.');
    }
  }

  deactivate() {
    if (this.client.active) {
      this.subscriptions.forEach((subscription, roomId) => {
        subscription.unsubscribe();
        console.log(`Unsubscribed from room ${roomId}`);
      });
      this.subscriptions.clear();
      this.client.deactivate().then(r => {
        this.isSubToARoom = false;
        this.connected = false;
        console.log("Client deactivated");
      });
    }
  }
  
}
