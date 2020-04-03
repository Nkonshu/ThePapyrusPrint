import { IProduit } from 'app/shared/model/produit.model';

export interface IImage {
  id?: number;
  path?: string;
  observation?: string;
  produits?: IProduit[];
}

export class Image implements IImage {
  constructor(public id?: number, public path?: string, public observation?: string, public produits?: IProduit[]) {}
}
