import React, { useState } from "react";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import InputSelectField from "../../components/InputSelectFiled";
import TooltipComponent from "../../components/TooltipComponent";
import { graphSVG, vector } from "../../assets/images/svg/SVGIcons";
import {
  formatNumberColour,
  formatNumber,
  formatNumberforChart,
} from "../../utilities/FormatUtilities";
const CareCategoryDetail = (props) => {
  const {
    careCategoryOptions,
    topProviders,
    topMembers,
    membersCount,
    providersCount,
    onSelectChange,
  } = props;

  const [showGraph, setShowGraph] = useState(false);

  const handleChange = (e, selectedValue) => {
    onSelectChange(selectedValue);
    console.log(selectedValue);
  };

  const handleTableView = () => {
    setShowGraph(!showGraph);
  };

  return (
    <div>
      <div className="d-flex my-3">
        <span className="col-5">
          <strong>Top 10 Members and Providers by Care Category</strong>
        </span>
        <div className="col-7 d-flex justify-content-end">
          <span>
            <p>Care Category</p>
          </span>
          <span className="mx-2">{vector}</span>
          <span style={{ width: "100px" }}>
            <InputSelectField
              options={careCategoryOptions}
              onChange={handleChange}
              name={"careCategory"}
            ></InputSelectField>
          </span>
          <div onClick={handleTableView} className="coc-cursor ms-3">
            View: {showGraph ? graphSVG : "123"}
          </div>
        </div>
      </div>
      <div className="row d-flex">
        <div className="col-6">
          <div className="table-responsive">
            <table className="table table-bordered border-2 rounded">
              <tr className="border" style={{ backgroundColor: "#F1F4F6" }}>
                <th className="p-3">No. of Members</th>
                <th className="p-3">Top Members</th>
              </tr>
              <tbody style={{ height: "100px" }}>
                {topProviders.length < 1 && (
                  <tr className="align-middle">
                    <td
                      colSpan={2}
                      style={{ color: "black", justifyContent: "center" }}
                    >
                      <span className="d-flex justify-content-center">
                        No rows to show
                      </span>
                    </td>
                  </tr>
                )}
                {topMembers &&
                  topMembers.map((item, index) => (
                    <tr key={index}>
                      {index == 0 && (
                        <td
                          rowSpan={topMembers.length}
                          className="align-middle"
                          style={{ width: "30%" }}
                        >
                          <h2 className="p-3">{formatNumber(membersCount)}</h2>
                        </td>
                      )}
                      {!showGraph ? (
                        <td>
                          <span className="d-flex p-2">
                            <span className="col-6">{item.memberUid}</span>
                            <span className="col-6">
                              {formatNumberColour(
                                formatNumberforChart(item.totalPricePM)
                              )}
                            </span>
                          </span>
                        </td>
                      ) : (
                        <td>
                          <span className="d-flex">
                            <span className="col-3">{item.memberUid}:</span>
                            <span className="col-9">
                              <HorizontalBarChart
                                actualvalue={item.totalPricePM}
                                type={"number"}
                                aspectRatio={7}
                                graphLength={1}
                                height={"35px"}
                                responsive={true}
                                maxValue={topMembers[0].totalPricePM}
                              />
                            </span>
                          </span>
                        </td>
                      )}
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>
        <div className="col-6">
          <div className="table-responsive">
            <table className="table table-bordered border-2 rounded">
              <tr className="border" style={{ backgroundColor: "#F1F4F6" }}>
                <th className="p-3">No. of Providers</th>
                <th className="p-3">Top Providers</th>
              </tr>
              <tbody style={{ height: "100px" }}>
                {topProviders.length < 1 && (
                  <tr className="align-middle">
                    <td
                      colSpan={2}
                      style={{ color: "black", justifyContent: "center" }}
                    >
                      <span className="d-flex justify-content-center">
                        No rows to show
                      </span>
                    </td>
                  </tr>
                )}
                {topProviders &&
                  topProviders.map((item, index) => (
                    <tr key={index}>
                      {index == 0 && (
                        <td
                          rowSpan={topProviders.length}
                          className="align-middle"
                          style={{ width: "30%" }}
                        >
                          <h2 className="p-3">
                            {formatNumber(providersCount)}
                          </h2>
                        </td>
                      )}
                      {!showGraph ? (
                        <td>
                          <span className="d-flex p-2">
                            <span className="col-8">
                              <TooltipComponent
                                value={item.providerName}
                                length={25}
                                children={item.providerName}
                              >
                                {item.providerName}
                              </TooltipComponent>
                            </span>
                            <span className="col-4">
                              {formatNumberColour(
                                formatNumberforChart(item.totalPricePM)
                              )}
                            </span>
                          </span>
                        </td>
                      ) : (
                        <td>
                          <span className="d-flex">
                            <span className="col-6 mt-2 p-0">
                              <TooltipComponent
                                value={item.providerName}
                                length={20}
                                children={item.providerName}
                              >
                                {item.providerName}
                              </TooltipComponent>
                            </span>

                            <span className="col-6">
                              <HorizontalBarChart
                                actualvalue={item.totalPricePM}
                                type={"number"}
                                aspectRatio={5}
                                graphLength={1}
                                height={"35px"}
                                responsive={true}
                                maxValue={topProviders[0].totalPricePM}
                              />
                            </span>
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
  );
};
export default CareCategoryDetail;
