## Introduction

This API provides endpoints for managing various entities within a Payroll Management System (PMS). The entities include Allowances, Bank Details, Branches, Contact Information, Deductions, Departments, Employees, Exports, Notifications, Salaries, System Users, and Taxes. Each controller corresponds to a specific entity and provides RESTful endpoints for CRUD operations and other specific actions.

## Related Repositories

This project also includes a [React client application](https://github.com/username/employee-management-client) that interacts with the API.


## Controllers and Endpoints

### 1. **AllowanceController**
- **Base URL:** `/allowances`
- **Endpoints:**
  - `GET /allowances`: Retrieves a paginated list of all allowances.
    - **Parameters:**
      - `page` (default: `0`)
      - `size` (default: `10`)

### 2. **BankDetailsController**
- **Base URL:** `/bankdetails`
- **Endpoints:**
  - `GET /bankdetails`: Retrieves a paginated list of all bank details.
    - **Parameters:**
      - `page` (default: `0`)
      - `size` (default: `10`)

### 3. **BranchController**
- **Base URL:** `/branches`
- **Endpoints:**
  - `GET /branches`: Retrieves a paginated list of all branches.
    - **Parameters:**
      - `page` (default: `0`)
      - `size` (default: `10`)

### 4. **ContactInfoController**
- **Base URL:** `/contactinfo`
- **Endpoints:**
  - `GET /contactinfo`: Retrieves a paginated list of all contact information.
    - **Parameters:**
      - `page` (default: `0`)
      - `size` (default: `10`)

### 5. **DeductionController**
- **Base URL:** `/deductions`
- **Endpoints:**
  - `GET /deductions`: Retrieves a paginated list of all deductions.
    - **Parameters:**
      - `page` (default: `0`)
      - `size` (default: `10`)

### 6. **DepartmentController**
- **Base URL:** `/departments`
- **Endpoints:**
  - `GET /departments`: Retrieves a paginated list of all departments.
    - **Parameters:**
      - `page` (default: `0`)
      - `size` (default: `10`)

### 7. **EmployeeController**
- **Base URL:** `/employees`
- **Endpoints:**
  - `GET /employees`: Retrieves a paginated list of all employees.
    - **Parameters:**
      - `page` (default: `1`)
      - `size` (default: `10`)
  - `GET /employees/new-by-department`: Retrieves new employees grouped by department within a specific date range.
    - **Parameters:**
      - `startDate` (optional, default: first day of the previous month)
      - `endDate` (optional, default: today)
      - `page` (default: `1`)
      - `size` (default: `10`)
  - `GET /employees/count-active/{departmentId}`: Counts the active employees in a specific department.
  - `POST /employees/add-employee`: Adds a new employee.
  - `PUT /employees/{employeeId}`: Updates an existing employee.
  - `DELETE /employees/{employeeId}`: Deletes an employee.
  - `POST /employees/search`: Searches for employees by a search term.
    - **Parameters:**
      - `searchTerm`
      - `page`
      - `size`

### 8. **ExportController**
- **Base URL:** `/api/export`
- **Endpoints:**
  - `POST /api/export/{tableName}`: Initiates an export job for a specific table.
  - `POST /api/exportSearch/{tableName}`: Initiates an export job for search results within a table.
    - **Parameters:**
      - `searchTerm`
  - `GET /api/download/{fileId}`: Downloads an exported file.
  - `GET /api/listExports`: Lists all export jobs.
    - **Parameters:**
      - `page`
      - `size`

### 9. **NotificationController**
- **Base URL:** `/api/notifications`
- **Endpoints:**
  - `GET /api/notifications`: Establishes a connection to receive real-time notifications via Server-Sent Events (SSE).
    - **Parameters:**
      - `clientId`: Unique identifier for the client connection.

### 10. **SalaryController**
- **Base URL:** `/salaries`
- **Endpoints:**
  - `GET /salaries/earnings-deductions/{employeeId}`: Retrieves earnings and deductions for a specific employee.
  - `GET /salaries/allowances-net-salaries/{departmentId}`: Retrieves total allowances and net salaries by department.
  - `GET /salaries/total-net-salary`: Retrieves the total net salary to be paid.
  - `GET /salaries/payment-history/{employeeId}`: Retrieves the payment history of a specific employee.
  - `GET /salaries`: Retrieves a paginated list of all salaries.
    - **Parameters:**
      - `page` (default: `1`)
      - `size` (default: `10`)
  - `GET /salaries/count`: Counts the total number of salaries.

### 11. **SystemUserController**
- **Base URL:** `/systemusers`
- **Endpoints:**
  - `GET /systemusers`: Retrieves a paginated list of all system users.
    - **Parameters:**
      - `page` (default: `0`)
      - `size` (default: `10`)
  - `POST /systemusers/auth`: Authenticates a system user and generates a JWT token.

### 12. **TaxController**
- **Base URL:** `/taxes`
- **Endpoints:**
  - `GET /taxes`: Retrieves a paginated list of all taxes.
    - **Parameters:**
      - `page` (default: `0`)
      - `size` (default: `10`)

## Usage

- Each endpoint follows RESTful conventions.
- Data is returned in JSON format, and pagination is managed via query parameters.
- Authentication and security measures should be applied where necessary.

## Authentication

- The `/systemusers/auth` endpoint provides a method to authenticate users using a JWT-based mechanism.

## Error Handling

- Standard HTTP status codes are used to indicate the success or failure of API requests.
- Detailed error messages are provided in the response body in case of failures.
