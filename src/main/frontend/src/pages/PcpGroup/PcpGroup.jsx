import React, { useState, useContext, useEffect } from "react";
import Navbar from "../../components/Navbar/Navbar.jsx";
import * as FileConstants from "../../constants/FileConstants";
import Filters from "../../components/Filters";
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import TabComponent from "../../components/TabComponent.jsx";
import PcpGroupSummary from "./PcpGroupSummary.jsx";
import PcpGroupDetails from "./PcpGroupDetails.jsx";
import { DataContext } from "../../context/DataContext.jsx";
import { pcpGroupFetch } from "../../services/ApiDataService.js";

const PcpGroup = () => {
  const [options, setOptions] = useState(FileConstants.formOptions);
  const [inputValues, setInputValues] = useState(FileConstants.formInputValues);
  const [selectedValue, setSelectedValue] = useState(
    FileConstants.formSelectedValues
  );
  const [rowData, setRowData] = useState();
  const monthMapping = FileConstants.monthMapping;
  const radioButtonoptions = FileConstants.radioButtonOptions;
  const [selectedOption, setSelectedOption] = useState("M");
  const { filterOptions, initialInputValues } = useContext(DataContext);
  const [toggle, setToggle] = useState(true);

  const [tabs, setTabs] = useState([
    {
      label: "Summary",
      content: <PcpGroupSummary rowData={rowData} />,
    },
    {
      label: "Details",
      content: <PcpGroupDetails />,
    },
  ]);

  useEffect(() => {
    setTabs([
      {
        label: "Summary",
        content: <PcpGroupSummary rowData={rowData} />,
      },
      {
        label: "Details",
        content: <PcpGroupDetails />,
      },
    ]);
  }, [rowData, inputValues, selectedValue]);

  useEffect(() => {
    if (filterOptions) {
      setOptions(filterOptions);
    }
    if (Object.keys(initialInputValues).length !== 0) {
      setInputValues(initialInputValues);
    }
  }, [filterOptions, initialInputValues]);

  useEffect(() => {
    fetchData();
  }, [toggle, options]);

  const useeffecttrigger = () => {
    setToggle((prevToggle) => !prevToggle);
  };

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

  const fetchData = async () => {
    try {
      const response = await pcpGroupFetch(inputValues);
      if (response.status === 200) {
        const dataArray = Object.entries(response.data).map(
          ([pcp_group, data]) => ({ pcp_group, ...data })
        );
        const transformedData = dataArray.map((item) => ({
          ...item,
          target_actual: [item.actual, item.target],
        }));
        setRowData(transformedData);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

  return (
    <>
      <div className="container-fluid h-100">
        <div className="row">
          <Navbar />
          <div className="column second-column overflow-auto px-3 py-2">
            <div className="row mb-2">
              <a href="/" className="text-decoration-none coc-primaryTeal">
                <i className="fa fa-arrow-left" aria-hidden="true"></i> Home
              </a>
            </div>
            <h3>PCP Group</h3>
            <div className="my-4">
              <TabComponent tabs={tabs} isSummaryDetail={true} />
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
          />
        </div>
      </div>
    </>
  );
};

export default PcpGroup;
