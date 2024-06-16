import { useState, useEffect, useContext } from "react";
import * as FileConstants from "../../constants/FileConstants";
import Navbar from "../../components/Navbar/Navbar";
import TabComponent from "../../components/TabComponent";
import ServiceRegionSummary from "./ServiceRegionSummary";
import Filters from "../../components/Filters";
import { DataContext } from "../../context/DataContext";
import ServiceRegionDetails from "./ServiceRegionDetails";

const ServiceRegion = () => {
  const [inputValues, setInputValues] = useState(FileConstants.formInputValues);
  const [options, setOptions] = useState(FileConstants.formOptions);
  const monthMapping = FileConstants.monthMapping;
  const radioButtonOptions = FileConstants.radioButtonOptions;
  const [selectedOption, setSelectedOption] = useState("M");
  const [toggle, setToggle] = useState(true);
  const { filterOptions, initialInputValues } = useContext(DataContext);
  const [tabs, setTabs] = useState([
    {
      label: "Summary",
      content: <ServiceRegionSummary inputValues={inputValues} />,
    },
    {
      label: "Details",
      content: <ServiceRegionDetails />,
    },
  ]);

  const useeffecttrigger = () => setToggle((prevToggle) => !prevToggle);

  const handleSubmit = async (e) => {
    e.preventDefault();
    useeffecttrigger();
  };

  const handleReset = () => {
    setSelectedOption("M");
    setInputValues(FileConstants.formInputValues);
    useeffecttrigger();
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
          const startMonth = options.endMonth[options.endMonth.length-1].value;
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

  useEffect(() => {
    if (filterOptions) {
      setOptions(filterOptions);
    }
    if (Object.keys(initialInputValues).length !== 0) {
      setInputValues(initialInputValues);
    }
  }, [filterOptions, initialInputValues, options]);

  useEffect(() => {
    setTabs([
      {
        label: "Summary",
        content: <ServiceRegionSummary inputValues={inputValues} />,
      },
      {
        label: "Details",
        content: <ServiceRegionDetails />,
      },
    ]);
  }, [toggle]);

  return (
    <div className="container-fluid h-100 m-0">
      <div className="row">
        <Navbar />
        <div className="column second-column overflow-auto px-3 py-2">
          <div className="row mb-2">
            <a href="/" className="text-decoration-none coc-primaryTeal">
              <i className="fa fa-arrow-left" aria-hidden="true"></i> Home
            </a>
          </div>
          <h3>Service Region</h3>
          <div className="my-4">
            <TabComponent tabs={tabs} isSummaryDetail={true} />
          </div>
        </div>
        <Filters
          FormSelectOptions={options}
          FormSelectValues={inputValues}
          onChange={handleChange}
          handleSubmit={handleSubmit}
          handleReset={handleReset}
          RadioButtonOptions={radioButtonOptions}
          RadioSelectedOption={selectedOption}
        />
      </div>
    </div>
  );
};

export default ServiceRegion;
