import { IProduit } from 'app/shared/model/produit.model';

export interface INotation {
  id?: number;
  note?: number;
  observation?: string;
  produits?: IProduit[];
}

export class Notation implements INotation {
  constructor(public id?: number, public note?: number, public observation?: string, public produits?: IProduit[]) {}
}
