MERGE INTO rpc_name_service R
  USING (VALUES ?, ?) I (service_name, uri)
  ON (R.service_name = I.service_name AND R.uri = I.uri)
  WHEN NOT MATCHED THEN INSERT (service_name, uri) VALUES (I.service_name, I.uri)