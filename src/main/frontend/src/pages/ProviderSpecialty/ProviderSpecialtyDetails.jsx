import { useState } from "react";
import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import { vector } from "../../assets/images/svg/SVGIcons";
import InputSelectField from "../../components/InputSelectFiled";
import { graphSVG } from "../../assets/images/svg/SVGIcons";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import { formatNumberColour } from "../../utilities/FormatUtilities";
import { handleValue } from "../../utilities/FormatUtilities";
const ProviderSpecialtyDetails = () => {
  const [showMembersProvidersGraph, setShowMembersProvidersGraph] =
    useState(true);
  const [showSpecialtyGraph, setShowSpecialtyGraph] = useState(true);

  const handleTopSpecialtiesGraph = () => {
    return (
      <div className="d-flex">
        <p>Sample Specialty Name</p>
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

  //Members Per PCP Group
  const membersbyProviderData = [
    {
      provider: "Provider Group 1",
      member: 13.4,
    },
    {
      provider: "Provider Group 2",
      member: 13.4,
    },
    {
      provider: "Provider Group 3",
      member: 13.4,
    },
    {
      provider: "Provider Group 4",
      member: 13.4,
    },
    {
      provider: "Provider Group 5",
      member: 13.4,
    },
  ];
  const [membersbyProviderRowData, setMembersbyProviderRowData] = useState(
    membersbyProviderData
  );
  const membersbyProviderColDefs = [
    {
      field: "provider",
      headerName: "Provider",
      flex: 1,
    },
    {
      field: "member",
      headerName: "Member",
      flex: 1,
    },
  ];

  //Top 5 PCP by cost for each Specialty
  const specialtiesData = [
    {
      specialties: "Sample Speciality Name",
    },
    {
      specialties: "Sample Speciality Name",
    },
    {
      specialties: "Sample Speciality Name ",
    },
    {
      specialties: "Sample Speciality Name",
    },
    {
      specialties: "Sample Speciality Name",
    },
  ];
  const [specialtiesRowData, setSpecialtiesRowData] = useState(specialtiesData);
  const specialtiesColDefs = [
    {
      field: "specialties",
      headerName: "Specialties",
      flex: 1,
      hide: showSpecialtyGraph,
      cellRenderer: handleValue,
    },
    {
      field: "specialties",
      headerName: "Specialties",
      flex: 1,
      hide: !showSpecialtyGraph,
      cellRenderer: handleTopSpecialtiesGraph,
    },
  ];

  //Top 10 PCP by cost
  const topMembersPerSpecialtyData = [
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
  const [topMembersPerSpecialtyRowData, setTopMembersPerSpecialtyRowData] =
    useState(topMembersPerSpecialtyData);
  const topMembersPerSpecialtyColDefs = [
    {
      field: "topMembers",
      headerName: "Top Members",
      flex: 1,
      hide: showMembersProvidersGraph,
      cellRenderer: handleValue,
    },
    {
      field: "topMembers",
      headerName: "Top Members",
      flex: 1,
      hide: !showMembersProvidersGraph,
      cellRenderer: handleTopSpecialtiesGraph,
    },
  ];

  //Top 5 Providers by cost for each PCP
  const topProviders = [
    {
      topProviders: "Sample Long Provider Name",
    },
    {
      topProviders: "Sample Long Provider Name",
    },
    {
      topProviders: "Sample Long Provider Name",
    },
    {
      topProviders: "Sample Long Provider Name",
    },
    {
      topProviders: "Sample Long Provider Name",
    },
  ];
  const [topProvidersRowData, setTopProvidersRowData] = useState(topProviders);
  const topProvidersColDefs = [
    {
      field: "topProviders",
      headerName: "Top Providers",
      flex: 1,
      hide: !showMembersProvidersGraph,
      cellRenderer: handleTopSpecialtiesGraph,
    },
    {
      field: "topProviders",
      headerName: "Top Providers",
      flex: 1,
      hide: showMembersProvidersGraph,
      cellRenderer: handleValue,
    },
  ];
  const handleMembersProvidersView = () => {
    setShowMembersProvidersGraph(!showMembersProvidersGraph);
  };

  const handleSpecialtyTableView = () => {
    setShowSpecialtyGraph(!showSpecialtyGraph);
  };
  return (
    <>
      <div className="d-flex mt-2 justify-content-between">
        <div className="col-6 p-2">
          <h6 className="mb-4">Members Per Provider</h6>
          <div
            className={"ag-theme-quartz"}
            style={{
              height: "250px",
            }}
          >
            <AgGridReact
              rowData={membersbyProviderRowData}
              columnDefs={membersbyProviderColDefs}
            />
          </div>
        </div>
        <div className="col-6 p-2">
          <div className="d-flex mb-0 justify-content-between">
            <h6 className="mb-4">Top 5 Specialties by cost</h6>

            <div onClick={handleSpecialtyTableView} className="coc-cursor ms-3">
              View: {showSpecialtyGraph ? graphSVG : "123"}
            </div>
          </div>

          <div
            className={"ag-theme-quartz"}
            style={{
              height: "250px",
            }}
          >
            <AgGridReact
              rowData={specialtiesRowData}
              columnDefs={specialtiesColDefs}
            />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="d-flex  mt-3 justify-content-between">
          <h6>Top Members & Providers per Specialty</h6>
          <span className="d-flex justify-content-end">
            <p>PCP</p>
            <span className="mx-1">{vector}</span>
            <span>
              <InputSelectField
                options={[{ label: "Specality1", value: "" }]}
              ></InputSelectField>
            </span>
            <div
              onClick={handleMembersProvidersView}
              className="coc-cursor ms-3"
            >
              View: {showMembersProvidersGraph ? graphSVG : "123"}
            </div>
          </span>
        </div>
        <div className="d-flex justify-content-between">
          <div className="col-6 p-2">
            <div
              className={"ag-theme-quartz"}
              style={{
                height: "300px",
              }}
            >
              <AgGridReact
                rowData={topMembersPerSpecialtyRowData}
                columnDefs={topMembersPerSpecialtyColDefs}
              />
            </div>
          </div>
          <div className="col-6 p-2">
            <div
              className={"ag-theme-quartz"}
              style={{
                height: "300px",
              }}
            >
              <AgGridReact
                rowData={topProvidersRowData}
                columnDefs={topProvidersColDefs}
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default ProviderSpecialtyDetails;
