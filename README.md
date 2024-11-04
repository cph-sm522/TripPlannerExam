# Trip Planning App Exam Project

# Endpoints dokumentation

- GET all trips // GET{{URL}}

    -
        {
        "id": 1,
        "starttime": [
        2024,
        11,
        4,
        10,
        49,
        13,
        704565000
        ],
        "endtime": [
        2024,
        11,
        4,
        12,
        49,
        13,
        704607000
        ],
        "startposition": "Beach Point",
        "name": "Beach Fun",
        "price": 49.99,
        "category": "BEACH",
        "guideId": 1
        }....


- GET trips by ID // GET{{URL}}/1
  -
        {
        "id": 3,
        "starttime": [
        2024,
        11,
        4,
        10,
        49,
        13,
        704631000
        ],
        "endtime": [
        2024,
        11,
        4,
        13,
        49,
        13,
        704634000
        ],
        "startposition": "City Center",
        "name": "City Tour",
        "price": 59.99,
        "category": "CITY",
        "guideId": 2
        }

    - CREATE trip // POST{{URL}}
      -
          {
          "id": 27,
          "starttime": [
          2024,
          12,
          1,
          10,
          0
          ],
          "endtime": [
          2024,
          12,
          1,
          12,
          0
          ],
          "startposition": "Beach Point",
          "name": "Beach Adventure",
          "price": 100.0,
          "category": "BEACH",
          "guideId": null
          }
- UPDATE trip - PUT{{URL}}/27
-
      - {
        "id": 27,
        "starttime": [
        2024,
        12,
        1,
        10,
        0
        ],
        "endtime": [
        2024,
        12,
        1,
        12,
        0
        ],
        "startposition": "Beach Point",
        "name": "Updated Beach Adventure",
        "price": 120.0,
        "category": "BEACH",
        "guideId": null
        }
- DELETE trip - DELETE{{URL}}/27

        <Response body is empty>

  - ADD guide to trip - PUT{{URL}}/27/guides/2
  - 
                    {
                    "id": 27,
                    "starttime": [
                    2024,
                    12,
                    1,
                    10,
                    0
                    ],
                    "endtime": [
                    2024,
                    12,
                    1,
                    12,
                    0
                    ],
                    "startposition": "Beach Point",
                    "name": "Updated Beach Adventure",
                    "price": 120.0,
                    "category": "BEACH",
                    "guideId": 2
                    }
  - 