import { INotation } from 'app/shared/model/notation.model';
import { IImage } from 'app/shared/model/image.model';
import { IMarque } from 'app/shared/model/marque.model';
import { ICatalogue } from 'app/shared/model/catalogue.model';
import { ICommande } from 'app/shared/model/commande.model';
import { IFacture } from 'app/shared/model/facture.model';

export interface IProduit {
  id?: number;
  nom?: string;
  prixVenteHtva?: number;
  prixVenteTvac?: number;
  prixAchatHt?: number;
  tauxTvaAchat?: number;
  tauxTva?: number;
  observation?: string;
  description?: string;
  notations?: INotation[];
  images?: IImage[];
  marque?: IMarque;
  catalogues?: ICatalogue[];
  commandes?: ICommande[];
  factures?: IFacture[];
}

export class Produit implements IProduit {
  constructor(
    public id?: number,
    public nom?: string,
    public prixVenteHtva?: number,
    public prixVenteTvac?: number,
    public prixAchatHt?: number,
    public tauxTvaAchat?: number,
    public tauxTva?: number,
    public observation?: string,
    public description?: string,
    public notations?: INotation[],
    public images?: IImage[],
    public marque?: IMarque,
    public catalogues?: ICatalogue[],
    public commandes?: ICommande[],
    public factures?: IFacture[]
  ) {}
}
