CREATE TABLE habitant (
  tenant_id VARCHAR(36) NOT NULL,
  id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  gender VARCHAR(06) NOT NULL,
  PRIMARY KEY (tenant_id, id));

CREATE VIEW habitant_TATOOINE AS SELECT * FROM habitant WHERE tenant_id = 'Tatooine';
CREATE VIEW habitant_ALDERAAN AS SELECT * FROM habitant WHERE tenant_id = 'Alderaan';
CREATE VIEW habitant_BESPIN   AS SELECT * FROM habitant WHERE tenant_id = 'Bespin'  ;