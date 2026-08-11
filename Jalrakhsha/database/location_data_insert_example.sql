-- Use REAL verified environmental values for production/demo data.
-- Example structure only:
INSERT INTO "local_data"
(state, district, location_name, latitude, longitude, annual_rainfall,
 ground_water_depth, aquifer_type, soil_type, recharge_potential)
VALUES
('West Bengal', 'Howrah', 'Domjur', 22.6490, 88.2190, 1600,
 8.5, 'Unconfined', 'Alluvial', 'High');
