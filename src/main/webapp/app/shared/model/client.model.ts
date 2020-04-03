import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IFacture } from 'app/shared/model/facture.model';
import { Civilite } from 'app/shared/model/enumerations/civilite.model';
import { Sexe } from 'app/shared/model/enumerations/sexe.model';

export interface IClient {
  id?: number;
  civilite?: Civilite;
  nom?: string;
  prenom?: string;
  sexe?: Sexe;
  dateNaissance?: Moment;
  telephone?: string;
  ville?: string;
  codePostal?: string;
  pays?: string;
  quartier?: string;
  email?: string;
  observation?: string;
  user?: IUser;
  image?: IClient;
  factures?: IFacture[];
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public civilite?: Civilite,
    public nom?: string,
    public prenom?: string,
    public sexe?: Sexe,
    public dateNaissance?: Moment,
    public telephone?: string,
    public ville?: string,
    public codePostal?: string,
    public pays?: string,
    public quartier?: string,
    public email?: string,
    public observation?: string,
    public user?: IUser,
    public image?: IClient,
    public factures?: IFacture[]
  ) {}
}
