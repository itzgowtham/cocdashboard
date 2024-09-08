import React, { useState, useEffect } from "react";
import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import { graphSVG } from "../../assets/images/svg/SVGIcons";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import { vector } from "../../assets/images/svg/SVGIcons";
import InputSelectField from "../../components/InputSelectFiled";
import { serviceRegionDetailsFetch } from "../../services/ApiDataService";
import { formatNumberColour } from "../../utilities/FormatUtilities";
import { DataContext } from "../../context/DataContext";
import { useContext } from "react";
import TooltipComponent from "../../components/TooltipComponent";

function ServiceRegionDetails(props) {
  const { inputValues } = props;
  const [regionBreakdownData, setRegionBreakdownData] = useState([]);
  const [topMembersData, setTopMembersData] = useState([]);
  const [showGraph, setShowGraph] = useState(false);
  const { filterOptions } = useContext(DataContext);
  const { state } = filterOptions;
  const [minMaxValues, setMinMaxValues] = useState({
    minProviderVal: 0,
    maxProviderVal: 0,
    minMembersVal: 0,
    maxMembersVal: 0,
  });

  const handleGraph = (props) => {
    const { name, value, minValue, maxValue } = props;

    return (
      <div className="d-flex">
        <p className="col-6">
          <TooltipComponent
            value={name}
            length={30}
            children={name}
          ></TooltipComponent>
        </p>
        <span className="col-6">
          {name !== "" && (
            <HorizontalBarChart
              actualvalue={value}
              type={"number"}
              // graphtype={{ type1: "Target", type2: "Actual" }}
              aspectRatio={6}
              graphLength={1}
              height={"38px"}
              maxValue={maxValue}
              minValue={minValue}
            />
          )}
        </span>
      </div>
    );
  };
  const handleTableView = () => {
    setShowGraph(!showGraph);
  };


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
  const serviceregionBreakdownColDefs = [
    {
      field: "service_area_state",
      headerName: "Service Area State",
      flex: 1,
    },
    {
      field: "totalMembers",
      headerName: "Members",
      flex: 1,
    },
    {
      field: "totalProvidersCount",
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

  const formatRegionData = (data) => {
    const formattedData = [];
    for (let item in data) {
      formattedData.push({ service_area_state: item, ...data[item] });
    }

    return formattedData;
  };

  const formatTopMembersData = (data) => {
    let formattedData = [];
    let memLen = data["topMembersPerServiceRegion"].length;
    let providerLen = data["topProvidersPerServiceRegion"].length;
    const memData = data["topMembersPerServiceRegion"];
    const providerData = data["topProvidersPerServiceRegion"];

    if (memLen > providerLen) {
      formattedData = memData.map((item, i) => {
        console.log(i > providerLen, i, providerLen);
        return {
          memberId: memData[i] ? memData[i].memberUid.toString() : "",
          memberTotalPricePM: memData[i] ? memData[i].totalPricePM : "",
          providerName: i < providerLen ? providerData[i].providerName : "",
          totalPricePM: i < providerLen ? providerData[i].totalPricePM : "",
        };
      });
    } else {
      formattedData = providerData.map((item, i) =>
        Object.assign({}, item, {
          memberId: memData[i] ? memData[i].memberUid.toString() : "",
          memberTotalPricePM: memData[i] ? memData[i].totalPricePM : "",
        })
      );
    }

    let max = 0,
      min = 100000,
      memMinVal = 100000,
      memMaxVal = 0;
    for (let item in formattedData) {
      const val = formattedData[item].totalPricePM;
      const memVal = formattedData[item].memberTotalPricePM;
      if (val > max) {
        max = val;
      } else if (val < min) {
        min = val;
      }
      if (memVal > memMaxVal) {
        memMaxVal = memVal;
      } else if (memVal < memMinVal) {
        memMinVal = memVal;
      }
    }
    setMinMaxValues({
      minProviderVal: min,
      maxProviderVal: max,
      minMembersVal: memMinVal,
      maxMembersVal: memMaxVal,
    });

    return formattedData;
  };

  const fetchData = async () => {
    try {
      const res = await serviceRegionDetailsFetch({
        ...inputValues
      });
      if (res.status === 200) {
        setRegionBreakdownData(
          formatRegionData(res.data.serviceRegionBreakdown)
        );
        setTopMembersData(
          formatTopMembersData({
            topMembersPerServiceRegion: res.data.topMembersPerServiceRegion,
            topProvidersPerServiceRegion: res.data.topProvidersPerServiceRegion,
          })
        );
      } else {
        console.log("service region failed with status code " + res.status);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [inputValues]);

  const topMembersProvidersColDefs = [
    {
      field: "memberId",
      headerName: "Members",
      flex: 1,
      hide: showGraph,
      cellRenderer: (props) => {
        const { data } = props;
        return data ? (
          <div className="d-flex">
            <p className="col-6">
              {" "}
              <TooltipComponent
                value={data.memberId}
                length={30}
                children={data.memberId}
              ></TooltipComponent>
            </p>
            <span className="col-6">
              {data.memberTotalPricePM
                ? formatNumberColour(data.memberTotalPricePM)
                : ""}
            </span>
          </div>
        ) : (
          ""
        );
      },
    },
    {
      field: "providerName",
      headerName: "Providers",
      flex: 1,
      hide: showGraph,
      cellRenderer: (props) => {
        const { data } = props;
        return data ? (
          <div className="d-flex">
            <p className="col-6">
              {" "}
              <TooltipComponent
                value={data.providerName}
                length={30}
                children={data.providerName}
              ></TooltipComponent>
            </p>
            <span className="col-6">
              {data.totalPricePM ? formatNumberColour(data.totalPricePM) : ""}
            </span>
          </div>
        ) : (
          ""
        );
      },
    },
    {
      field: "members",
      headerName: "Members",
      flex: 1,
      hide: !showGraph,
      cellRenderer: (props) => {
        const { data } = props;
        return handleGraph({
          name: data.memberId,
          value: data.memberTotalPricePM ? data.memberTotalPricePM : 0,
          maxValue: minMaxValues.maxMembersVal,
          minValue: minMaxValues.minMembersVal,
        });
      },
    },
    {
      field: "providerName",
      headerName: "Providers",
      flex: 1,
      hide: !showGraph,
      cellRenderer: (props) => {
        const { data } = props;
        return handleGraph({
          name: data.providerName,
          value: data.totalPricePM ? data.totalPricePM : 0,
          maxValue: minMaxValues.maxProviderVal,
          minValue: minMaxValues.minProviderVal,
        });
      },
    },
  ];

  return (
    <>
      <h6 className="my-3">
        <strong>Service Region Breakdown</strong>
      </h6>
      <div
        className={"ag-theme-quartz"}
        style={{
          height: "250px",
        }}
      >
        <AgGridReact
          rowData={regionBreakdownData}
          columnDefs={serviceregionBreakdownColDefs}
        />
      </div>
      <div className="d-flex mt-3 mb-2">
        <span className="col-6">
          <strong>Top 10 Members & Providers per Service Region</strong>
        </span>
        <div className="col-6 d-flex justify-content-end">
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
          rowData={topMembersData}
          columnDefs={topMembersProvidersColDefs}
        />
      </div>
    </>
  );
}

export default ServiceRegionDetails;
