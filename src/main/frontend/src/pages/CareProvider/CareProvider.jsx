import React, { useEffect, useState, useContext } from "react";
import Navbar from "../../components/Navbar/Navbar";
import * as FileConstants from "../../constants/FileConstants";
import TabComponent from "../../components/TabComponent";
import Filters from "../../components/Filters";
import {
  careProviderFetch,
  careProviderDetailsFetch,
} from "../../services/ApiDataService";
import { DataContext } from "../../context/DataContext";
import { formatOptions } from "../../utilities/FormatUtilities.jsx";
import CareProviderSummary from "./CareProviderSummary.jsx";
import CareProviderDetails from "./CareProviderDetails.jsx";
import Chatbot from "../../components/ChatBot.jsx";

function CareProvider(props) {
  const [inputValues, setInputValues] = useState(FileConstants.formInputValues);
  const [options, setOptions] = useState(FileConstants.formOptions);
  const monthMapping = FileConstants.monthMapping;
  const radioButtonoptions = FileConstants.radioButtonOptions;
  const [selectedOption, setSelectedOption] = useState("M");
  const [InPatientrowdata, setInPatientdata] = useState();
  const [OutPatientrowdata, setOutPatientdata] = useState();
  const [toggle, setToggle] = useState(true);
  const { filterOptions, initialInputValues } = useContext(DataContext);
  const [dataType, setDataType] = useState(["Target", "Actual"]);
  const [tabIndex, setTabIndex] = useState(0);
  //speciality dropdown
  const [careProviderOptions, setCareProviderOptions] = useState([]);
  const [selectedCareProviderOption, setSelectedCareProviderOption] =
    useState("");
  const [topTenProvidersByCost, setTopTenProvidersByCost] = useState([]);
  const [
    topTenMembersByCostforEachProvider,
    setTopTenMembersByCostForEachProvider,
  ] = useState([]);
  const [
    topTenProvidersByCostForEachSpeciality,
    setTopTenProvidersByCostForEachSpeciality,
  ] = useState([]);
  const [topMembersPerProvider, setTopMembersPerProvider] = useState([]);
  //provider dropdown
  const [providerOptions, setProviderOptions] = useState([]);
  const [selectedProviderOption, setSelectedProviderOption] = useState([]);

  const handleSelectChange = (value) => {
    setSelectedCareProviderOption(value);
  };

  const handleProviderChange = (value) => {
    setSelectedProviderOption(value);
  };

  const [tabs, setTabs] = useState([
    {
      label: "Summary",
      content: (
        <CareProviderSummary
          InpatientDataArray={InPatientrowdata}
          OutpatientDataArray={OutPatientrowdata}
          dataType={dataType}
        />
      ),
    },
    {
      label: "Details",
      content: (
        <CareProviderDetails
          inputValues={inputValues}
          careProviderOptions={careProviderOptions}
          topMembersPerProvider={topMembersPerProvider}
          topTenMembersByCostforEachProvider={
            topTenMembersByCostforEachProvider
          }
          topTenProvidersByCost={topTenProvidersByCost}
          topTenProvidersByCostForEachSpeciality={
            topTenProvidersByCostForEachSpeciality
          }
          onSelectChange={handleSelectChange}
          onProviderChange={handleProviderChange}
          providerOptions={providerOptions}
        />
      ),
    },
  ]);

  const getDatafromTabs = (index) => {
    setTabIndex(index);
  };

  const useeffecttrigger = () => setToggle((prevToggle) => !prevToggle);

  const handleSubmit = async (e) => {
    e.preventDefault();
    useeffecttrigger();
  };

  const handleChange = (fieldName, selectedValue) => {
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

  const fetchData = async () => {
    try {
      const response2 = await careProviderFetch(inputValues);
      if (response2.status === 200) {
        const InpatientDataArray = Object.entries(response2.data.InPatient).map(
          ([care_provider, data]) => ({ care_provider, ...data })
        );
        const OutpatientDataArray = Object.entries(
          response2.data.OutPatient
        ).map(([care_provider, data]) => ({ care_provider, ...data }));
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
        setInPatientdata(transformedData);
        setOutPatientdata(transformedData2);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

  const handleReset = () => {
    setSelectedOption("M");
    const updatedInitialValue = {
      ...FileConstants.formInputValues,
      state: filterOptions.state[1].value,
    };
    setInputValues(updatedInitialValue);
  };

  useEffect(() => {
    if (filterOptions && Array.isArray(filterOptions.state)) {
      let newFilterOptions = { ...filterOptions };
      newFilterOptions.state = [...filterOptions.state];
      newFilterOptions.state.shift();
      setOptions(newFilterOptions);
    }
    if (Object.keys(initialInputValues).length !== 0) {
      let newFilterOptions = { ...filterOptions };
      const updatedInitialValue = {
        ...initialInputValues,
        state: newFilterOptions.state[1].value,
      };
      setInputValues(updatedInitialValue);
    }
  }, [filterOptions, initialInputValues]);

  const fetchDetailsData = async () => {
    try {
      const response3 = await careProviderDetailsFetch({
        ...inputValues,
        speciality: selectedCareProviderOption,
        providerName: selectedProviderOption,
      });
      if (response3.status === 200) {
        setCareProviderOptions(
          [{ label: "All", value: "" },...formatOptions(response3?.data?.distinctSpeciality)]);
        setProviderOptions([{ label: "All", value: "" },...formatOptions(response3?.data?.distinctProvider)]);
        setTopTenProvidersByCost(response3?.data?.top10ProvidersByCost);
        setTopTenMembersByCostForEachProvider(
          response3.data?.top10MembersByCostForEachSpeciality
        );
        setTopTenProvidersByCostForEachSpeciality(
          response3.data?.top10ProvidersByCostForEachSpeciality
        );
        setTopMembersPerProvider(response3.data?.topMembersPerProvider);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

  useEffect(() => {
    if (tabIndex === 0) fetchData();
    else fetchDetailsData();
  }, [toggle, tabIndex, selectedCareProviderOption, selectedProviderOption]);

  useEffect(() => {
    setTabs([
      {
        label: "Summary",
        content: (
          <CareProviderSummary
            InpatientDataArray={InPatientrowdata}
            OutpatientDataArray={OutPatientrowdata}
            dataType={dataType}
          />
        ),
      },
      {
        label: "Details",
        content: (
          <CareProviderDetails
            inputValues={inputValues}
            careProviderOptions={careProviderOptions}
            topMembersPerProvider={topMembersPerProvider}
            topTenMembersByCostforEachProvider={
              topTenMembersByCostforEachProvider
            }
            topTenProvidersByCost={topTenProvidersByCost}
            topTenProvidersByCostForEachSpeciality={
              topTenProvidersByCostForEachSpeciality
            }
            onSelectChange={handleSelectChange}
            onProviderChange={handleProviderChange}
            providerOptions={providerOptions}
          />
        ),
      },
    ]);
  }, [
    InPatientrowdata,
    OutPatientrowdata,
    inputValues,
    toggle,
    topTenProvidersByCost,
  ]);

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
          <h3>Care Provider</h3>
          <div className="mt-4">
            <TabComponent
              tabs={tabs}
              isSummaryDetail={true}
              sendIndex={getDatafromTabs}
            />
          </div>
          <div className="d-flex mt-2 justify-content-between">
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
          RadioButtonOptions={radioButtonoptions}
          RadioSelectedOption={selectedOption}
          areaState={true}
          isSpecialityDisabled={tabIndex === 0 ? true : true}
          isProviderDisabled={tabIndex === 0 ? false : true}
          isTypeDisabled={tabIndex === 0 ? false : true}
          isMonthDisabled={tabIndex == 0 ? false : true}
        />
      </div>
    </div>
  );
}

export default CareProvider;
