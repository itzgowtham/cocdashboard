import React, { useState } from "react";
import "./Components.css";

const TabComponent = (props) => {
  const { tabs, isSummaryDetail } = props;
  const [activeTab, setActiveTab] = useState(0);

  const handleTabClick = (index) => {
    setActiveTab(index);
    if (props.sendIndex) {
      props.sendIndex(index);
    }
  };

  return (
    <div className="tabComponent">
      <div className="tab-header">
        {tabs.map((tab, index) => (
          <div
            key={index}
            className={`tab ${!isSummaryDetail ? "m-2" : "mb-2"} p-1 ${
              activeTab === index ? "active" : "inactive"
            } ${isSummaryDetail && "summary-details-tab"} ${
              tabs.length === index + 1 && isSummaryDetail && "last-tab"
            } ${index === 0 && isSummaryDetail && "first-tab"}`}
            onClick={() => handleTabClick(index)}
          >
            {tab.label}
          </div>
        ))}
      </div>
      {tabs[activeTab].content}
    </div>
  );
};

export default TabComponent;
