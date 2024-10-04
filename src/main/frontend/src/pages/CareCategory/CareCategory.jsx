import Navbar from "../../components/Navbar/Navbar";
import * as FileConstants from "../../constants/FileConstants";
import TabComponent from "../../components/TabComponent";
import Filters from "../../components/Filters";
import {
  careCategoryFetch,
  careCategoryDetailsFetch,
} from "../../services/ApiDataService";
import { DataContext } from "../../context/DataContext";
import CareCategoryDetail from "./CareCategoryDetail.jsx";
import CareCategorySummary from "./CareCategorySummary.jsx";
import { useEffect, useState, useContext } from "react";
import { formatOptions } from "../../utilities/FormatUtilities.jsx";
import Chatbot from "../../components/ChatBot.jsx";
function CareCategory() {
  var maxValue;
  var minValue;
  const [inputValues, setInputValues] = useState(FileConstants.formInputValues);
  const [options, setOptions] = useState(FileConstants.formOptions);
  const monthMapping = FileConstants.monthMapping;
  const radioButtonoptions = FileConstants.radioButtonOptions;
  const [selectedOption, setSelectedOption] = useState("M");
  const [InPatientrowdata, setInPatientdata] = useState();
  const [OutPatientrowdata, setOutPatientdata] = useState();
  const [toggle, setToggle] = useState(true);
  const { filterOptions, initialInputValues } = useContext(DataContext);
  const [graphType, setGraphType] = useState(
    FileConstants.targetVsActulaGraphtype
  );
  const [dataType, setDataType] = useState(["Target", "Actual"]);
  const [tabIndex, setTabIndex] = useState("");
  const [careCategoryOptions, setCareCategoryOptions] = useState([]);
  const [selectedCarecategoryOption, setSelectedCarecategoryOption] =
    useState("");
  const [topProviders, setTopProviders] = useState([]);
  const [topMembers, setTopMembers] = useState([]);
  const [membersCount, setMembersCount] = useState(0);
  const [providersCount, setProvidersCount] = useState(0);

  const handleSelectChange = (value) => {
    setSelectedCarecategoryOption(value);
  };

  const [tabs, setTabs] = useState([
    {
      label: "Summary",
      content: (
        <CareCategorySummary
          InpatientDataArray={InPatientrowdata}
          OutpatientDataArray={OutPatientrowdata}
          dataType={dataType}
        />
      ),
    },
    {
      label: "Details",
      content: (
        <CareCategoryDetail
          inputValues={inputValues}
          careCategoryOptions={careCategoryOptions}
          topProviders={topProviders}
          topMembers={topMembers}
          onSelectChange={handleSelectChange}
          membersCount={membersCount}
          providersCount={providersCount}
        />
      ),
    },
  ]);

  const getDatafromTabs = (index) => {
    setTabIndex(index);
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
      const response2 = await careCategoryFetch(inputValues);
      if (response2.status === 200) {
        const InpatientDataArray = Object.entries(response2.data.InPatient).map(
          ([care_category, data]) => ({ care_category, ...data })
        );
        const OutpatientDataArray = Object.entries(
          response2.data.OutPatient
        ).map(([care_category, data]) => ({ care_category, ...data }));
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
          setGraphType({ type1: "Target", type2: "Actual" });
          setDataType(["Target", "Actual"]);
        } else {
          setGraphType({ type1: "Prior", type2: "Current" });
          setDataType(["Prior", "Current"]);
        }
        setInPatientdata(transformedData);
        setOutPatientdata(transformedData2);
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
    if (tabIndex == 0) {
      fetchData();
    } else {
      fetchDataDetails();
    }
  }, [toggle, tabIndex, selectedCarecategoryOption]);

  const fetchDataDetails = async () => {
    try {
      const response = await careCategoryDetailsFetch({
        ...inputValues,
        careCategory: selectedCarecategoryOption,
      });
      if (response.status === 200) {
        setCareCategoryOptions(formatOptions(response.data.distinctCategory));
        setTopProviders(response.data.topProvidersPerCareCategory);
        setTopMembers(response.data.topMembersPerCareCategory);
        setProvidersCount(response.data.providersCount);
        setMembersCount(response.data.membersCount);
        maxValue = topProviders[0].totalPricePM;
        minValue = topProviders[0].totalPricePM;
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

  useEffect(() => {
    setTabs([
      {
        label: "Summary",
        content: (
          <CareCategorySummary
            InpatientDataArray={InPatientrowdata}
            OutpatientDataArray={OutPatientrowdata}
            graphType={graphType}
            datatype={dataType}
          />
        ),
      },
      {
        label: "Details",
        content: (
          <CareCategoryDetail
            inputValues={inputValues}
            careCategoryOptions={careCategoryOptions}
            topProviders={topProviders}
            topMembers={topMembers}
            onSelectChange={handleSelectChange}
            membersCount={membersCount}
            providersCount={providersCount}
          />
        ),
      },
    ]);
  }, [InPatientrowdata, OutPatientrowdata, careCategoryOptions, toggle]);

  return (
    <div className="container-fluid h-100">
      <div className="row">
        <Navbar />
        <div className="column second-column overflow-auto px-3 py-2">
          <div className="row mb-2">
            <a href="/" className="text-decoration-none coc-primaryTeal">
              <i className="fa fa-arrow-left" aria-hidden="true"></i> Home
            </a>
          </div>
          <h3>Care Category</h3>
        
          <div className="my-4">
            <TabComponent
              tabs={tabs}
              isSummaryDetail={true}
              sendIndex={getDatafromTabs}
            />
          </div>
          <div className="d-flex justify-content-between">
            <span></span>
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
          isSpecialityDisabled={tabIndex == 0 ? true : true}
          isProviderDisabled={tabIndex == 0 ? true : true}
          isTypeDisabled={tabIndex == 0 ? false : true}
          isMonthDisabled={tabIndex == 0 ? false : true}
        />
      </div>
    </div>
  );
}

export default CareCategory;
