import React from "react";
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import {
  handleDifferenceinPercentage,
  handleValueinDollar,
} from "../../utilities/FormatUtilities";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import "../../App.css";

function CareCategoryInOutPatient(props) {
  const { showGraph, dataType } = props;
  const rowData = props.rowdata;
  const handleGraph = (params) => {
    return (
      <div>
        <HorizontalBarChart
          actualvalue={params.value[0]}
          targetvalue={params.value[1]}
          type={"number"}
          graphtype={{ type1: dataType[0], type2: dataType[1] }}
          aspectRatio={5}
          graphLength={2}
          height={"120px"}
        />
      </div>
    );
  };

  const colDefs = [
    {
      field: "care_provider",
      headerName: "Care Provider",
      flex: 1,
    },
    {
      field: "target",
      headerName: dataType ? dataType[0] : "Target",
      flex: 1,
      hide: showGraph,
      cellRenderer: handleValueinDollar,
    },
    {
      field: "actual",
      headerName: dataType ? dataType[1] : "Actual",
      flex: 1,
      hide: showGraph,
      cellRenderer: handleValueinDollar,
    },
    {
      field: "target_actual",
      headerName: dataType
        ? dataType[0] === "Target"
          ? "Target vs Actual"
          : "Prior vs Current"
        : "Target vs Actual",
      flex: 1,
      hide: !showGraph,
      cellRenderer: handleGraph,
    },
    {
      field: "difference",
      headerName: "Difference",
      flex: 1,
      cellRenderer: handleValueinDollar,
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
      <div
        className={"ag-theme-quartz"}
        style={{
          height: "418px",
        }}
      >
        <AgGridReact
          rowData={rowData}
          columnDefs={colDefs}
          rowHeight={46}
          pagination={true}
          paginationPageSize={20}
        />
      </div>
    </>
  );
}

export default CareCategoryInOutPatient;
