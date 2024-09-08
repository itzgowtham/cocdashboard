import { useState, useEffect } from "react";
import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import HorizontalBarChart from "../../components/HorizontalBarChart";
import InputSelectField from "../../components/InputSelectFiled";
import TooltipComponent from "../../components/TooltipComponent";
import { graphSVG, vector } from "../../assets/images/svg/SVGIcons";
import { formatNumberColour } from "../../utilities/FormatUtilities";

const ProviderSpecialtyDetails = (props) => {
  const {
    membersPerSpecialty,
    topSpecialties,
    topMembersPerSpecialty,
    topProvidersBySpecialty,
    specialtyOptions,
    onSelectChange,
  } = props;
  const [showMembersProvidersGraph, setShowMembersProvidersGraph] =
    useState(true);
  const [showSpecialtyGraph, setShowSpecialtyGraph] = useState(true);

  const maxValueTopSpecialty = topSpecialties[0]?.totalPricePM;
  const minValueTopSpecialty = topSpecialties[0]?.totalPricePM;
  const maxValueTopMemberPerSpecialty = topMembersPerSpecialty[0]?.totalPricePM;
  const minValueTopMemberPerSpecialty = topMembersPerSpecialty[0]?.totalPricePM;
  const maxValueTopProviderBySpecialty =
    topProvidersBySpecialty[0]?.totalPricePM;
  const minValueTopProviderBySpecialty =
    topProvidersBySpecialty[0]?.totalPricePM;

  //Members Per Specialty Graph & Value - Table 3
  // const handleMembersPerSpecialtyGraph = (params) => {
  //   const name = params?.data?.speciality;
  //   const value = params?.data?.totalPricePM;
  //   return (
  //     <div className="d-flex">
  //       <p style={{ flex: 2 }}>
  //         <TooltipComponent value={name} length={35} children={name} />
  //       </p>
  //       <span style={{ flex: 2 }}>
  //         <HorizontalBarChart
  //           actualvalue={value}
  //           type={"number"}
  //           graphtype={{ type1: "Target", type2: "Actual" }}
  //           aspectRatio={6}
  //           graphLength={1}
  //           height={"38px"}
  //           maxValue={maxValueTopSpecialty}
  //           minValue={minValueTopSpecialty}
  //         />
  //       </span>
  //     </div>
  //   );
  // };
  // const handleMembersPerSpecialtyValue = (params) => {
  //   const name = params?.data?.speciality;
  //   const value = params?.data?.totalPricePM;
  //   return (
  //     <div className="d-flex">
  //       <p style={{ flex: 2 }}>
  //         <TooltipComponent value={name} length={35} children={name} />
  //       </p>
  //       <span style={{ flex: 1 }}>{value}</span>
  //     </div>
  //   );
  // };

  //Top Specialties by cost Graph & Value - Table 3
  const handleTopSpecialtiesByCostGraph = (params) => {
    const name = params?.data?.speciality;
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
            maxValue={maxValueTopSpecialty}
            minValue={minValueTopSpecialty}
          />
        </span>
      </div>
    );
  };
  const handleTopSpecialtiesByCostValue = (params) => {
    const name = params?.data?.speciality;
    const value = formatNumberColour(params?.data?.totalPricePM);
    return (
      <div className="d-flex">
        <p className="col-9">
          <TooltipComponent value={name} length={35} children={name} />
        </p>
        <span className="col-3">{value}</span>
      </div>
    );
  };

  //Top Members Per Specialty Graph & Value - Table 3
  const topMembersPerSpecialtyGraph = (params) => {
    const name = params?.data?.memberUid;
    const value = params?.data?.totalPricePM;
    return (
      <div className="d-flex">
        <p style={{ flex: 2 }}>{name}</p>
        <span style={{ flex: 2 }}>
          <HorizontalBarChart
            actualvalue={value}
            type={"number"}
            graphtype={{ type1: "Target", type2: "Actual" }}
            aspectRatio={6}
            graphLength={1}
            height={"38px"}
            maxValue={maxValueTopMemberPerSpecialty}
            minValue={minValueTopMemberPerSpecialty}
          />
        </span>
      </div>
    );
  };
  const topMembersPerSpecialtyValue = (params) => {
    const name = params?.data?.memberUid;
    const value = formatNumberColour(params?.data?.totalPricePM);
    return (
      <div className="d-flex">
        <p style={{ flex: 2 }}>{name}</p>
        <span style={{ flex: 2 }}>{value}</span>
      </div>
    );
  };

  //Top Providers Per Specialty Graph & Value - Table 4
  const topProvidersPerSpecialtyGraph = (params) => {
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
            maxValue={maxValueTopProviderBySpecialty}
            minValue={minValueTopProviderBySpecialty}
          />
        </span>
      </div>
    );
  };
  const topProvidersPerSpecialtyValue = (params) => {
    const name = params?.data?.providerName;
    const value = formatNumberColour(params?.data?.totalPricePM);
    return (
      <div className="d-flex">
        <p className="col-9">
          {" "}
          <TooltipComponent value={name} length={35} children={name} />
        </p>
        <span className="col-3">{value}</span>
      </div>
    );
  };

  //Members Per Specialty Col Defs - Table 1
  const membersPerSpecialtyColDefs = [
    {
      field: "speciality",
      headerName: "Specialties",
      flex: 1,
      // hide: showSpecialtyGraph,
      // cellRenderer: handleValue,
    },
    {
      field: "totalMembers",
      headerName: "Member",
      flex: 1,
      // hide: !showSpecialtyGraph,
      // cellRenderer: sampleGraph,
    },
  ];

  //TopSpecialtiesByCost Col Defs - Table 2
  const topFiveSpecialtiesByCostColDefs = [
    {
      field: "speciality",
      headerName: "Specialties",
      flex: 1,
      hide: showSpecialtyGraph,
      cellRenderer: handleTopSpecialtiesByCostValue,
    },
    {
      field: "totalPricePM",
      headerName: "Specialties",
      flex: 1,
      hide: !showSpecialtyGraph,
      cellRenderer: handleTopSpecialtiesByCostGraph,
    },
  ];

  //Top Members Per Specialty Column Defs - Table 3
  const topMembersPerSpecialtyColDefs = [
    {
      field: "topMembers",
      headerName: "Top Members",
      flex: 1,
      hide: showMembersProvidersGraph,
      cellRenderer: topMembersPerSpecialtyValue,
    },
    {
      field: "topMembers",
      headerName: "Top Members",
      flex: 1,
      hide: !showMembersProvidersGraph,
      cellRenderer: topMembersPerSpecialtyGraph,
    },
  ];

  //Top Providers Per Specialty Column Defs - Table 4
  const topProvidersPerSpecialtyColDefs = [
    {
      field: "providerName",
      headerName: "Top Providers",
      flex: 1,
      hide: showMembersProvidersGraph,
      cellRenderer: topProvidersPerSpecialtyValue,
    },
    {
      field: "totalPricePM",
      headerName: "Top Providers",
      flex: 1,
      hide: !showMembersProvidersGraph,
      cellRenderer: topProvidersPerSpecialtyGraph,
    },
  ];

  const handleMembersProvidersView = () => {
    setShowMembersProvidersGraph(!showMembersProvidersGraph);
  };

  const handleSpecialtyTableView = () => {
    setShowSpecialtyGraph(!showSpecialtyGraph);
  };

  const onSpecialtyChange = (fieldName, selectedValue) => {
    onSelectChange(selectedValue);
  };

  return (
    <>
      <div className="d-flex mt-2 justify-content-between">
        <div className="col-6 p-2">
          <h6 className="mb-4">Members Per Specialty</h6>
          <div
            className={"ag-theme-quartz"}
            style={{
              height: "250px",
            }}
          >
            <AgGridReact
              rowData={membersPerSpecialty}
              columnDefs={membersPerSpecialtyColDefs}
            />
          </div>
        </div>
        <div className="col-6 p-2">
          <div className="d-flex mb-0 justify-content-between">
            <h6 className="mb-4">Top 10 Specialties by cost</h6>

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
              rowData={topSpecialties}
              columnDefs={topFiveSpecialtiesByCostColDefs}
            />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="d-flex  mt-3 justify-content-between">
          <h6>Top Members & Providers per Specialty</h6>
          <span className="d-flex justify-content-end">
            <p>Provider Specialty</p>
            <span className="mx-2">{vector}</span>
            <span style={{ width: "150px" }}>
              <InputSelectField
                options={specialtyOptions}
                onChange={onSpecialtyChange}
              />
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
                rowData={topMembersPerSpecialty}
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
                rowData={topProvidersBySpecialty}
                columnDefs={topProvidersPerSpecialtyColDefs}
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default ProviderSpecialtyDetails;
