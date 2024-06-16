import Navbar from "../../components/Navbar/Navbar";
import * as FileConstants from "../../constants/FileConstants";
import TabComponent from "../../components/TabComponent";
import Filters from "../../components/Filters";
import { careCategoryFetch } from "../../services/ApiDataService";
import { DataContext } from "../../context/DataContext";
import CareCategoryDetail from "./CareCategoryDetail.jsx";
import CareCategorySummary from "./CareCategorySummary.jsx";
import { useEffect, useState, useContext } from "react";
function CareCategory() {
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

  const [tabs, setTabs] = useState([
    {
      label: "Summary",
      content: (
        <CareCategorySummary
          InpatientDataArray={InPatientrowdata}
          OutpatientDataArray={OutPatientrowdata}
        />
      ),
    },
    {
      label: "Details",
      content: <CareCategoryDetail />,
    },
  ]);

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
          setDataType(["Current", "Prior"]);
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
    fetchData();
  }, [toggle, options]);

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
        content: <CareCategoryDetail />,
      },
    ]);
  }, [InPatientrowdata, OutPatientrowdata, inputValues]);

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
          <h3>Care Category</h3>
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
          RadioButtonOptions={radioButtonoptions}
          RadioSelectedOption={selectedOption}
        />
      </div>
    </div>
  );
}

export default CareCategory;
