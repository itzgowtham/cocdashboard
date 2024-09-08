import React, { useState } from "react";
import { useEffect } from "react";
import TabComponent from "../../components/TabComponent";
import CareCategoryInOutPatient from "./CareCategoryInOutPatient";
import { graphSVG } from "../../assets/images/svg/SVGIcons";
function CareCategorySummary(props) {
  const { InpatientDataArray, OutpatientDataArray, datatype } = props;
  const [showGraph, setShowGraph] = useState(false);
  const [tabs, setTabs] = useState([
    {
      label: "In Patient",
      content: (
        <CareCategoryInOutPatient
          showGraph={showGraph}
          rowdata={""}
          datatype={datatype}
        />
      ),
    },
    {
      label: "Out Patient",
      content: (
        <CareCategoryInOutPatient
          showGraph={showGraph}
          rowdata={""}
          datatype={datatype}
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
          <CareCategoryInOutPatient
            showGraph={showGraph}
            rowdata={InpatientDataArray}
            datatype={datatype}
          />
        ),
      },
      {
        label: "Out Patient",
        content: (
          <CareCategoryInOutPatient
            showGraph={showGraph}
            rowdata={OutpatientDataArray}
            datatype={datatype}
          />
        ),
      },
    ]);
  }, [InpatientDataArray, OutpatientDataArray, showGraph]);

  return (
    <div className="container-fluid mt-3">
      <div className="row">
          <div className="col-12 p-0">
            <div className="d-flex justify-content-between p-0">
              <strong>PMPM</strong>
              <span className="d-flex">
                {showGraph ? (
                  <span className="me-4">
                    <i
                      className="fa fa-circle me-1"
                      style={{ color: "#004F59" }}
                      aria-hidden="true"
                    >
                      {props.graphType ? props.graphType.type1 : "Target"}
                    </i>
                    <i
                      className="fa fa-circle ms-2 me-1"
                      style={{ color: "#6FC2B4" }}
                      aria-hidden="true"
                    >
                      {props.graphType ? props.graphType.type2 : "Actual"}
                    </i>
                  </span>
                ) : (
                  <p></p>
                )}
                <div onClick={handleTableView} className="coc-cursor">
                  <strong>View:</strong> {showGraph ? graphSVG : "123"}
                </div>
              </span>
            </div>
            <TabComponent tabs={tabs} />
          </div>
       
      </div>
    </div>
  );
}
export default CareCategorySummary;
