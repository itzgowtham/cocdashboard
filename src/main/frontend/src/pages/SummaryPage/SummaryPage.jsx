import React, { useContext, useEffect, useState } from "react";
import Navbar from "../../components/Navbar/Navbar";
import { PMPMfetch } from "../../services/ApiDataService";
import AreaChart from "../../components/AreaChart";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import { checkPercentage } from "../../utilities/FormatUtilities";
import Filters from "../../components/Filters";
import * as FileConstants from "../../constants/FileConstants";
import TabComponent from "../../components/TabComponent";
import { DataContext } from "../../context/DataContext";
import "./SummaryPage.css";

function SummaryPage() {
  const [toggle, setToggle] = useState(true);
  const [kpiMetrics, setKpimetrics] = useState(FileConstants.kpiMetricsconst);
  const [inputValues, setInputValues] = useState(FileConstants.formInputValues);
  const [selectedValue, setSelectedValue] = useState(
    FileConstants.formSelectedValues
  );
  const radioButtonOptions = FileConstants.radioButtonOptions;
  const [selectedOption, setSelectedOption] = useState("M");
  const monthMapping = FileConstants.monthMapping;
  const [labels, setLabels] = useState();
  const [areaData, setAreaData] = useState({ data1: [], data2: [] });
  const [graphType, setGraphType] = useState(
    FileConstants.targetVsActulaGraphtype
  );
  const [options, setOptions] = useState(FileConstants.formOptions);
  const { filterOptions } = useContext(DataContext);
  const { initialInputValues } = useContext(DataContext);

  useEffect(() => {
    setInputValues(initialInputValues);
  }, [initialInputValues]);
  let tabs = [
    {
      label: "Paid Expense PMPM",
      content: (
        <AreaChart
          className="mt-7"
          labels={labels}
          data={areaData}
          graphtype={graphType}
        />
      ),
    },
    {
      label: "MLR Trend",
      content: (
        <AreaChart
          className="mt-7"
          labels={labels}
          data={areaData}
          graphtype={graphType}
        />
      ),
    },
  ];

  useEffect(() => {
    if (filterOptions) {
      setOptions(filterOptions);
    }
  }, [filterOptions]);

  const useeffecttrigger = () => setToggle((prevToggle) => !prevToggle);
  const handleChange = (fieldName, selectedValue) => {
    console.log(selectedValue);
    if (fieldName === "startMonth") {
      setSelectedOption(selectedValue);
      if (selectedValue === "YTD") {
        const year = inputValues.endMonth.slice(-4);
        const startMonth = `Jan ${year}`;
        setInputValues({
          ...inputValues,
          startMonth: startMonth,
        });
      } else {
        const selectedMonth = radioButtonOptions.find(
          (option) => option.label === selectedValue
        );
        const endMonthIndex = options.endMonth.findIndex(
          (option) =>
            option.label === inputValues.endMonth &&
            option.value === inputValues.endMonth
        );
        const startMonthIndex =
          endMonthIndex + monthMapping[selectedMonth.label];
        if (startMonthIndex <= options.endMonth.length) {
          const startMonth = options?.endMonth[startMonthIndex]?.value;
          setInputValues({
            ...inputValues,
            startMonth: startMonth,
          });
        } else {
          const startMonth = inputValues.endMonth;
          setInputValues({
            ...inputValues,
            startMonth: startMonth,
          });
        }
      }
    } else if (fieldName === "endMonth") {
      setSelectedOption("M");
      setInputValues({
        ...inputValues,
        startMonth: "",
        [fieldName]: selectedValue,
      });
    } else {
      setInputValues({ ...inputValues, [fieldName]: selectedValue });
      setSelectedValue(inputValues);
    }
  };

  const handleReset = () => {
    setSelectedOption("M");
    setInputValues(FileConstants.formInputValues);
    setSelectedValue(FileConstants.formSelectedValues);
    useeffecttrigger();
  };

  useEffect(() => {
    const fetchPMPMData = async () => {
      try {
        const response2 = await PMPMfetch(inputValues);
        if (response2.status === 200) {
          setKpimetrics({
            memberMonths: response2.data.kpimetrics.memberMonths,
            PMPMExpense: response2.data.kpimetrics.pmpm,
            endingMembers: response2.data.kpimetrics.endingMembers,
            memberMonthsPercentageChange:
              response2.data.kpimetrics.memberMonthsPercentageChange,
            endingMembersPercentageChange:
              response2.data.kpimetrics.endingMembersPercentageChange,
            pmpmPercentageChange:
              response2.data.kpimetrics.pmpmPercentageChange,
            targetEndingMembers: response2.data.kpimetrics.targetEndingMembers,
            targetMemberMonths: response2.data.kpimetrics.targetMemberMonths,
            targetPMPMExpense: response2.data.kpimetrics.targetPmpm,
          });
          if (
            inputValues.graphType === "Target vs Actual" ||
            inputValues.graphType === ""
          ) {
            const arealabels = Object.keys(response2.data.areaChart.actual).map(
              (element) => element.substring(0, 3)
            );
            setLabels(arealabels);
            setGraphType({ type1: "Target", type2: "Actual" });
            setAreaData({
              data1: Object.values(response2.data.areaChart.target),
              data2: Object.values(response2.data.areaChart.actual),
            });
          } else {
            const arealabels = Object.keys(
              response2.data.areaChart.current
            ).map((element) => element.substring(0, 3));
            setLabels(arealabels);
            setGraphType({ type1: "Prior", type2: "Current" });
            setAreaData({
              data1: Object.values(response2.data.areaChart.previous),
              data2: Object.values(response2.data.areaChart.current),
            });
          }
        } else {
          console.log("PMPMfetch failed with status code " + response2.status);
        }
      } catch (error) {
        console.log("Could not fetch PMPM data: " + error);
      }
    };
    if (inputValues) {
      fetchPMPMData();
    }
  }, [toggle]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    useeffecttrigger();
  };

  return (
    <div className="container-fluid h-100 m-0">
      <div className="row">
        <Navbar />
        <div className="column second-column overflow-auto">
          <div className="col-12 p-2">
            <div className="row mb-2">
              <a href="/" className="text-decoration-none coc-primaryTeal">
                <i className="fa fa-arrow-left" aria-hidden="true"></i> Home
              </a>
            </div>

            <h4 className="m-0">Summary</h4>
            <div className="row mb-0"></div>
            <div className="row d-flex">
              <div className="col-12 p-2 align-items-center">
                <div className="d-flex justify-content-between">
                  <h5 className="mb-3">Performance Highlights</h5>
                  <span>
                    <i
                      className="fa fa-circle me-1"
                      style={{ color: "#004F59" }}
                      aria-hidden="true"
                    ></i>
                    {graphType.type1}{" "}
                    <i
                      className="fa fa-circle ms-2 me-1"
                      style={{ color: "#6FC2B4" }}
                      aria-hidden="true"
                    ></i>
                    {graphType.type2}
                  </span>
                </div>
                <div className="col-12 align-items-center ">
                  <div className="d-flex row mb-0">
                    <div className="col-8">
                      <HorizontalBarChart
                        actualvalue={kpiMetrics.endingMembers}
                        targetvalue={kpiMetrics.targetEndingMembers}
                        label={"Ending Membership"}
                        type={"number"}
                        graphtype={graphType}
                        aspectRatio={7}
                        graphLength={2}
                        height={"90px"}
                      />
                    </div>
                    <div className="col-4 mt-4 pt-3">
                      {checkPercentage(
                        kpiMetrics.endingMembersPercentageChange
                      )}
                    </div>
                  </div>
                  <div className="d-flex row mb-0">
                    <div className="col-8">
                      <HorizontalBarChart
                        actualvalue={kpiMetrics.memberMonths}
                        targetvalue={kpiMetrics.targetMemberMonths}
                        label={"Member Months"}
                        type={"number"}
                        graphtype={graphType}
                        aspectRatio={7}
                        graphLength={2}
                        height={"90px"}
                      />
                    </div>
                    <div className="col-4 mt-4 pt-3">
                      {checkPercentage(kpiMetrics.memberMonthsPercentageChange)}
                    </div>
                  </div>
                  <div className="d-flex row mb-0">
                    <div className="col-8">
                      <HorizontalBarChart
                        actualvalue={kpiMetrics.PMPMExpense}
                        targetvalue={kpiMetrics.targetPMPMExpense}
                        label="Paid Expense PMPM"
                        type={"dollar"}
                        graphtype={graphType}
                        aspectRatio={7}
                        graphLength={2}
                        height={"90px"}
                      />
                    </div>
                    <div className="col-4 mt-4 pt-3">
                      {checkPercentage(kpiMetrics.pmpmPercentageChange)}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="d-flex justify-content-between mt-2 mb-0">
              <h5>Month over Month Trends</h5>
              <span>
                <i
                  className="fa fa-circle me-1"
                  style={{ color: "#004F59" }}
                  aria-hidden="true"
                ></i>
                {graphType.type1}{" "}
                <i
                  className="fa fa-circle ms-2 me-1"
                  style={{ color: "#6FC2B4" }}
                  aria-hidden="true"
                ></i>
                {graphType.type2}
              </span>
            </div>
            <div className="row mb-0">
              <TabComponent tabs={tabs} />
            </div>
          </div>
        </div>
        <Filters
          FormSelectOptions={options}
          FormSelectValues={selectedValue}
          onChange={handleChange}
          handleSubmit={handleSubmit}
          handleReset={handleReset}
          RadioButtonOptions={radioButtonOptions}
          RadioSelectedOption={selectedOption}
        ></Filters>
      </div>
    </div>
  );
}
export default SummaryPage;
