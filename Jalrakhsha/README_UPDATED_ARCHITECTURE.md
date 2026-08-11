# AquaSave GIS - Updated Architecture

## Core rule
1. Frontend sends ONLY user assessment inputs + selected `locationId`.
2. `LocationData` contains environmental data for each stored location.
3. Backend fetches `LocationData` using `locationId`.
4. All calculations and feasibility scoring happen in Spring Boot.
5. Assessment inputs and backend calculation results are persisted.
6. Complete report response contains environmental data, every calculation, every feasibility component, recommendation and economic feasibility.

## User assessment JSON
```json
{
  "userName": "your_username",
  "locationId": 1,
  "roofArea": 120,
  "roofType": "Concrete",
  "dwellers": 4,
  "openSpace": 50
}
```

## Environmental LocationData JSON
Use REAL environmental data for your project; do not treat these as user assessment inputs.

```json
{
  "state": "West Bengal",
  "district": "Howrah",
  "locationName": "Domjur",
  "latitude": 22.6490,
  "longitude": 88.2190,
  "annualRainfall": 1600,
  "groundWaterDepth": 8.5,
  "aquiferType": "Unconfined",
  "soilType": "Alluvial",
  "rechargePotential": "High"
}
```

Insert environmental records through `POST /locations` or your database/admin process.

## GIS flow
- `GET /locations` loads stored assessment locations.
- `GET /locations/nearest?latitude=...&longitude=...` selects the nearest stored location when GPS is used.
- The selected `locationId` is submitted with the assessment.
- Backend fetches the corresponding environmental record.

## Assessment API
`POST /assessments/generate`

The backend saves user inputs, fetches environmental data, calculates water balance, RTRWH feasibility, recharge feasibility, overall feasibility, recommendation and economic values.

## Report API
- `POST /report/{assessmentId}/report?language=en` records report generation.
- `GET /report/{assessmentId}` returns the complete report data including every feasibility component.

## Important
If `LocationData` is empty, GPS can still obtain coordinates, but the system cannot calculate an environmental-data-based assessment until real `LocationData` records are inserted.
