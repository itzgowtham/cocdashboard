import { useState, useEffect } from "react";
import RadioButtons from "../../components/RadioButtons";
import TabComponent from "../../components/TabComponent";
import { graphSVG } from "../../assets/images/svg/SVGIcons";
import SummaryInOutPatient from "./SummaryInOutPatient";
import { serviceRegionFetch } from "../../services/ApiDataService";

const ServiceRegionSummary = (props) => {
  const { inputValues } = props;
  const viewTypes = ["Expense PMPM", "Member Months"];
  const [selectedViewType, setSelectedViewType] = useState("Expense PMPM");
  const [title, setTitle] = useState("Expense PMPM Data");
  const [showGraph, setShowGraph] = useState(false);
  const [summaryData, setSummaryData] = useState([]);
  const [summaryDataWithoutFormat, setSummaryDataWithoutFormat] = useState([]);
  const [outPatientData, setOutPatientData] = useState([]);
  const [outPatientDataWithoutFormat, setOutPatientDataWithoutFormat] =
    useState([]);
  const [inPatientData, setInPatientData] = useState([]);
  const [inPatientDataWithoutFormat, setInPatientDataWithoutFormat] = useState(
    []
  );
  const [dataType, setDataType] = useState(["Target", "Actual"]);

  let tabs = [
    {
      label: "Summary",
      content: (
        <SummaryInOutPatient
          showGraph={showGraph}
          data={summaryData}
          selectedViewType={selectedViewType}
          dataWithoutFormat={summaryDataWithoutFormat}
          dataType={dataType}
        />
      ),
    },
    {
      label: "In Patient",
      content: (
        <SummaryInOutPatient
          showGraph={showGraph}
          data={inPatientData}
          selectedViewType={selectedViewType}
          dataWithoutFormat={inPatientDataWithoutFormat}
          dataType={dataType}
        />
      ),
    },
    {
      label: "Out Patient",
      content: (
        <SummaryInOutPatient
          showGraph={showGraph}
          data={outPatientData}
          selectedViewType={selectedViewType}
          dataWithoutFormat={outPatientDataWithoutFormat}
          dataType={dataType}
        />
      ),
    },
  ];

  const formatRegionData = (data) => {
    const formattedData = [];
    for (let item in data) {
      formattedData.push({ service_area_state: item, ...data[item] });
    }
    return formattedData;
  };

  const fetchData = async () => {
    try {
      const res = await serviceRegionFetch({
        ...inputValues,
        viewType: selectedViewType,
      });
      if (res.status === 200) {
        const { all, ip, op } = res.data;
        setSummaryData(formatRegionData(all));
        setOutPatientData(formatRegionData(op));
        setInPatientData(formatRegionData(ip));
        setSummaryDataWithoutFormat(all);
        setInPatientDataWithoutFormat(ip);
        setOutPatientDataWithoutFormat(op);
        setDataType(
          inputValues.graphType === "Target vs Actual" ||
            inputValues.graphType === ""
            ? ["Target", "Actual"]
            : ["Current", "Prior"]
        );
      } else {
        console.log("service region failed with status code " + res.status);
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };

  const buttonChange = (value) => {
    setTitle(value + " Data");
    setSelectedViewType(value);
  };

  const handleTableView = () => {
    setShowGraph(!showGraph);
  };

  useEffect(() => {
    fetchData();
  }, [selectedViewType, inputValues]);

  return (
    <div className="row p-1 mt-3">
      <div className="d-flex justify-content-between">
        <strong>PMPM</strong>
        <div className="d-flex">
          <RadioButtons
            options={viewTypes}
            selectedOption={selectedViewType}
            onChange={buttonChange}
          />
          <div onClick={handleTableView} className="coc-cursor">
            <strong>View:</strong> {showGraph ? graphSVG : "123"}
          </div>
        </div>
      </div>
      <TabComponent tabs={tabs} isSummary={true} />
    </div>
  );
};

export default ServiceRegionSummary;
