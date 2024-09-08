import React from "react";
import { checkPercentage, formatNumber } from "../utilities/FormatUtilities";
function KPIMetricCard(props) {
  const {title,value,percentage}=props;
  return (
    <div className="col mx-2 mt-3">
      <div className="card">
        <div className="card-body">
          <h6 className="card-title">{title}</h6>
          <h4 className="card-text mt-3 mb-3">
          {title === "PMPM" ? "$" : ""}
          {title==="MLR"?value:`${formatNumber(value)}`}
            </h4>
          <div className="d-flex">
            <p className="card-text me-2">
              {checkPercentage(percentage)}
            </p>
            <p className="card-text text-secondary">This Month</p>
          </div>
        </div>
      </div>
    </div>
  );
}
export default KPIMetricCard;
