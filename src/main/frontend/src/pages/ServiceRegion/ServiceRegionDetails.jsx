import React, { useState } from "react";
import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import { graphSVG } from "../../assets/images/svg/SVGIcons";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import { vector } from "../../assets/images/svg/SVGIcons";
import InputSelectField from "../../components/InputSelectFiled";
import { handleValue } from "../../utilities/FormatUtilities";
function ServiceRegionDetails() {
  const handleGraph = () => {
    return (
      <div className="d-flex">
        <p>Sample Long Member Name</p>
        <HorizontalBarChart
          actualvalue={12000}
          type={"number"}
          graphtype={{ type1: "Target", type2: "Actual" }}
          aspectRatio={7}
          graphLength={1}
          height={"38px"}
        />
      </div>
    );
  };
  const handleTableView = () => {
    setShowGraph(!showGraph);
  };
  const [showGraph, setShowGraph] = useState(true);

  const serviceregionBreakdownData = [
    {
      serviceAreaState: "AL",
      members: 13.4,
      providers: 500,
    },
    {
      serviceAreaState: "AL",
      members: 13.4,
      providers: 500,
    },
    {
      serviceAreaState: "AL",
      members: 13.4,
      providers: 500,
    },
    {
      serviceAreaState: "AL",
      members: 13.4,
      providers: 500,
    },
    {
      serviceAreaState: "AL",
      members: 13.4,
      providers: 500,
    },
    {
      serviceAreaState: "AL",
      members: 13.4,
      providers: 500,
    },
  ];
  const [serviceregionBreakdownRowData, setServiceregionBreakdownRowData] =
    useState(serviceregionBreakdownData);

  const serviceregionBreakdownColDefs = [
    {
      field: "serviceAreaState",
      headerName: "Service Area State",
      flex: 1,
    },
    {
      field: "members",
      headerName: "Members",
      flex: 1,
    },
    {
      field: "providers",
      headerName: "Providers",
      flex: 1,
    },
  ];

  const topMembersProvidersData = [
    {
      members: "Sample Long Member Name",
      providers: "Sample Long Member Name",
    },
    {
      members: "Sample Long Member Name ",
      providers: "Sample Long Member Name",
    },
    {
      members: "Sample Long Member Name",
      providers: "Sample Long Member Name",
    },
    {
      members: "Sample Long Member Name",
      providers: "Sample Long Member Name",
    },
    {
      members: "Sample Long Member Name",
      providers: "Sample Long Member Name",
    },
    {
      members: "Sample Long Member Name",
      providers: "Sample Long Member Name",
    },
  ];
  const [topMembersProvidersRowData, setTopMembersProvidersData] = useState(
    topMembersProvidersData
  );

  const topMembersProvidersColDefs = [
    {
      field: "members",
      headerName: "Members",
      flex: 1,
      hide: showGraph,
      cellRenderer: handleValue,
    },
    {
      field: "providers",
      headerName: "Providers",
      flex: 1,
      hide: showGraph,
      cellRenderer: handleValue,
    },
    {
      field: "membersGraph",
      headerName: "Members",
      flex: 1,
      hide: !showGraph,
      cellRenderer: handleGraph,
    },
    {
      field: "providersGraph",
      headerName: "Providers",
      flex: 1,
      hide: !showGraph,
      cellRenderer: handleGraph,
    },
  ];
  return (
    <>
      <h6 className="my-3">Service Region Breakdown</h6>
      <div
        className={"ag-theme-quartz"}
        style={{
          height: "250px",
        }}
      >
        <AgGridReact
          rowData={serviceregionBreakdownRowData}
          columnDefs={serviceregionBreakdownColDefs}
        />
      </div>
      <div className="d-flex my-3 justify-content-between">
        <h6>Top 10 Members & Providers per Service Region</h6>
        <div className="col-6 d-flex justify-content-end">
          <span>
            <p>Service Region</p>
          </span>
          <span className="mx-2">{vector}</span>
          <span>
            <InputSelectField
              options={[{ label: "AL", value: "" }]}
            ></InputSelectField>
          </span>
          <div onClick={handleTableView} className="coc-cursor ms-3">
            View: {showGraph ? graphSVG : "123"}
          </div>
        </div>
      </div>

      <div
        className={"ag-theme-quartz"}
        style={{
          height: "250px",
        }}
      >
        <AgGridReact
          rowData={topMembersProvidersRowData}
          columnDefs={topMembersProvidersColDefs}
        />
      </div>
    </>
  );
}

export default ServiceRegionDetails;
