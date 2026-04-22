@echo off
echo Starting Student Management and Fee Collection System...
echo.
echo The application will be available at:
echo - Main Application: http://localhost:8080
echo - Swagger UI: http://localhost:8080/swagger-ui.html
echo - H2 Console: http://localhost:8080/h2-console
echo.

mvnw.cmd spring-boot:run

pause
