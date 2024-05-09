import React, { useEffect, useState, useContext } from "react";
import TabComponent from "../../components/TabComponent.jsx";
import * as FileConstants from "../../constants/FileConstants";
import Navbar from "../../components/Navbar/Navbar.jsx";
import { providerSpecialtyFetch } from "../../services/ApiDataService.js";
import Filters from "../../components/Filters";
import ProviderSpecialtySummary from "./ProviderSpecialtySummary.jsx";
import ProviderSpecialtyDetails from "./ProviderSpecialtyDetails.jsx";
import { DataContext } from "../../context/DataContext";

const ProviderSpecialty = () => {
  const [inputValues, setInputValues] = useState(FileConstants.formInputValues);
  const [options, setOptions] = useState(FileConstants.formOptions);
  const monthMapping = FileConstants.monthMapping;
  const radioButtonOptions = FileConstants.radioButtonOptions;
  const [selectedOption, setSelectedOption] = useState("M");
  const [InPatientrowdata, setInPatientRowdata] = useState();
  const [OutPatientrowdata, setOutPatientRowdata] = useState();
  const [toggle, setToggle] = useState(true);
  const { filterOptions, initialInputValues } = useContext(DataContext);

  const [tabs, setTabs] = useState([
    {
      label: "Summary",
      content: (
        <ProviderSpecialtySummary
          InpatientDataArray={InPatientrowdata}
          OutpatientDataArray={OutPatientrowdata}
        />
      ),
    },
    {
      label: "Details",
      content: <ProviderSpecialtyDetails />,
    },
  ]);

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
    }
  };

  const handleReset = () => {
    setSelectedOption("M");
    setInputValues(FileConstants.formInputValues);
  };

  const useeffecttrigger = () => setToggle((prevToggle) => !prevToggle);

  const handleSubmit = async (e) => {
    e.preventDefault();
    useeffecttrigger();
  };

  const fetchData = async () => {
    try {
      const response2 = await providerSpecialtyFetch(inputValues);
      if (response2.status === 200) {
        const InpatientDataArray = Object.entries(response2.data.InPatient).map(
          ([provider_specialty, data]) => ({ provider_specialty, ...data })
        );
        const OutpatientDataArray = Object.entries(
          response2.data.OutPatient
        ).map(([provider_specialty, data]) => ({
          provider_specialty,
          ...data,
        }));
        const transformedData = InpatientDataArray.map((item) => ({
          ...item,
          target_actual: [item.actual, item.target],
        }));
        const transformedData2 = OutpatientDataArray.map((item) => ({
          ...item,
          target_actual: [item.actual, item.target],
        }));
        setInPatientRowdata(transformedData);
        setOutPatientRowdata(transformedData2);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

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

  useEffect(() => {
    setTabs([
      {
        label: "Summary",
        content: (
          <ProviderSpecialtySummary
            InpatientDataArray={InPatientrowdata}
            OutpatientDataArray={OutPatientrowdata}
          />
        ),
      },
      {
        label: "Details",
        content: <ProviderSpecialtyDetails />,
      },
    ]);
  }, [InPatientrowdata, OutPatientrowdata, inputValues]);

  return (
    <>
      <div className="container-fluid h-100 m-0">
        <div className="row">
          <Navbar />
          <div className="column second-column overflow-auto px-3 py-2">
            <div className="row mb-2">
              <a href="/" className="text-decoration-none coc-primaryTeal">
                <i className="fa fa-arrow-left" aria-hidden="true"></i> Home
              </a>
            </div>
            <h3>Provider Specialty</h3>
            <div className="mt-4">
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
          ></Filters>
        </div>
      </div>
    </>
  );
};
export default ProviderSpecialty;
