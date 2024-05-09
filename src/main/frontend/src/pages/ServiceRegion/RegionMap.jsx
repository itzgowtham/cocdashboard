import React from "react";
import { geograpyJSON } from "../../constants/GeographyConstants.jsx";
import { ComposableMap, Geographies, Geography } from "react-simple-maps";
import "./RegionMap.css";
import { color } from "chart.js/helpers";
import { handleValueinDollar } from "../../utilities/FormatUtilities";

const RegionMap = (props) => {
  const geoUrl = geograpyJSON;
  const { selectedViewType, data } = props;
  const minValue = "$0.00";
  let maxValue = 390;
  let insufficientData = Object.keys(data).length > 55 ? false : true;

  for (let item in data) {
    if (data[item].difference > maxValue) {
      maxValue = data[item].difference;
    }
  }

  maxValue = handleValueinDollar({ value: maxValue });

  const colors =
    selectedViewType === "Expense PMPM"
      ? {
          3: "#007680",
          2: "#35939D",
          1: "#56B0BA",
          0: "#E6F4F1",
        }
      : {
          1: "#6FC2B4",
          3: "#8bcec2",
          0: "#ebf7f5",
          2: "#cfebe6",
        };

  const colorPalette =
    selectedViewType === "Expense PMPM"
      ? "expense-color-palette"
      : "member-color-palette";
  const labelColor =
    selectedViewType === "Expense PMPM" ? "#007680" : "#6FC2B4";

  const regions = [
    "Alabama",
    "Alaska",
    "Arizona",
    "Colorado",
    "Florida",
    "Georgia",
    "Indiana",
    "Kansas",
    "Maine",
    "Massachusetts",
    "Minnesota",
    "New Jersey",
    "North Carolina",
    "North Dakota",
    "Oklahoma",
    "Pennsylvania",
    "South Dakota",
    "Texas",
    "Wyoming",
    "Connecticut",
    " Missouri",
    "West Virginia",
    "Illinois",
    "New Mexico",
    "Arkansas",
    "California",
    "Delaware",
    "District of Columbia",
    "Hawaii",
    "Iowa",
    "Kentucky",
    "Maryland",
    "Michigan",
    "Mississippi",
    "Montana",
    "New Hampshire",
    "New York",
    "Ohio",
    "Oregon",
    "Tennessee",
    "Utah",
    "Virginia",
    "Washington",
    "Wisconsin",
    "American Samoa",
    "Guam",
    "Commonwealth of the Northern Mariana Islands",
    "Nebraska",
    "South Carolina",
    " Puerto Rico",
    "United States Virgin Islands",
    "Idaho",
    "Nevada",
    "Vermont",
    "Louisiana",
    "Rhode Island",
  ];

  const countryFormatting = {
    Alabama: "AL",
    Alaska: "AK",
    Arizona: "AZ",
    Colorado: "CO",
    Florida: "FL",
    Georgia: "GA",
    Indiana: "IN",
    Kansas: "KS",
    Maine: "ME",
    Massachusetts: "MA",
    Minnesota: "MN",
    "New Jersey": "NJ",
    "North Carolina": "NC",
    "North Dakota": "ND",
    Oklahoma: "OK",
    Pennsylvania: "PA",
    "South Dakota": "SD",
    Texas: "TX",
    Wyoming: "WY",
    Connecticut: "CT",
    Missouri: "MO",
    "West Virginia": "WV",
    Illinois: "IL",
    "New Mexico": "NM",
    Arkansas: "AR",
    California: "CA",
    Delaware: "DE",
    "District of Columbia": "DC",
    Hawaii: "HI",
    Iowa: "IA",
    Kentucky: "KY",
    Maryland: "MD",
    Michigan: "MI",
    Mississippi: "MS",
    Montana: "MT",
    "New Hampshire": "NH",
    "New York": "NY",
    Ohio: "OH",
    Oregon: "OR",
    Tennessee: "TN",
    Utah: "UT",
    Virginia: "VA",
    Washington: "WA",
    Wisconsin: "WI",
    "Puerto Rico": "PR",
    "United States Virgin Islands": "VI",
    "American Samoa": "AS",
    Nebraska: "NE",
    "South Carolina": "SC",
    Guam: "GU",
    "Commonwealth of the Northern Mariana Islands": "MP",
    Idaho: "ID",
    Nevada: "NV",
    Vermont: "VT",
    Louisiana: "LA",
    "Rhode Island": "RI",
  };

  return (
    <div className="p-3" style={{ width: "733px", height: "427px" }}>
      <ComposableMap projection="geoAlbers">
        <Geographies geography={geoUrl}>
          {({ geographies }) =>
            geographies.map((geo) => {
              const name = countryFormatting[geo.properties.name];
              const stateData = data[name];
              let colorCode;
              if (stateData) {
                if (stateData.difference > 300) {
                  colorCode = colors[3];
                } else if (stateData.difference > 100) {
                  colorCode = colors[2];
                } else if (stateData.difference > 0) {
                  colorCode = colors[1];
                } else {
                  colorCode = colors[0];
                }
              } else {
                colorCode = "#BBBCBC";
              }

              return (
                <Geography
                  key={geo.rsmKey}
                  geography={geo}
                  fill={colorCode}
                  stroke="#fff"
                />
              );
            })
          }
        </Geographies>
      </ComposableMap>
      <div className="flex-sm-row" style={{ width: "180px" }}>
        {insufficientData && (
          <div>
            <span
              className="label-color"
              style={{ backgroundColor: "#BBBCBC" }}
            ></span>
            <span className="label">Insufficent Data</span>
          </div>
        )}

        <div>
          <span
            className="label-color"
            style={{ backgroundColor: labelColor }}
          ></span>
          <span className="label">{selectedViewType}</span>
        </div>

        <div className={`${colorPalette}`}></div>
        <div className="d-flex justify-content-between">
          <span className="value">{minValue}</span>
          <span className="value">{maxValue}</span>
        </div>
      </div>
    </div>
  );
};

export default RegionMap;
