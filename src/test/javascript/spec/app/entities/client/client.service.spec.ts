import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { ClientService } from 'app/entities/client/client.service';
import { IClient, Client } from 'app/shared/model/client.model';
import { Civilite } from 'app/shared/model/enumerations/civilite.model';
import { Sexe } from 'app/shared/model/enumerations/sexe.model';

describe('Service Tests', () => {
  describe('Client Service', () => {
    let injector: TestBed;
    let service: ClientService;
    let httpMock: HttpTestingController;
    let elemDefault: IClient;
    let expectedResult: IClient | IClient[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ClientService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Client(
        0,
        Civilite.Mr,
        'AAAAAAA',
        'AAAAAAA',
        Sexe.M,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateNaissance: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Client', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateNaissance: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate
          },
          returnedFromService
        );

        service.create(new Client()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Client', () => {
        const returnedFromService = Object.assign(
          {
            civilite: 'BBBBBB',
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            sexe: 'BBBBBB',
            dateNaissance: currentDate.format(DATE_FORMAT),
            telephone: 'BBBBBB',
            ville: 'BBBBBB',
            codePostal: 'BBBBBB',
            pays: 'BBBBBB',
            quartier: 'BBBBBB',
            email: 'BBBBBB',
            observation: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Client', () => {
        const returnedFromService = Object.assign(
          {
            civilite: 'BBBBBB',
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            sexe: 'BBBBBB',
            dateNaissance: currentDate.format(DATE_FORMAT),
            telephone: 'BBBBBB',
            ville: 'BBBBBB',
            codePostal: 'BBBBBB',
            pays: 'BBBBBB',
            quartier: 'BBBBBB',
            email: 'BBBBBB',
            observation: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Client', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
