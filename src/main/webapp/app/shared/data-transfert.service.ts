import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable({ providedIn: 'root' })
export class DataTransfertService {
  private messageSource = new BehaviorSubject<string>('marche');
  public currentMessage = this.messageSource.asObservable();

  constructor() {}

  changeMessage(message: string): any {
    this.messageSource.next(message);
  }
}
