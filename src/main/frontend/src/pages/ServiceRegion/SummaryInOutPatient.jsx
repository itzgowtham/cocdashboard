import React from "react";
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import {
  handleDifferenceinPercentage,
  handleValueinDollar,
} from "../../utilities/FormatUtilities";
import "../../App.css";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import RegionMap from "./RegionMap";

function SummaryInOutPatient(props) {
  const { showGraph, data, dataWithoutFormat, selectedViewType, dataType } =
    props;
  const rowData = [
    {
      service_area_state: "AL",
      target: "120.98",
      actual: "120.98",
      difference: "120.98",
      difference_: 4.5,
      target_actual: 4,
    },
    {
      service_area_state: "AZ",
      target: "120.98",
      actual: "120.98",
      difference: "120.98",
      difference_: 4.5,
      target_actual: 4,
    },
    {
      service_area_state: "AR",
      target: "120.98",
      actual: "120.98",
      difference: "120.98",
      difference_: 4.5,
      target_actual: 4,
    },
    {
      service_area_state: "CA",
      target: "120.98",
      actual: "120.98",
      difference: "120.98",
      difference_: 4.5,
      target_actual: 4,
    },
    {
      service_area_state: "CO",
      target: "120.98",
      actual: "120.98",
      difference: "120.98",
      difference_: 4.5,
      target_actual: 4.4,
    },
    {
      service_area_state: "CT",
      target: "120.98",
      actual: "120.98",
      difference: "120.98",
      difference_: 4.5,
      target_actual: 4,
    },
    {
      service_area_state: "DE",
      target: "1210.98",
      actual: "1320.98",
      difference: "1290.98",
      difference_: -4.5,
      target_actual: 4,
    },
    {
      service_area_state: "FL",
      target: "1210.98",
      actual: "1320.98",
      difference: "1290.98",
      difference_: -4.5,
      target_actual: 4,
    },
    {
      service_area_state: "GA",
      target: "1220.98",
      actual: "1420.98",
      difference: "1230.98",
      difference_: 4.5,
      target_actual: 4,
    },
    {
      service_area_state: "HI",
      target: "1210.98",
      actual: "1320.98",
      difference: "1290.98",
      difference_: -100,
      target_actual: 4,
    },
  ];

  const handleGraph = () => {
    return (
      <div>
        <HorizontalBarChart
          actualvalue={12000}
          targetvalue={140000}
          type={"number"}
          graphtype={{ type1: "Target", type2: "Actual" }}
          aspectRatio={5}
        />
      </div>
    );
  };

  const colDefs = [
    {
      field: "service_area_state",
      headerName: "Service Area State",
      flex: 1,
    },
    {
      field: "target",
      headerName: dataType[0],
      flex: 1,
      hide: showGraph,
      cellRenderer:
        selectedViewType === "Expense PMPM" ? handleValueinDollar : "target",
    },
    {
      field: "actual",
      headerName: dataType[1],
      flex: 1,
      hide: showGraph,
      cellRenderer:
        selectedViewType === "Expense PMPM" ? handleValueinDollar : "actual",
    },
    {
      field: "difference",
      headerName: "Difference",
      flex: 1,
      cellRenderer:
        selectedViewType === "Expense PMPM"
          ? handleValueinDollar
          : "difference",
    },
    {
      field: "differencePercentage",
      headerName: "Difference %",
      flex: 1,
      cellRenderer: handleDifferenceinPercentage,
    },
  ];
  return (
    <>
      {!showGraph && (
        <div
          className={"ag-theme-quartz"}
          style={{
            height: "418px",
          }}
        >
          <AgGridReact rowData={data} columnDefs={colDefs} pagination={true} paginationPageSize={20} />
        </div>
      )}
      {showGraph && (
        <div className="d-flex column">
          <RegionMap
            selectedViewType={props.selectedViewType}
            data={dataWithoutFormat}
            dataType={dataType}
          />
          <div
            className={"ag-theme-quartz col-5"}
            style={{
              height: "418px",
            }}
          >
            <AgGridReact rowData={data} columnDefs={colDefs} />
          </div>
        </div>
      )}
    </>
  );
}

export default SummaryInOutPatient;
