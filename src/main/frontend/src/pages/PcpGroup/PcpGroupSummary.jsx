import React, { useState } from "react";
import HorizontalBarChart from "../../components/HorizontalBarChart.jsx";
import {
  handleDifferenceinPercentage,
  handleValueinDollar,
} from "../../utilities/FormatUtilities.jsx";
import { graphSVG } from "../../assets/images/svg/SVGIcons.jsx";
import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme

const PcpGroupSummary = (props) => {
  const { rowData, dataType } = props;
  const [showGraph, setShowGraph] = useState(false);

  const handleGraph = (params) => {
    return (
      <span style={{ flex: 1 }}>
        <HorizontalBarChart
          actualvalue={params.value[0]}
          targetvalue={params.value[1]}
          type={"number"}
          graphtype={{ type1: "Target", type2: "Actual" }}
          aspectRatio={5}
          graphLength={2}
          height={"100%"}
        />
      </span>
    );
  };

  const handleTableView = () =>
    showGraph === false ? setShowGraph(true) : setShowGraph(false);

  const colDefs = [
    {
      field: "pcp_group",
      headerName: "PCP Provider Group",
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
      <div className="d-flex justify-content-between p-0 mt-3">
        <strong>PMPM</strong>
        <span className="d-flex">
          {showGraph ? (
            <div className="me-4">
              <i
                className="fa fa-circle me-2"
                style={{ color: "#004F59" }}
                aria-hidden="true"
              >
                Target
              </i>
              <i
                className="fa fa-circle ms-2"
                style={{ color: "#6FC2B4" }}
                aria-hidden="true"
              >
                Actual
              </i>
            </div>
          ) : (
            <p></p>
          )}
          <div onClick={handleTableView} className="coc-cursor">
            <strong>View:</strong> {showGraph ? graphSVG : "123"}
          </div>
        </span>
      </div>
      <div
        className={"ag-theme-quartz mt-2"}
        style={{
          height: "430px",
        }}
      >
        <AgGridReact
          rowData={rowData}
          columnDefs={colDefs}
          pagination={true}
          paginationPageSize={20}
        />
      </div>
    </>
  );
};

export default PcpGroupSummary;
