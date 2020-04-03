import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { INotation } from 'app/shared/model/notation.model';

type EntityResponseType = HttpResponse<INotation>;
type EntityArrayResponseType = HttpResponse<INotation[]>;

@Injectable({ providedIn: 'root' })
export class NotationService {
  public resourceUrl = SERVER_API_URL + 'api/notations';

  constructor(protected http: HttpClient) {}

  create(notation: INotation): Observable<EntityResponseType> {
    return this.http.post<INotation>(this.resourceUrl, notation, { observe: 'response' });
  }

  update(notation: INotation): Observable<EntityResponseType> {
    return this.http.put<INotation>(this.resourceUrl, notation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INotation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INotation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
