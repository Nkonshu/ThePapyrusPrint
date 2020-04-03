import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICatalogue } from 'app/shared/model/catalogue.model';

type EntityResponseType = HttpResponse<ICatalogue>;
type EntityArrayResponseType = HttpResponse<ICatalogue[]>;

@Injectable({ providedIn: 'root' })
export class CatalogueService {
  public resourceUrl = SERVER_API_URL + 'api/catalogues';

  constructor(protected http: HttpClient) {}

  create(catalogue: ICatalogue): Observable<EntityResponseType> {
    return this.http.post<ICatalogue>(this.resourceUrl, catalogue, { observe: 'response' });
  }

  update(catalogue: ICatalogue): Observable<EntityResponseType> {
    return this.http.put<ICatalogue>(this.resourceUrl, catalogue, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
