- OkHttp to Fetch Website DOM
- Ksoup to Parse HTML
- Ktorm to Handle Database
- HikariCP to Handle Connection Pool

TODO: Aprimorar velocidade do script.
TODO: Criar Transaction no DB para evitar inconsistencia de dados.
TODO: Add FEAT de loading para calcular tempo ou alguma lib de console com loading.

### MYSQL DATABASE TBCA

```sql
CREATE DATABASE tbca;
USE tbca;

CREATE TABLE foods (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    food_unique_code VARCHAR(10) UNIQUE NOT NULL,
    portuguese_name VARCHAR(30) NOT NULL,
    scientific_name VARCHAR(255) NOT NULL,
    group_name VARCHAR(50) NOT NULL,
    brand VARCHAR(50)
);

CREATE TABLE nutrients (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    food_unique_code VARCHAR(10) NOT NULL,
    n_component VARCHAR(100) NOT NULL,
    unity VARCHAR(10) NOT NULL,
    value_by_hundred_grams DOUBLE NOT NULL,
    default_deviation VARCHAR(50),
    min_value DOUBLE,
    max_value DOUBLE,
    used_data_value INT,
    n_references VARCHAR(255),
    data_type VARCHAR(50) NOT NULL,
    
     FOREIGN KEY (`food_unique_code`) REFERENCES foods(`food_unique_code`)
		ON DELETE CASCADE
);
```