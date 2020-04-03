import { IClient } from 'app/shared/model/client.model';

export interface IPoint {
  id?: number;
  nombrePoint?: number;
  observation?: string;
  client?: IClient;
}

export class Point implements IPoint {
  constructor(public id?: number, public nombrePoint?: number, public observation?: string, public client?: IClient) {}
}
