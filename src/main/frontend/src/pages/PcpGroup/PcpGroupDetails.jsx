import { useState } from "react";
import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import { vector } from "../../assets/images/svg/SVGIcons";
import InputSelectField from "../../components/InputSelectFiled";
import { graphSVG } from "../../assets/images/svg/SVGIcons";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import { handleValue } from "../../utilities/FormatUtilities";
const PcpGroupDetails = () => {
  const [showTopMembersGraph, setShowTopMembersGraph] = useState(true);
  const [showTopPcpGraph, setShowTopPcpGraph] = useState(true);
  const [showTopPcpSpecialtyGraph, setShowTopPcpSpecialtyGraph] =
    useState(true);
  const handleGraph = () => {
    return (
      <div className="d-flex">
        <p>Sample Long Name</p>
        <HorizontalBarChart
          actualvalue={10}
          type={"number"}
          aspectRatio={5}
          graphLength={1}
          height={"35px"}
        />
      </div>
    );
  };
  //Members Per PCP Group
  const pcpData = [
    {
      pcpProviderGroup: "Provider Group 1",
      members: 13.4,
    },
    {
      pcpProviderGroup: "Provider Group 2",
      members: 13.4,
    },
    {
      pcpProviderGroup: "Provider Group 2",
      members: 13.4,
    },
    {
      pcpProviderGroup: "Provider Group 2",
      members: 13.4,
    },
    {
      pcpProviderGroup: "Provider Group 2",
      members: 13.4,
    },
  ];
  const [pcpRowData, setPcpRowData] = useState(pcpData);
  const pcpColDefs = [
    {
      field: "pcpProviderGroup",
      headerName: "PCP Provider Group",
      flex: 1,
    },
    {
      field: "members",
      headerName: "Members",
      flex: 1,
    },
  ];

  //Top 5 PCP by cost for each Specialty
  const top5RowData = [
    {
      topPcp: "Sample Long PCP Name",
    },
    {
      topPcp: "Sample Long PCP Name",
    },
    {
      topPcp: "Sample Long PCP Name",
    },
    {
      topPcp: "Sample Long PCP Name",
    },
    {
      topPcp: "Sample Long PCP Name",
    },
  ];
  const [top5PcpRowData, setTop5PcpRowData] = useState(top5RowData);
  const top5PcpColDefs = [
    {
      field: "topPcp",
      headerName: "Top PCP",
      flex: 1,
      hide: showTopMembersGraph,
      cellRenderer: handleValue,
    },
    {
      field: "topPcp",
      headerName: "Top PCP",
      flex: 1,
      hide: !showTopMembersGraph,
      cellRenderer: handleGraph,
    },
  ];

  //Top 10 PCP by cost
  const top10RowData = [
    {
      topPcp: "Sample Long PCP Name",
    },
    {
      topPcp: "Sample Long PCP Name",
    },
    {
      topPcp: "Sample Long PCP Name",
    },
    {
      topPcp: "Sample Long PCP Name",
    },
    {
      topPcp: "Sample Long PCP Name",
    },
  ];
  const [top10PcpByCost, setTop5PcpByCost] = useState(top10RowData);
  const top10PcpByCostColDefs = [
    {
      field: "topPcp",
      headerName: "Top PCP",
      flex: 1,
      hide: showTopPcpGraph,
      cellRenderer: handleValue,
    },
    {
      field: "topPcp",
      headerName: "Top PCP",
      flex: 1,
      hide: !showTopPcpGraph,
      cellRenderer: handleGraph,
    },
  ];

  //Top 10 Members by cost for each PCP
  const top10MembersByCost = [
    {
      topMembers: "Sample Long Member Name",
    },
    {
      topMembers: "Sample Long Member Name",
    },
    {
      topMembers: "Sample Long Member Name",
    },
    {
      topMembers: "Sample Long Member Name",
    },
    {
      topMembers: "Sample Long Member Name",
    },
  ];
  const [top10MembersByCostRowData, setTop10MembersByCostRowData] =
    useState(top10MembersByCost);
  const top10MembersByCostColDefs = [
    {
      field: "topMembers",
      headerName: "Top Members",
      flex: 1,
      hide: showTopPcpSpecialtyGraph,
      cellRenderer: handleValue,
    },
    {
      field: "topMembers",
      headerName: "Top Members",
      flex: 1,
      hide: !showTopPcpSpecialtyGraph,
      cellRenderer: handleGraph,
    },
  ];
  const handleTopMembersTableView = () => {
    setShowTopMembersGraph(!showTopMembersGraph);
  };
  const handleTopPcpTableView = () => {
    setShowTopPcpGraph(!showTopPcpGraph);
  };
  const handleTopPcpSpecialtyTableView = () => {
    setShowTopPcpSpecialtyGraph(!showTopPcpSpecialtyGraph);
  };

  return (
    <>
      <div className="d-flex mt-2 justify-content-between">
        <div className="col-5 p-2">
          <h6 className="mb-4">Members Per PCP Group</h6>
          <div
            className={"ag-theme-quartz"}
            style={{
              height: "250px",
            }}
          >
            <AgGridReact rowData={pcpRowData} columnDefs={pcpColDefs} />
          </div>
        </div>
        <div className="col-7 p-2">
          <div className="d-flex justify-content-between">
            <h6 className="mb-4">Top 5 PCP by cost for each Specialty</h6>
            <span className="d-flex justify-content-end">
              <span className="mx-1">{vector}</span>
              <span>
                <InputSelectField
                  options={[{ label: "Surgery", value: "" }]}
                ></InputSelectField>
              </span>
              <div
                onClick={handleTopMembersTableView}
                className="coc-cursor ms-3"
              >
                View: {showTopMembersGraph ? graphSVG : "123"}
              </div>
            </span>
          </div>

          <div
            className={"ag-theme-quartz"}
            style={{
              height: "250px",
            }}
          >
            <AgGridReact rowData={top5PcpRowData} columnDefs={top5PcpColDefs} />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="d-flex mt-2 justify-content-between">
          <div className="col-5 p-2">
            <span className="d-flex mb-4 justify-content-between">
              <h6>Top 10 PCP by Cost</h6>
              <div onClick={handleTopPcpTableView} className="coc-cursor ms-3">
                View: {showTopPcpGraph ? graphSVG : "123"}
              </div>
            </span>
            <div
              className={"ag-theme-quartz"}
              style={{
                height: "250px",
              }}
            >
              <AgGridReact
                rowData={top10PcpByCost}
                columnDefs={top10PcpByCostColDefs}
              />
            </div>
          </div>
          <div className="col-7 p-2">
            <div className="d-flex mb-3 justify-content-between">
              <h6 className="">Top 5 PCP by cost for each PCP</h6>
              <span className="d-flex justify-content-end">
                <p>PCP</p>
                <span className="mx-1">{vector}</span>
                <span>
                  <InputSelectField
                    options={[{ label: "Surgery", value: "" }]}
                  ></InputSelectField>
                </span>
                <div
                  onClick={handleTopPcpSpecialtyTableView}
                  className="coc-cursor ms-3"
                >
                  View: {showTopPcpSpecialtyGraph ? graphSVG : "123"}
                </div>
              </span>
            </div>
            <div
              className={"ag-theme-quartz"}
              style={{
                height: "250px",
              }}
            >
              <AgGridReact
                rowData={top10MembersByCostRowData}
                columnDefs={top10MembersByCostColDefs}
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default PcpGroupDetails;
