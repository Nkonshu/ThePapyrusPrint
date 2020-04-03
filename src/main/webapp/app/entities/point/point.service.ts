import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPoint } from 'app/shared/model/point.model';

type EntityResponseType = HttpResponse<IPoint>;
type EntityArrayResponseType = HttpResponse<IPoint[]>;

@Injectable({ providedIn: 'root' })
export class PointService {
  public resourceUrl = SERVER_API_URL + 'api/points';

  constructor(protected http: HttpClient) {}

  create(point: IPoint): Observable<EntityResponseType> {
    return this.http.post<IPoint>(this.resourceUrl, point, { observe: 'response' });
  }

  update(point: IPoint): Observable<EntityResponseType> {
    return this.http.put<IPoint>(this.resourceUrl, point, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
