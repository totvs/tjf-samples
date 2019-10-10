CREATE TABLE habitant (
  tenant_id VARCHAR(36) NOT NULL,
  id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  gender VARCHAR(06) NOT NULL,
  PRIMARY KEY (tenant_id, id));

CREATE VIEW habitant_tatooine AS SELECT * FROM habitant WHERE tenant_id = 'tatooine';
CREATE VIEW habitant_alderaan AS SELECT * FROM habitant WHERE tenant_id = 'alderaan';
CREATE VIEW habitant_bespin   AS SELECT * FROM habitant WHERE tenant_id = 'bespin'  ;  