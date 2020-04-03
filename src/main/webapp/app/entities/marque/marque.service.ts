import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMarque } from 'app/shared/model/marque.model';

type EntityResponseType = HttpResponse<IMarque>;
type EntityArrayResponseType = HttpResponse<IMarque[]>;

@Injectable({ providedIn: 'root' })
export class MarqueService {
  public resourceUrl = SERVER_API_URL + 'api/marques';

  constructor(protected http: HttpClient) {}

  create(marque: IMarque): Observable<EntityResponseType> {
    return this.http.post<IMarque>(this.resourceUrl, marque, { observe: 'response' });
  }

  update(marque: IMarque): Observable<EntityResponseType> {
    return this.http.put<IMarque>(this.resourceUrl, marque, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMarque>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMarque[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
