import { Moment } from 'moment';
import { IProduit } from 'app/shared/model/produit.model';
import { IClient } from 'app/shared/model/client.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IFacture {
  id?: number;
  date?: Moment;
  numero?: string;
  dateLivraison?: Moment;
  lieuxLivraison?: string;
  orderStatus?: OrderStatus;
  observation?: string;
  produits?: IProduit[];
  client?: IClient;
}

export class Facture implements IFacture {
  constructor(
    public id?: number,
    public date?: Moment,
    public numero?: string,
    public dateLivraison?: Moment,
    public lieuxLivraison?: string,
    public orderStatus?: OrderStatus,
    public observation?: string,
    public produits?: IProduit[],
    public client?: IClient
  ) {}
}
