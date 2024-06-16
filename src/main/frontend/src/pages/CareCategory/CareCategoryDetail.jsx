import React from "react";
import { useState } from "react";
import InputSelectField from "../../components/InputSelectFiled";
import { graphSVG } from "../../assets/images/svg/SVGIcons";
import { vector } from "../../assets/images/svg/SVGIcons";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import { formatNumberColour } from "../../utilities/FormatUtilities";
const CareCategoryDetail = () => {
  const [showGraph, setShowGraph] = useState(false);
  const data = [1, 2, 3, 4, 5, 6, 7, 8];
  const handleTableView = () => {
    setShowGraph(!showGraph);
  };

  return (
    <div>
      <div className="d-flex my-3">
        <span className="col-6">
          <strong>Top 10 Members and Providers by Care Category</strong>
        </span>
        <div className="col-6 d-flex justify-content-end">
          <span>
            <p>Care Category</p>
          </span>
          <span className="mx-2">{vector}</span>
          <span>
            <InputSelectField
              options={[{ label: "Surgery", value: "" }]}
            ></InputSelectField>
          </span>
          <div onClick={handleTableView} className="coc-cursor ms-3">
            View: {showGraph ? graphSVG : "123"}
          </div>
        </div>
      </div>
      <div className="row d-flex">
        <div className="col-6">
          <div className={"ag-theme-quartz"} style={{ height: "500px" }}>
            <div className="table-responsive">
              <table className="table table-bordered border-2 rounded">
                <tr className="border" style={{ backgroundColor: "#F1F4F6" }}>
                  <th className="p-3">No of Providers</th>
                  <th className="p-3">Top Providers</th>
                </tr>
                <tbody>
                  {data.map((item, index) => (
                    <tr key={index}>
                      {index === 0 && (
                        <td
                          rowSpan={data.length}
                          className="align-middle"
                          style={{ width: "30%" }}
                        >
                          <h2 className="p-3">13k</h2>
                        </td>
                      )}
                      {!showGraph ? (
                        <td>
                          <span className="d-flex p-2">
                            Sample provider name - {formatNumberColour(item)}
                          </span>
                        </td>
                      ) : (
                        <td>
                          <span className="d-flex">
                            Sample provider name:
                            <HorizontalBarChart
                              actualvalue={item}
                              type={"number"}
                              aspectRatio={5}
                              graphLength={1}
                              height={"35px"}
                              responsive={true}
                            />
                          </span>
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div className="col-6">
          <div className={"ag-theme-quartz"} style={{ height: "500px" }}>
            <div className="table-responsive">
              <table className="table table-bordered border-2 rounded">
                <tr className="border" style={{ backgroundColor: "#F1F4F6" }}>
                  <th className="p-3">No of Providers</th>
                  <th className="p-3">Top Providers</th>
                </tr>
                <tbody>
                  {data.map((item, index) => (
                    <tr key={index}>
                      {index === 0 && (
                        <td
                          rowSpan={data.length}
                          className="align-middle"
                          style={{ width: "30%" }}
                        >
                          <h2 className="p-3"> 13k</h2>
                        </td>
                      )}
                      {!showGraph ? (
                        <td>
                          <span className="d-flex p-2">
                            Sample provider name - {formatNumberColour(item)}
                          </span>
                        </td>
                      ) : (
                        <td>
                          <span className="d-flex">
                            Sample provider name:
                            <HorizontalBarChart
                              actualvalue={item}
                              type={"number"}
                              aspectRatio={5}
                              graphLength={1}
                              height={"35px"}
                              responsive={true}
                            />
                          </span>
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default CareCategoryDetail;
