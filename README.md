# �������� ������� ��� �������� ������ Neoflex "����������� ���������"

## ����������� �� SpringBoot + Java 11 c ����� API: GET "/calculate"

### ����������� ����������:
���������� ��������� ���� ������� �������� �� 12 ������� � ���������� ���� ������� - 
�������� ������ ���������, ������� ������ ����������.

### ���. �������:
��� ������� ����� ����� ������� ������ ��� ����� � ������, ����� ������ ����������� 
������� ��������� � ������ ���������� � ��������.

### ������������ ����������: 
- Java 11;
- Gradle (Groovy);
- Spring Boot;
- Junit (��� ������������).

### ������ �������:
- ��� ���������� ������� ��������� �������: `./gradlew clean build`;
- ��� ������� ������� ��������� �������: `./gradlew bootRun`.

### ������� ������������� ���������
��� ������� ��������� ��� ����� �������� � ����������: 
<localhost:8080/api/vacations/calculate?salaryPerYear=x&vacationDays=y>, 
��� x - ������� ��������, y - ���������� ���� �������.

��� ������� ��������� � ������ �������� � ����������:
<localhost:8080/api/vacations/calculate?salaryPerYear=x&startDate=a&endDate=b>, 
��� a � b - ���� ������ � ����� �������, ��������������, � ������� (YYYY-MM-dd).

***������������ ������������� � ������� Postman � Junit.***
