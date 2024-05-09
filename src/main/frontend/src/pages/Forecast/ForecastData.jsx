import React, { useState } from "react";
import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import HorizontalBarChart from "../../components/HorizontalBarChart";
import {
  handleDifferenceinPercentage,
  handleValueinDollar,
} from "../../utilities/FormatUtilities";
import LineChart from "../../components/LineChart";

const ForecastData = (props) => {
  const { rowData, showGraph, selectedOption } = props;

  //once api is ready, replace dummy data with api data

  //once api is ready, replace dummy data with api data
  const handleGraph = () => {
    return (
      <div>
        <HorizontalBarChart
          actualvalue={12000}
          targetvalue={140000}
          type={"number"}
          graphtype={{ type1: "Target", type2: "Actual" }}
          aspectRatio={5}
          graphLength={2}
        />
      </div>
    );
  };

  const colDefs = [
    {
      field: "period",
      headerName: "Period",
      flex: 1,
    },
    {
      field: "forecast_value",
      headerName: `Forecasted Value (${
        selectedOption === "Cost" ? "PMPM" : "Members"
      })`,
      flex: 1,
      //   hide: showGraph,
      cellRenderer: (params) => {
        return selectedOption === "Cost"
          ? handleValueinDollar(params)
          : params.value;
      },
    },
    {
      field: "confidence_interval",
      headerName: `Confidence Interval (${
        selectedOption === "Cost" ? "PMPM" : "Members"
      })`,
      flex: 1,
      //   hide: showGraph,
      cellRenderer: (params) => {
        return selectedOption === "Cost"
          ? `+/- ${handleValueinDollar(params)}`
          : `+/- ${params.value}`;
      },
    },
  ];

  const data = {
    labels: rowData.map((data) => data.period),
    datasets: [
      {
        type: "scatter",
        label: "Test Data Points",
        data: [
          {
            x: "Jul 2023",
            y: 130,
          },

          { x: "Aug 2023", y: 710 },
          { x: "Sep 2023", y: 280 },
          { x: "Oct 2023", y: 80 },
        ],
        backgroundColor: "#F39000",
      },
      {
        type: "scatter",
        label: "Observed Data Points",
        data: [
          {
            x: "Jan 2023",
            y: 120,
          },

          { x: "Feb 2023", y: 580 },
          { x: "Apr 2023", y: 220 },
          { x: "Mar 2023", y: 130 },
          { x: "May 2023", y: 875 },
          { x: "Jun 2023", y: 140 },
        ],
        backgroundColor: "#004F59",
      },
      {
        label: "Forecast",
        data: rowData.map((data) => data.forecast_value),
        fill: false,
        pointRadius: 0,
        borderWidth: 2,
        tension: 0.2,
        //backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "#00ABAB",
      },
      {
        label: "Uncertainty Interval",
        data: rowData.map((data) => data.forecast_value),
        borderWidth: 15,
        fill: false,
        pointRadius: 0,
        tension: 0.2,
        borderColor: "rgba(0, 171, 171, 0.2)",
      },
    ],
  };

  const graphType = data.datasets.map((items) => items.label);
  return (
    <>
      {showGraph ? (
        <>
          <LineChart
            labels={data.labels}
            data={data.datasets}
            selectedOption={selectedOption}
          />
        </>
      ) : (
        <div
          className={"ag-theme-quartz"}
          style={{
            height: "500px",
          }}
        >
          {showGraph ? (
            <h3>Graphical View in Progress</h3>
          ) : (
            <AgGridReact
              rowData={rowData}
              columnDefs={colDefs}
              rowHeight={46}
              pagination={true}
          paginationPageSize={7}
            />
          )}
        </div>
      )}
    </>
  );
};

export default ForecastData;
