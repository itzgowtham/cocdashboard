import React, { useState } from "react";
import { useEffect } from "react";
import TabComponent from "../../components/TabComponent";
import CareProviderInOutPatient from "./CareProviderInOutPatient";
import { graphSVG } from "../../assets/images/svg/SVGIcons";
function CareCategorySummary(props) {
  const { InpatientDataArray, OutpatientDataArray } = props;
  const [showGraph, setShowGraph] = useState(false);
  const [tabs, setTabs] = useState([
    {
      label: "In Patient",
      content: <CareProviderInOutPatient showGraph={showGraph} rowdata={""} />,
    },
    {
      label: "Out Patient",
      content: <CareProviderInOutPatient showGraph={showGraph} rowdata={""} />,
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
          <CareProviderInOutPatient
            showGraph={showGraph}
            rowdata={InpatientDataArray}
          />
        ),
      },
      {
        label: "Out Patient",
        content: (
          <CareProviderInOutPatient
            showGraph={showGraph}
            rowdata={OutpatientDataArray}
          />
        ),
      },
    ]);
  }, [InpatientDataArray, OutpatientDataArray, showGraph]);

  return (
    <div className="container-fluid h-100 mt-3">
      <div className="row">
        <div className="column second-column overflow-auto p-0">
          <div className="col-12 p-0">
            <div className="d-flex justify-content-between m-0 p-0">
              <strong>PMPM</strong>
              <span className="d-flex">
                {showGraph ? (
                  <div className="me-4">
                    <i
                      className="fa fa-circle me-2"
                      style={{ color: "#004F59" }}
                      aria-hidden="true"
                    >
                      Target
                    </i>
                    <i
                      className="fa fa-circle ms-2"
                      style={{ color: "#6FC2B4" }}
                      aria-hidden="true"
                    >
                      Actual
                    </i>
                  </div>
                ) : (
                  <p></p>
                )}
                <div onClick={handleTableView} className="coc-cursor">
                  <strong>View:</strong> {showGraph ? graphSVG : "123"}
                </div>
              </span>
            </div>
            <div>
              <TabComponent tabs={tabs} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
export default CareCategorySummary;
