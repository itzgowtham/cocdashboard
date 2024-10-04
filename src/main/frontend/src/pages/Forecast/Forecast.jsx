import React, { useEffect, useState, useContext } from "react";
import Navbar from "../../components/Navbar/Navbar.jsx";
import RadioButtons from "../../components/RadioButtons.jsx";
import * as FileConstants from "../../constants/FileConstants.jsx";
import Filters from "../../components/Filters.jsx";
import { graphSVG } from "../../assets/images/svg/SVGIcons.jsx";
import ForecastData from "./ForecastData.jsx";
import { DataContext } from "../../context/DataContext.jsx";
import { forecastFetch } from "../../services/ApiDataService.js";
import Chatbot from "../../components/ChatBot.jsx";
import "./Forecast.css";

const Forecast = () => {
  const [options, setOptions] = useState(FileConstants.formOptions);
  const [inputValues, setInputValues] = useState(FileConstants.formInputValues);
  const [selectedValue, setSelectedValue] = useState(
    FileConstants.formSelectedValues
  );
  const [toggle, setToggle] = useState(true);
  const forecastOptions = ["Cost", "Member Months"];
  const [selectedForecastOption, setSelectedForecastOption] = useState("Cost");
  const { filterOptions, initialInputValues } = useContext(DataContext);
  const [selectedOption, setSelectedOption] = useState("M");
  const [showGraph, setShowGraph] = useState(false);
  const monthMapping = FileConstants.monthMapping;
  const radioButtonoptions = FileConstants.radioButtonOptions;
  const [forecastMemberData, setForecastMemberData] = useState([]);
  const [forecastCostData, setForecastCostData] = useState([]);

  // const tabs = [
  //   {
  //     label: "Summary",
  //     content: <CareProviderSummary />,
  //   },
  //   {
  //     label: "Details",
  //     content: <CareProviderDetails/>
  //   },
  // ];

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
        const selectedMonth = radioButtonoptions.find(
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
    }
  };

  const fetchData = async () => {
    try {
      const response = await forecastFetch(inputValues);
      if (response.status === 200) {
        console.log("FORECAST DATA", response.data);
        const forecastCostDataArray = Object.entries(response.data.pmpm).map(
          ([forecast, data]) => ({ forecast, ...data })
        );
        const forecastMemberDataArray = Object.entries(
          response.data.member
        ).map(([forecast, data]) => ({ forecast, ...data }));
        setForecastCostData(forecastCostDataArray);
        setForecastMemberData(forecastMemberDataArray);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [toggle, options]);

  const useeffecttrigger = () => {
    setToggle((prevToggle) => !prevToggle);
  };

  const handleReset = () => {
    setSelectedOption("M");
    setInputValues(FileConstants.formInputValues);
    setSelectedValue(FileConstants.formSelectedValues);
    useeffecttrigger();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    useeffecttrigger();
  };

  useEffect(() => {
    if (filterOptions) {
      setOptions(filterOptions);
    }
    if (Object.keys(initialInputValues).length !== 0) {
      setInputValues(initialInputValues);
    }
  }, [filterOptions, initialInputValues]);

  const handleTableView = () =>
    showGraph === false ? setShowGraph(true) : setShowGraph(false);

  const buttonChange = (value) => {
    setSelectedForecastOption(value);
  };

  return (
    <div className="container-fluid h-100 m-0">
      <div className="row">
        <Navbar />
        <div className="column second-column overflow-auto forecast px-3 py-2">
          <div className="row mb-2">
            <a href="/" className="text-decoration-none coc-primaryTeal">
              <i className="fa fa-arrow-left" aria-hidden="true"></i> Home
            </a>
          </div>
          <h3>Forecast</h3>
          <div className="header mb-2 mt-3">
            <strong>{selectedForecastOption}</strong>
            <span className="d-flex">
              <RadioButtons
                options={forecastOptions}
                selectedOption={selectedForecastOption}
                onChange={buttonChange}
              />
              <div className="vertical-divider"></div>
              <div onClick={handleTableView} className="coc-cursor">
                <strong>View:</strong> {showGraph ? graphSVG : "123"}
              </div>
            </span>
          </div>
          <ForecastData
            showGraph={showGraph}
            rowData={
              selectedForecastOption === "Cost"
                ? forecastCostData
                : forecastMemberData
            }
            selectedOption={selectedForecastOption}
          />
           <div className="d-flex mt-2 justify-content-between">
              <p></p>
              <Chatbot/>
            </div>
        </div>
        <Filters
          FormSelectOptions={options}
          FormSelectValues={selectedValue}
          onChange={handleChange}
          handleSubmit={handleSubmit}
          handleReset={handleReset}
          RadioButtonOptions={radioButtonoptions}
          RadioSelectedOption={selectedOption}
          isTypeDisabled={true}
          isMonthDisabled={true}
        />
      </div>
    </div>
  );
};
export default Forecast;
