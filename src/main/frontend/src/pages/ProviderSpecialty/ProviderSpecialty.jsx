import React, { useEffect, useState, useContext } from "react";
import ProviderSpecialtySummary from "./ProviderSpecialtySummary.jsx";
import ProviderSpecialtyDetails from "./ProviderSpecialtyDetails.jsx";
import TabComponent from "../../components/TabComponent.jsx";
import Filters from "../../components/Filters";
import * as FileConstants from "../../constants/FileConstants";
import Navbar from "../../components/Navbar/Navbar.jsx";
import {
  providerSpecialtyDetailsFetch,
  providerSpecialtyFetch,
} from "../../services/ApiDataService.js";
import { DataContext } from "../../context/DataContext";
import { formatOptions } from "../../utilities/FormatUtilities.jsx";
import Chatbot from "../../components/ChatBot.jsx";
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
  const [membersPerSpecialty, setMembersPerSpecialty] = useState([]);
  const [topSpecialties, setTopSpecialties] = useState([]);
  const [topMembersPerSpecialty, setTopMembersPerSpecialty] = useState([]);
  const [topProvidersBySpecialty, setTopProvidersBySpecialty] = useState([]);
  const [specialtyOptions, setSpecialtyOptions] = useState([]);
  const [dataType, setDataType] = useState(["Target", "Actual"]);
  const [tabIndex, setTabIndex] = useState("");
  const [selectedProviderSpecialtyOption, setSelectedProviderSpecialtyOption] =
    useState("");

  const handleSelectChange = (value) => {
    setSelectedProviderSpecialtyOption(value);
  };

  const [tabs, setTabs] = useState([
    {
      label: "Summary",
      content: (
        <ProviderSpecialtySummary
          InpatientDataArray={InPatientrowdata}
          OutpatientDataArray={OutPatientrowdata}
          dataType={dataType}
          graphtype={{ type1: dataType[0], type2: dataType[1] }}
        />
      ),
    },
    {
      label: "Details",
      content: (
        <ProviderSpecialtyDetails
          membersPerSpecialty={membersPerSpecialty}
          topSpecialties={topSpecialties}
          topMembersPerSpecialty={topMembersPerSpecialty}
          topProvidersBySpecialty={topProvidersBySpecialty}
          specialtyOptions={specialtyOptions}
          onSelectChange={handleSelectChange}
        />
      ),
    },
  ]);

  const getDatafromTabs = (index) => {
    setTabIndex(index);
  };

  const handleChange = (fieldName, selectedValue) => {
    console.log(selectedValue);
    if (fieldName === "startMonth") {
      setSelectedOption(selectedValue);
      if (selectedValue === "YTD") {
        const year = inputValues.endMonth.slice(-4);
        const startMonth = `Jan ${year}`;
        if (
          options.endMonth.some(
            (option) =>
              option.label === startMonth && option.value === startMonth
          )
        ) {
          setInputValues({
            ...inputValues,
            startMonth: startMonth,
          });
        } else {
          setInputValues({
            ...inputValues,
            startMonth: `Jul 2019`,
          });
        }
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
          const startMonth =
            options.endMonth[options.endMonth.length - 1].value;
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

  useEffect(() => {
    if (tabIndex == 0) {
      fetchData();
    } else {
      fetchDetailsData();
    }
  }, [toggle, tabIndex, selectedProviderSpecialtyOption]);

  const fetchDetailsData = async () => {
    try {
      const response = await providerSpecialtyDetailsFetch({
        ...inputValues,
        speciality: selectedProviderSpecialtyOption,
      });
      if (response.status === 200) {
        const membersPerSpecialty = response.data.membersPerSpeciality;
        const topSpecialties = response.data.topSpecialitiesByCost;
        const topMembersPerSpecialty = response.data.topMembersPerSpeciality;
        const topProvidersBySpecialty = response.data.topProvidersPerSpeciality;
        const specialtyOptions = response.data.distinctSpeciality;
        setMembersPerSpecialty(membersPerSpecialty);
        setTopSpecialties(topSpecialties);
        setTopMembersPerSpecialty(topMembersPerSpecialty);
        setTopProvidersBySpecialty(topProvidersBySpecialty);
        setSpecialtyOptions(formatOptions(specialtyOptions));
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
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
        if (
          inputValues.graphType === "Target vs Actual" ||
          inputValues.graphType === ""
        ) {
          setDataType(["Target", "Actual"]);
        } else {
          setDataType(["Prior", "Current"]);
        }
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
    setTabs([
      {
        label: "Summary",
        content: (
          <ProviderSpecialtySummary
            InpatientDataArray={InPatientrowdata}
            OutpatientDataArray={OutPatientrowdata}
            dataType={dataType}
          />
        ),
      },
      {
        label: "Details",
        content: (
          <ProviderSpecialtyDetails
            membersPerSpecialty={membersPerSpecialty}
            topSpecialties={topSpecialties}
            topMembersPerSpecialty={topMembersPerSpecialty}
            topProvidersBySpecialty={topProvidersBySpecialty}
            specialtyOptions={specialtyOptions}
            onSelectChange={handleSelectChange}
          />
        ),
      },
    ]);
  }, [InPatientrowdata, OutPatientrowdata, specialtyOptions, toggle, tabIndex]);

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
              <TabComponent
                tabs={tabs}
                isSummaryDetail={true}
                sendIndex={getDatafromTabs}
              />
            </div>
            <div className="chatbot-container">
              <p></p>
              <Chatbot />
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
            isSpecialityDisabled={tabIndex == 0 ? false : true}
            isProviderDisabled={true}
            isTypeDisabled={tabIndex == 0 ? false : true}
            isMonthDisabled={tabIndex == 0 ? false : true}
          />
        </div>
      </div>
    </>
  );
};
export default ProviderSpecialty;
