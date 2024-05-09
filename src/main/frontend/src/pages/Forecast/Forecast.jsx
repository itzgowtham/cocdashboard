import React, { useEffect, useState, useContext } from "react";
import Navbar from "../../components/Navbar/Navbar.jsx";
import RadioButtons from "../../components/RadioButtons.jsx";
import * as FileConstants from "../../constants/FileConstants.jsx";
import Filters from "../../components/Filters.jsx";
import "./Forecast.css";
import { graphSVG } from "../../assets/images/svg/SVGIcons.jsx";
import ForecastData from "./ForecastData.jsx";
import { DataContext } from "../../context/DataContext.jsx";

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

  const customerData = [
    {
      care_provider: "Provider 1",
      target: 5,
      actual: 1,
      difference: 1,
      difference_: 3,
    },
    {
      care_provider: "Provider 2",
      target: 5,
      actual: 2,
      difference: 2,
      difference_: 2,
    },
    {
      care_provider: "Provider 3",
      target: 5,
      actual: 3,
      difference: 3,
      difference_: 4,
    },
    {
      care_provider: "Provider 4",
      target: 5,
      actual: 4,
      difference: 4,
      difference_: 8,
    },
    {
      care_provider: "Provider 5",
      target: 5,
      actual: 4,
      difference: 4,
      difference_: 8,
      target_actual: 4,
    },
  ];

  const forecastData = [
    {
      period: "Jan 2023",
      forecast_value: 123,
      confidence_interval: 5,
    },
    {
      period: "Feb 2023",
      forecast_value: 567,
      confidence_interval: 12,
    },
    {
      period: "Mar 2023",
      forecast_value: 234,
      confidence_interval: 4,
    },
    {
      period: "Apr 2023",
      forecast_value: 123,
      confidence_interval: 7,
    },
    {
      period: "May 2023",
      forecast_value: 876,
      confidence_interval: 5,
    },
    {
      period: "Jun 2023",
      forecast_value: 142,
      confidence_interval: 6,
    },
    {
      period: "Jul 2023",
      forecast_value: 123,
      confidence_interval: 5,
    },
    {
      period: "Aug 2023",
      forecast_value: 512,
      confidence_interval: 9,
    },
    {
      period: "Sep 2023",
      forecast_value: 170,
      confidence_interval: 2,
    },
    {
      period: "Oct 2023",
      forecast_value: 340,
      confidence_interval: 8,
    },
  ];

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
            rowData={forecastData}
            selectedOption={selectedForecastOption}
          />
        </div>
        <Filters
          FormSelectOptions={options}
          FormSelectValues={selectedValue}
          onChange={handleChange}
          handleSubmit={handleSubmit}
          handleReset={handleReset}
          RadioButtonOptions={radioButtonoptions}
          RadioSelectedOption={selectedOption}
        />
      </div>
    </div>
  );
};
export default Forecast;
