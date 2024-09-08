import { useState } from "react";
import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import { vector } from "../../assets/images/svg/SVGIcons";
import InputSelectField from "../../components/InputSelectFiled";
import { graphSVG } from "../../assets/images/svg/SVGIcons";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import TooltipComponent from "../../components/TooltipComponent";
import { formatNumberColour } from "../../utilities/FormatUtilities";

const CareProviderDetails = (props) => {
  const {
    topMembersPerProvider,
    topTenMembersByCostforEachProvider,
    topTenProvidersByCost,
    topTenProvidersByCostForEachSpeciality,

    careProviderOptions,
    onSelectChange,

    onProviderChange,
    providerOptions,
    // specialtyOptions,
    // onSpecialtyChange
  } = props;

  //except 1st table
  const [showTopMembersGraph, setShowTopMembersGraph] = useState(true);
  const [showTopProviderGraph, setShowTopProviderGraph] = useState(true);
  const [showTopProviderSpecialtyGraph, setShowTopProviderSpecialtyGraph] =
    useState(true);

  //max values for all 3 graphs
  const maxValueTopMembersByCostForProvider =
    topTenMembersByCostforEachProvider[0]?.totalPricePM;
  const maxValueTopProviderByCost = topTenProvidersByCost[0]?.totalPricePM;
  const maxValueTopProviderByCostForEachSpeciality =
    topTenProvidersByCostForEachSpeciality[0]?.totalPricePM;

  //graph and number view handling of 2nd table(members by cost for each provider)
  //graph part
  const handleTopMembersByCostForEachProviderGraph = (params) => {
    const name = params?.data?.memberUid;
    const value = params?.data?.totalPricePM;
    return (
      <div className="d-flex">
        <p className="col-5">{name}</p>
        <span className="col-7">
          <HorizontalBarChart
            actualvalue={value}
            type={"number"}
            graphtype={{ type1: "Target", type2: "Actual" }}
            aspectRatio={6}
            graphLength={1}
            height={"38px"}
            maxValue={maxValueTopMembersByCostForProvider}
            minValue={0}
          />
        </span>
      </div>
    );
  };

  //number format part
  const handleTopMembersByCostForEachProviderValue = (params) => {
    const name = params?.data?.memberUid;
    const value = formatNumberColour(params?.data?.totalPricePM);
    return (
      <div className="d-flex">
        <p className="col-6">{name}</p>
        <span className="col-6">{value}</span>
      </div>
    );
  };

  //graph and number view handling of 3rd table(providers by cost)
  //graph part
  const handleTopProviderByCostGraph = (params) => {
    console.log("Inside providers by cost graph", params);
    const providerName = params?.data?.providerName;
    const providerValue = params?.data?.totalPricePM;
    return (
      <div className="d-flex">
        <p className="col-5">
          <TooltipComponent
            value={providerName}
            length={25}
            children={providerName}
          />
        </p>
        <span className="col-7">
          <HorizontalBarChart
            actualvalue={providerValue}
            type={"number"}
            graphtype={{ type1: "Target", type2: "Actual" }}
            aspectRatio={6}
            graphLength={1}
            height={"38px"}
            maxValue={maxValueTopProviderByCost}
            minValue={0}
          />
        </span>
      </div>
    );
  };
  //number part
  const handleTopProviderByCostValue = (params) => {
    const providerName = params?.data?.providerName;
    const providerValue = formatNumberColour(params?.data?.totalPricePM);
    return (
      <div className="d-flex">
        <p className="col-8">
          <TooltipComponent
            value={providerName}
            length={45}
            children={providerName}
          />
        </p>
        <span className="col-4">{providerValue}</span>
      </div>
    );
  };

  //Top 10 PCP by cost for each specialty
  const handleTopProviderByCostForEachSpecialityGraph = (params) => {
    const name = params?.data?.providerName;
    const value = params?.data?.totalPricePM;
    return (
      <div className="d-flex">
        <p className="col-5">
          <TooltipComponent value={name} length={25} children={name} />
        </p>
        <span className="col-7">
          <HorizontalBarChart
            actualvalue={value}
            type={"number"}
            graphtype={{ type1: "Target", type2: "Actual" }}
            aspectRatio={6}
            graphLength={1}
            height={"38px"}
            maxValue={maxValueTopProviderByCostForEachSpeciality}
            minValue={0}
          />
        </span>
      </div>
    );
  };

  const handleTopProviderByCostForEachSpecialityValue = (params) => {
    const name = params?.data?.providerName;
    const value = formatNumberColour(params?.data?.totalPricePM);
    return (
      <div className="d-flex">
        <p className="col-9">
          <TooltipComponent value={name} length={45} children={name} />
        </p>
        <span className="col-3">{value}</span>
      </div>
    );
  };

  //coldefs for all 4 tables
  const topMembersByProviderColDef = [
    {
      field: "providerName",
      headerName: "Provider Name",
      flex: 1,
    },
    {
      field: "totalMembers",
      headerName: "Members",
      flex: 1,
    },
  ];

  const topMembersByCostForEachProviderColDefs = [
    {
      field: "memberUid",
      headerName: "Top Members",
      flex: 1,
      hide: showTopMembersGraph,
      cellRenderer: handleTopMembersByCostForEachProviderValue,
    },
    {
      field: "totalPricePM",
      headerName: "Top Members",
      flex: 1,
      hide: !showTopMembersGraph,
      cellRenderer: handleTopMembersByCostForEachProviderGraph,
    },
  ];

  const topProvidersByCostColDefs = [
    {
      field: "providerName",
      headerName: "Top Provider",
      flex: 1,
      hide: showTopProviderGraph,
      cellRenderer: handleTopProviderByCostValue,
    },
    {
      field: "totalPricePM",
      headerName: "Top Provider",
      flex: 1,
      hide: !showTopProviderGraph,
      cellRenderer: handleTopProviderByCostGraph,
    },
  ];

  const topProvidersByCostForEachSpecialityColDefs = [
    {
      field: "providerName",
      headerName: "Top Provider",
      flex: 1,
      hide: showTopProviderSpecialtyGraph,
      cellRenderer: handleTopProviderByCostForEachSpecialityValue,
    },
    {
      field: "totalPricePM",
      headerName: "Top Provider",
      flex: 1,
      hide: !showTopProviderSpecialtyGraph,
      cellRenderer: handleTopProviderByCostForEachSpecialityGraph,
    },
  ];

  //graph toggle for 3 tables
  const handleTopMembersTableView = () => {
    setShowTopMembersGraph(!showTopMembersGraph);
  };

  const handleTopProviderTableView = () => {
    setShowTopProviderGraph(!showTopProviderGraph);
  };

  const handleTopProviderSpecialtyTableView = () => {
    setShowTopProviderSpecialtyGraph(!showTopProviderSpecialtyGraph);
  };

  //care provider option selection
  const onSelctedProviderChange = (fieldName, selectedValue) => {
    onProviderChange(selectedValue);
  };

  const onSpecialtyChange = (fieldName, selectedValue) => {
    onSelectChange(selectedValue);
  };

  return (
    <>
      <div className="d-flex mt-2 justify-content-between">
        <div className="col-6 p-2">
          <h6 style={{ marginBottom: "28px" }}>Members per Provider</h6>
          <div
            className={"ag-theme-quartz"}
            style={{
              height: "250px",
            }}
          >
            <AgGridReact
              rowData={topMembersPerProvider}
              columnDefs={topMembersByProviderColDef}
            />
          </div>
        </div>
        <div className="col-6 p-2">
          <div className="d-flex justify-content-between">
            <h6 className="col-5">Top 10 Members by Cost for each Provider</h6>
            <span className="col-7 d-flex justify-content-end">
              <p>Provider</p>
              <span className="mx-1">{vector}</span>
              <span style={{ width: "100px" }}>
                <InputSelectField
                  options={providerOptions}
                  onChange={onSelctedProviderChange}
                />
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
            <AgGridReact
              rowData={topTenMembersByCostforEachProvider}
              columnDefs={topMembersByCostForEachProviderColDefs}
            />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="d-flex justify-content-between">
          <div className="col-6 p-2">
            <span className="d-flex justify-content-between">
              <h6 style={{ marginBottom: "27px" }}>Top 10 Providers by Cost</h6>
              <div
                onClick={handleTopProviderTableView}
                className="coc-cursor ms-3"
              >
                View: {showTopProviderGraph ? graphSVG : "123"}
              </div>
            </span>
            <div
              className={"ag-theme-quartz"}
              style={{
                height: "250px",
              }}
            >
              <AgGridReact
                rowData={topTenProvidersByCost}
                columnDefs={topProvidersByCostColDefs}
              />
            </div>
          </div>
          <div className="col-6 p-2">
            <div className="d-flex justify-content-between">
              <h6 className="col-4">Top 5 Providers by Cost by Speciality</h6>
              <span className="col-7 d-flex justify-content-end">
                <p>Specialty</p>
                <span className="mx-1">{vector}</span>
                <span style={{ width: "80px" }}>
                  <InputSelectField
                    options={careProviderOptions}
                    onChange={onSpecialtyChange}
                  />
                </span>
                <div
                  onClick={handleTopProviderSpecialtyTableView}
                  className="coc-cursor ms-3"
                >
                  View: {showTopProviderSpecialtyGraph ? graphSVG : "123"}
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
                rowData={topTenProvidersByCostForEachSpeciality}
                columnDefs={topProvidersByCostForEachSpecialityColDefs}
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default CareProviderDetails;
