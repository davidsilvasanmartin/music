import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { environment } from '../environments/environment';
import { Employee } from './employee';

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  private readonly _url = environment.apiUrl;

  constructor(private readonly _http: HttpClient) {}

  getEmployees(): Observable<Employee[]> {
    return this._http.get<Employee[]>(`${this._url}/employees`);
  }
}
