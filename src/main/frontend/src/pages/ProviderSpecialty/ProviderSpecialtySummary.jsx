import React, { useState, useEffect } from "react";
import TabComponent from "../../components/TabComponent.jsx";
import ProviderSpecialtyInOutPatient from "./ProviderSpecialtyInOutPatient.jsx";
import { graphSVG } from "../../assets/images/svg/SVGIcons.jsx";

const ProviderSpecialtySummary = (props) => {
  const { InpatientDataArray, OutpatientDataArray } = props;
  const [showGraph, setShowGraph] = useState(false);
  const [tabs, setTabs] = useState([
    {
      label: "In Patient",
      content: (
        <ProviderSpecialtyInOutPatient showGraph={showGraph} rowdata={""} />
      ),
    },
    {
      label: "Out Patient",
      content: (
        <ProviderSpecialtyInOutPatient showGraph={showGraph} rowdata={""} />
      ),
    },
  ]);

  const handleTableView = () => {
    setShowGraph(!showGraph);
  };

  useEffect(() => {
    setTabs([
      {
        label: "In Patient",
        content: (
          <ProviderSpecialtyInOutPatient
            showGraph={showGraph}
            rowdata={InpatientDataArray}
          />
        ),
      },
      {
        label: "Out Patient",
        content: (
          <ProviderSpecialtyInOutPatient
            showGraph={showGraph}
            rowdata={OutpatientDataArray}
          />
        ),
      },
    ]);
  }, [InpatientDataArray, OutpatientDataArray, showGraph]);

  return (
    <>
      <div className="d-flex justify-content-between mt-3">
        <strong>PMPM Data</strong>
        <div onClick={handleTableView} className="coc-cursor">
          <strong>View:</strong> {showGraph ? graphSVG : "123"}
        </div>
      </div>
      <TabComponent tabs={tabs} />
    </>
  );
};

export default ProviderSpecialtySummary;
