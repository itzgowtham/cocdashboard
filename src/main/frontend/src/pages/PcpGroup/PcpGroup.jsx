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
import {
  pcpGroupDetailsFetch,
  pcpGroupFetch,
} from "../../services/ApiDataService.js";
import { formatOptions } from "../../utilities/FormatUtilities.jsx";
import Chatbot from "../../components/ChatBot.jsx";
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
  const [dataType, setDataType] = useState(["Target", "Actual"]);
  const [tabIndex, setTabIndex] = useState("");
  const [membersPerPcpGroup, setMembersPerPcpGroup] = useState([]);
  const [topMembersByCostForEachPcp, setTopMembersByCostForEachPcp] = useState(
    []
  );
  const [topPcpByCost, setTopPcpByCost] = useState([]);
  const [topPcpByCostForEachSpeciality, setTopPcpByCostForEachSpeciality] =
    useState([]);
  const [pcpOptions, setPcpOptions] = useState([]);
  const [specialityOptions, setSpecialityOptions] = useState([]);
  const [selectedPcpOption, setSelectedPcpOption] = useState("");
  const [selectedSpecialityOption, setSelectedSpecialityOption] = useState("");

  const handleSelectChange = (value) => {
    setSelectedPcpOption(value);
  };

  const handleSpecialityChange = (value) => {
    setSelectedSpecialityOption(value);
  };

  const [tabs, setTabs] = useState([
    {
      label: "Summary",
      content: <PcpGroupSummary rowData={rowData} dataType={dataType} />,
    },
    {
      label: "Details",
      content: (
        <PcpGroupDetails
          membersPerPcpGroup={membersPerPcpGroup}
          topMembersByCostForEachPcp={topMembersByCostForEachPcp}
          topPcpByCost={topPcpByCost}
          topPcpByCostForEachSpeciality={topPcpByCostForEachSpeciality}
          pcpOptions={pcpOptions}
          specialtyOptions={specialityOptions}
          onPcpChange={handleSelectChange}
          onSpecialtyChange={handleSpecialityChange}
        />
      ),
    },
  ]);

  useEffect(() => {
    setTabs([
      {
        label: "Summary",
        content: <PcpGroupSummary rowData={rowData} dataType={dataType} />,
      },
      {
        label: "Details",
        content: (
          <PcpGroupDetails
            membersPerPcpGroup={membersPerPcpGroup}
            topMembersByCostForEachPcp={topMembersByCostForEachPcp}
            topPcpByCost={topPcpByCost}
            topPcpByCostForEachSpeciality={topPcpByCostForEachSpeciality}
            pcpOptions={pcpOptions}
            specialtyOptions={specialityOptions}
            onPcpChange={handleSelectChange}
            onSpecialtyChange={handleSpecialityChange}
          />
        ),
      },
    ]);
  }, [rowData, toggle, tabIndex, pcpOptions, specialityOptions]);

  useEffect(() => {
    if (filterOptions) {
      setOptions(filterOptions);
    }
    if (Object.keys(initialInputValues).length !== 0) {
      setInputValues(initialInputValues);
    }
  }, [filterOptions, initialInputValues]);

  useEffect(() => {
    if (tabIndex == 0) {
      fetchData();
    } else {
      fetchDetailsData();
    }
  }, [toggle, tabIndex, selectedPcpOption, selectedSpecialityOption]);

  const fetchDetailsData = async () => {
    try {
      const response = await pcpGroupDetailsFetch({
        ...inputValues,
        speciality: selectedSpecialityOption,
        providername: selectedPcpOption,
      });
      if (response.status === 200) {
        const membersPerPcpGroup = response.data.membersPerPcpGroup;
        const topMembersByCostForEachPcp =
          response.data.topMembersByCostForEachPcp;
        const topPcpByCost = response.data.topPcpByCost;
        const topPcpByCostForEachSpeciality =
          response.data.topPcpByCostForEachSpeciality;
        const specialityOptions = formatOptions(
          response.data.distinctSpeciality
        );
        const pcpOptions = formatOptions(response.data.distinctProvider);
        setMembersPerPcpGroup(membersPerPcpGroup);
        setTopMembersByCostForEachPcp(topMembersByCostForEachPcp);
        setTopPcpByCost(topPcpByCost);
        setTopPcpByCostForEachSpeciality(topPcpByCostForEachSpeciality);
        setPcpOptions([{ label: "All", value: "" }, ...pcpOptions]);
        setSpecialityOptions([
          { label: "All", value: "" },
          ...specialityOptions,
        ]);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
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
        if (
          inputValues.graphType === "Target vs Actual" ||
          inputValues.graphType === ""
        ) {
          setDataType(["Target", "Actual"]);
        } else {
          setDataType(["Prior", "Current"]);
        }
        setRowData(transformedData);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

  const useeffecttrigger = () => {
    setSelectedSpecialityOption("");
    setSelectedPcpOption("");
    setToggle((prevToggle) => !prevToggle);
  };

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
            FormSelectValues={selectedValue}
            onChange={handleChange}
            handleSubmit={handleSubmit}
            handleReset={handleReset}
            RadioButtonOptions={radioButtonoptions}
            RadioSelectedOption={selectedOption}
            isSpecialityDisabled={tabIndex == 0 ? true : true}
            isProviderDisabled={tabIndex == 0 ? false : true}
            isTypeDisabled={tabIndex == 0 ? false : true}
            isMonthDisabled={tabIndex == 0 ? false : true}
          />
        </div>
      </div>
    </>
  );
};

export default PcpGroup;
