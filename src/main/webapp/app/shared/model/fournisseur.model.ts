import { ICommande } from 'app/shared/model/commande.model';
import { ICatalogue } from 'app/shared/model/catalogue.model';

export interface IFournisseur {
  id?: number;
  nom?: string;
  ville?: string;
  pays?: string;
  telephone?: string;
  email?: string;
  commandes?: ICommande[];
  catalogues?: ICatalogue[];
}

export class Fournisseur implements IFournisseur {
  constructor(
    public id?: number,
    public nom?: string,
    public ville?: string,
    public pays?: string,
    public telephone?: string,
    public email?: string,
    public commandes?: ICommande[],
    public catalogues?: ICatalogue[]
  ) {}
}
