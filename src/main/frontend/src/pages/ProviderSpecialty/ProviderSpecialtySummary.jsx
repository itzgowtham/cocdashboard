import React, { useState, useEffect } from "react";
import TabComponent from "../../components/TabComponent.jsx";
import ProviderSpecialtyInOutPatient from "./ProviderSpecialtyInOutPatient.jsx";
import { graphSVG } from "../../assets/images/svg/SVGIcons.jsx";

const ProviderSpecialtySummary = (props) => {
  const { InpatientDataArray, OutpatientDataArray, dataType } = props;
  const [showGraph, setShowGraph] = useState(false);
  const [tabs, setTabs] = useState([
    {
      label: "In Patient",
      content: (
        <ProviderSpecialtyInOutPatient
          showGraph={showGraph}
          rowdata={""}
          dataType={dataType}
        />
      ),
    },
    {
      label: "Out Patient",
      content: (
        <ProviderSpecialtyInOutPatient
          showGraph={showGraph}
          rowdata={""}
          dataType={dataType}
        />
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
            dataType={dataType}
          />
        ),
      },
      {
        label: "Out Patient",
        content: (
          <ProviderSpecialtyInOutPatient
            showGraph={showGraph}
            rowdata={OutpatientDataArray}
            dataType={dataType}
          />
        ),
      },
    ]);
  }, [InpatientDataArray, OutpatientDataArray, showGraph]);

  return (
    <>
      <div className="d-flex justify-content-between mt-3">
        <strong>PMPM</strong>
        <div onClick={handleTableView} className="coc-cursor">
          <strong>View:</strong> {showGraph ? graphSVG : "123"}
        </div>
      </div>
      <TabComponent tabs={tabs} />
    </>
  );
};

export default ProviderSpecialtySummary;
