import { IProduit } from 'app/shared/model/produit.model';

export interface IStock {
  id?: number;
  stockMax?: string;
  stockMin?: string;
  quantiteProduit?: string;
  observation?: string;
  produit?: IProduit;
}

export class Stock implements IStock {
  constructor(
    public id?: number,
    public stockMax?: string,
    public stockMin?: string,
    public quantiteProduit?: string,
    public observation?: string,
    public produit?: IProduit
  ) {}
}
