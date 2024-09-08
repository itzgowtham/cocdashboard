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

const PcpGroupDetails = (props) => {
  const {
    membersPerPcpGroup,
    topMembersByCostForEachPcp,
    topPcpByCost,
    topPcpByCostForEachSpeciality,
    pcpOptions,
    specialtyOptions,
    onPcpChange,
    onSpecialtyChange,
  } = props;
  const [showTopMembersGraph, setShowTopMembersGraph] = useState(true);
  const [showTopPcpGraph, setShowTopPcpGraph] = useState(true);
  const [showTopPcpSpecialtyGraph, setShowTopPcpSpecialtyGraph] =
    useState(true);
  // const maxValueMembersPerPcpGroup = membersPerPcpGroup[0]?.totalPricePM;
  // const minValueMembersPerPcpGroup =
  //   membersPerPcpGroup[membersPerPcpGroup.length - 1]?.totalPricePM;
  const maxValueTopMembersByCostForEachPcp =
    topMembersByCostForEachPcp[0]?.totalPricePM;
  const maxValueTopPcpByCost = topPcpByCost[0]?.totalPricePM;
  const maxValueTopPcpByCostForEachSpeciality =
    topPcpByCostForEachSpeciality[0]?.totalPricePM;

  //TMembers Per PCP Group
  // const handleMembersPerPcpGroupGraph = (params) => {
  //   const specialtyName = params?.data?.speciality;
  //   const specialtyValue = params?.data?.totalPricePM;
  //   return (
  //     <div className="d-flex">
  //       <p style={{ flex: 2 }}>
  //         <TooltipComponent
  //           value={specialtyName}
  //           length={28}
  //           children={specialtyName}
  //         />
  //       </p>
  //       <span style={{ flex: 2 }}>
  //         <HorizontalBarChart
  //           actualvalue={specialtyValue}
  //           type={"number"}
  //           graphtype={{ type1: "Target", type2: "Actual" }}
  //           aspectRatio={6}
  //           graphLength={1}
  //           height={"38px"}
  //           maxValue={maxValueMembersPerPcpGroup}
  //           minValue={0}
  //         />
  //       </span>
  //     </div>
  //   );
  // };
  // const handleMembersPerPcpGroupValue = (params) => {
  //   const specialtyName = params?.data?.speciality;
  //   const specialtyValue = params?.data?.totalPricePM;
  //   return (
  //     <div className="d-flex">
  //       <p style={{ flex: 2 }}>
  //         {" "}
  //         <TooltipComponent
  //           value={specialtyName}
  //           length={45}
  //           children={specialtyName}
  //         />
  //       </p>
  //       <span style={{ flex: 1 }}>{specialtyValue}</span>
  //     </div>
  //   );
  // };

  //Top 10 Members by cost for each PCP
  const handleTopMembersByCostForEachPcpGraph = (params) => {
    const name = params?.data?.memberUid;
    const value = params?.data?.totalPricePM;
    return (
      <div className="d-flex">
        <p className="col-4">{name}</p>
        <span className="col-8">
          <HorizontalBarChart
            actualvalue={value}
            type={"number"}
            graphtype={{ type1: "Target", type2: "Actual" }}
            aspectRatio={6}
            graphLength={1}
            height={"38px"}
            maxValue={maxValueTopMembersByCostForEachPcp}
            minValue={0}
          />
        </span>
      </div>
    );
  };
  const handleTopMembersByCostForEachPcpValue = (params) => {
    const name = params?.data?.memberUid;
    const value = formatNumberColour(params?.data?.totalPricePM);
    return (
      <div className="d-flex">
        <p className="col-6">{name}</p>
        <span className="col-6">{value}</span>
      </div>
    );
  };

  //Top 10 PCP by Cost
  const handleTopPcpByCostGraph = (params) => {
    const specialtyName = params?.data?.providerName;
    const specialtyValue = params?.data?.totalPricePM;
    return (
      <div className="d-flex">
        <p className="col-5">
          <TooltipComponent
            value={specialtyName}
            length={25}
            children={specialtyName}
          />
        </p>
        <span className="col-7">
          <HorizontalBarChart
            actualvalue={specialtyValue}
            type={"number"}
            graphtype={{ type1: "Target", type2: "Actual" }}
            aspectRatio={6}
            graphLength={1}
            height={"38px"}
            maxValue={maxValueTopPcpByCost}
            minValue={0}
          />
        </span>
      </div>
    );
  };
  const handleTopPcpByCostValue = (params) => {
    const specialtyName = params?.data?.providerName;
    const specialtyValue = formatNumberColour(params?.data?.totalPricePM);
    return (
      <div className="d-flex">
        <p className="col-8">
          <TooltipComponent
            value={specialtyName}
            length={45}
            children={specialtyName}
          />
        </p>
        <span className="col-4">{specialtyValue}</span>
      </div>
    );
  };

  //Top 10 PCP by cost for each specialty
  const handleTopPcpByCostForEachSpecialityGraph = (params) => {
    const name = params?.data?.providerName;
    const value = params?.data?.totalPricePM;
    return (
      <div className="d-flex">
        <p className="col-5">
          <TooltipComponent value={name} length={23} children={name} />
        </p>
        <span className="col-7">
          <HorizontalBarChart
            actualvalue={value}
            type={"number"}
            graphtype={{ type1: "Target", type2: "Actual" }}
            aspectRatio={6}
            graphLength={1}
            height={"38px"}
            maxValue={maxValueTopPcpByCostForEachSpeciality}
            minValue={0}
          />
        </span>
      </div>
    );
  };
  const handleTopPcpByCostForEachSpecialityValue = (params) => {
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

  const membersPerPcpGroupColDefs = [
    {
      field: "providerName",
      headerName: "PCP Provider Group",
      flex: 1,
    },
    {
      field: "totalMembers",
      headerName: "Members",
      flex: 1,
    },
  ];

  const topMembersByCostForEachPcpColDefs = [
    {
      field: "speciality",
      headerName: "Top Members",
      flex: 1,
      hide: showTopMembersGraph,
      cellRenderer: handleTopMembersByCostForEachPcpValue,
    },
    {
      field: "totalPricePM",
      headerName: "Top Members",
      flex: 1,
      hide: !showTopMembersGraph,
      cellRenderer: handleTopMembersByCostForEachPcpGraph,
    },
  ];

  const topPcpByCostColDefs = [
    {
      field: "speciality",
      headerName: "Top PCP",
      flex: 1,
      hide: showTopPcpGraph,
      cellRenderer: handleTopPcpByCostValue,
    },
    {
      field: "totalPricePM",
      headerName: "Top PCP",
      flex: 1,
      hide: !showTopPcpGraph,
      cellRenderer: handleTopPcpByCostGraph,
    },
  ];

  const topPcpByCostForEachSpecialityColDefs = [
    {
      field: "providerName",
      headerName: "Top PCP",
      flex: 1,
      hide: showTopPcpSpecialtyGraph,
      cellRenderer: handleTopPcpByCostForEachSpecialityValue,
    },
    {
      field: "totalPricePM",
      headerName: "Top PCP",
      flex: 1,
      hide: !showTopPcpSpecialtyGraph,
      cellRenderer: handleTopPcpByCostForEachSpecialityGraph,
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

  const onPcpSelectChange = (fieldName, selectedValue) => {
    onPcpChange(selectedValue);
  };

  const onSpecialtySelectChange = (fieldName, selectedValue) => {
    onSpecialtyChange(selectedValue);
  };

  return (
    <>
      <div className="d-flex mt-2 justify-content-between">
        <div className="col-6 p-2">
          <h6 style={{ marginBottom: "28px" }}>Members Per PCP Group</h6>
          <div
            className={"ag-theme-quartz"}
            style={{
              height: "250px",
            }}
          >
            <AgGridReact
              rowData={membersPerPcpGroup}
              columnDefs={membersPerPcpGroupColDefs}
            />
          </div>
        </div>
        <div className="col-6 p-2">
          <div className="d-flex justify-content-between">
            <h6 className="col-5">Top 10 Members by cost for each PCP</h6>
            <span className="col-7 d-flex justify-content-end">
              <p>PCP</p>
              <span className="mx-1">{vector}</span>
              <span style={{ width: "100px" }}>
                <InputSelectField
                  options={pcpOptions}
                  onChange={onPcpSelectChange}
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
              rowData={topMembersByCostForEachPcp}
              columnDefs={topMembersByCostForEachPcpColDefs}
            />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="d-flex justify-content-between">
          <div className="col-6 p-2">
            <span className="d-flex justify-content-between">
              <h6 style={{ marginBottom: "24px" }}>Top 10 PCP by Cost</h6>
              <div onClick={handleTopPcpTableView} className="coc-cursor ms-3">
                View: {showTopPcpGraph ? graphSVG : "123"}
              </div>
            </span>
            <div
              className={"ag-theme-quartz mt-1"}
              style={{
                height: "250px",
              }}
            >
              <AgGridReact
                rowData={topPcpByCost}
                columnDefs={topPcpByCostColDefs}
              />
            </div>
          </div>
          <div className="col-6 p-2">
            <div className="d-flex justify-content-between">
              <h6 className="col-4">Top 5 PCP by cost by Speciality</h6>
              <span className="col-7 d-flex justify-content-end">
                <p>Specialty</p>
                <span className="mx-1">{vector}</span>
                <span style={{ width: "80px" }}>
                  <InputSelectField
                    options={specialtyOptions}
                    onChange={onSpecialtySelectChange}
                  />
                </span>
                <div
                  onClick={handleTopPcpSpecialtyTableView}
                  className="coc-cursor ms-1"
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
                rowData={topPcpByCostForEachSpeciality}
                columnDefs={topPcpByCostForEachSpecialityColDefs}
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default PcpGroupDetails;
