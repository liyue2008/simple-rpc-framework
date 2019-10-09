CREATE TABLE IF NOT EXISTS rpc_name_service (
                              service_name VARCHAR(255) NOT NULL,
                              uri VARCHAR(255) NOT NULL,
                              PRIMARY KEY (service_name, uri)
                           );