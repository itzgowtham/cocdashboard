import React, { useState } from "react";
import { geograpyJSON } from "../../constants/GeographyConstants.jsx";
import { ComposableMap, Geographies, Geography } from "react-simple-maps";
import "./RegionMap.css";
import { handleValueinDollar } from "../../utilities/FormatUtilities";
import { Tooltip } from "react-tooltip";

const RegionMap = (props) => {
  const geoUrl = geograpyJSON;
  const { selectedViewType, data, dataType } = props;
  let minValue = 0;
  let maxValue = 390;
  let insufficientData = Object.keys(data).length > 55 ? false : true;

  for (let item in data) {
    if (data[item].actual > maxValue && data[item].name != "others") {
      maxValue = data[item].actual;
    }
    if (data[item].actual < minValue && data[item].name != "others") {
      minValue = data[item].actual;
    }
  }

  const colors =
    selectedViewType === "Expense PMPM"
      ? {
          3: "#007680",
          2: "#35939D",
          1: "#56B0BA",
          0: "#E6F4F1",
        }
      : {
          2: "#86d7ca",
          3: "#53C7B4",
          0: "#ebf7f5",
          1: "#a9e3d9",
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

  const [name, setName] = useState("");
  const [actual, setActual] = useState("");
  const [target, setTarget] = useState("");
  const top = maxValue * 0.7;
  const mid = maxValue * 0.5;
  const below = maxValue * 0.25;
  maxValue =
    selectedViewType === "Expense PMPM"
      ? handleValueinDollar({ value: maxValue })
      : maxValue;
  minValue =
    selectedViewType === "Expense PMPM"
      ? handleValueinDollar({ value: minValue })
      : minValue;

  return (
    <div className="p-3" style={{ width: "733px", height: "427px" }}>
      <ComposableMap projection="geoAlbers">
        <Geographies geography={geoUrl}>
          {({ geographies }) =>
            geographies.map((geo) => {
              const name = countryFormatting[geo.properties.name];
              const stateData = data[name];
              let actualData = "";
              let colorCode;
              if (stateData) {
                actualData = stateData.actual;
                if (actualData > top) {
                  colorCode = colors[3];
                } else if (actualData > mid) {
                  colorCode = colors[2];
                } else if (actualData > below) {
                  colorCode = colors[1];
                } else {
                  colorCode = colors[0];
                }
              } else {
                colorCode = "#BBBCBC";
              }

              return (
                  <Geography
                    className="my-anchor-element"
                    key={geo.rsmKey}
                    geography={geo}
                    fill={colorCode}
                    stroke="#fff"
                    onMouseOver={() => {
                      setName(name);
                      actualData !== ""
                        ? setActual(actualData)
                        : setActual("No Data");
                      stateData?.target
                        ? setTarget(stateData.target)
                        : setTarget("No Data");
                    }}
                  ></Geography>
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
        <Tooltip
          anchorSelect=".my-anchor-element"
          style={{ overflow: "visible" }}
        >
          <p className="m-0">{`Region : ${name}   `}</p>
          <p className="m-0">{`${dataType[0]}: ${actual}`}</p>
          <p className="m-0">{`${dataType[1]}: ${target}`}</p>
        </Tooltip>
      </div>
    </div>
  );
};

export default RegionMap;
