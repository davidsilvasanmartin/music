import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Employee } from './employee';
import { EmployeeService } from './employee.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  employees$: Observable<Employee[]>;

  constructor(private readonly _employeeService: EmployeeService) {
    this.employees$ = this._employeeService.getEmployees();
  }
}
