import React, { useEffect, useState } from "react";
import { KPIMetrics } from "../services/ApiDataService";
import Banner from "../components/Banner";
import Card from "../components/Card";
import KPIMetricCard from "../components/KPIMetricCard";
import pic from "../assets/images/LandingPage.png";
import localpolice from "../assets/images/LandingScreenIcons/local_police.png";
import careCategory from "../assets/images/LandingScreenIcons/CareCategory.png";
import forecast from "../assets/images/LandingScreenIcons/forecast.png";
import pcpGroup from "../assets/images/LandingScreenIcons/pcpGroup.png";
import providerspeciality from "../assets/images/LandingScreenIcons/providerspeciality.png";
import careprovider from "../assets/images/LandingScreenIcons/careprovider.png";
import serviceregion from "../assets/images/LandingScreenIcons/serviceregion.png";

const LandingPage = () => {
  const [kpiMetrics, setKpiMetrics] = useState({
    pmpm: 0,
    memberMonths: 0,
    endingMembers: 0,
    mlr: "No Data Available",
    memberMonthsPercentageChange: 0,
    endingMembersPercentage: 0,
    pmpmPercentageChange: 0,
    mlrPercentageChange: 0,
  });

  useEffect(() => {
    const kpimerticsFetch = async () => {
      try {
        const response = await KPIMetrics();
        if (response.status === 200) {
          setKpiMetrics({
            pmpm: response.data.pmpm,
            memberMonths: response.data.memberMonths,
            endingMembers: response.data.endingMembers,
            mlr: "No Data Available",
            pmpmPercentageChange: response.data.pmpmPercentageChange,
            endingMembersPercentage:
              response.data.endingMembersPercentageChange,
            memberMonthsPercentageChange:
              response.data.memberMonthsPercentageChange,
            mlrPercentageChange: 0,
          });
        } else {
          console.log("Failed with status code " + response.status);
        }
      } catch (error) {
        console.log("Could not fetch data" + error);
      }
    };
    kpimerticsFetch();
  }, []);

  return (
    <div className="col-12 container-fluid w-100">
      <div className="row">
        <Banner />
      </div>
      <div className="row coc-teal7BackGround">
        <div className="col-9 m-4 mx-auto text-white">
          <div className="row">
            <div className="col-8" style={{ fontWeight: "300" }}>
              <h2>Cost Analytics</h2>
              <p className="text-justify">
                Explore comprehensive Healthcare expenditure insights with our
                Cost Analytics Dashboard. Identify key cost drivers, recognize
                savings opportunities, and enhance financial planning with
                real-time tracking of essential metrics. Empower strategic
                decision-making for improved cost-effectiveness.
              </p>
            </div>
            <div className="col-4">
              <img src={pic} className="w-100 h-100" />
            </div>
          </div>
        </div>
      </div>
      <div
        className="row mt-0 w-100 position-absolute"
        style={{ top: 300, left: 10 }}
      >
        <div className="col-11 border mb-3 shadow-bottom rounded mx-auto bg-body">
          <div className="row d-flex mx-auto">
            <div className="row d-flex mx-auto mb-3">
              <KPIMetricCard
                value={kpiMetrics.pmpm}
                percentage={kpiMetrics.pmpmPercentageChange}
                title="PMPM"
              />
              <KPIMetricCard
                value={kpiMetrics.endingMembers}
                percentage={kpiMetrics.endingMembersPercentage}
                title="ENDING MEMBERS"
              />
              {/* HardCoded - will be replace by api data */}
              <KPIMetricCard
                value={kpiMetrics.mlr}
                percentage={kpiMetrics.mlrPercentageChange}
                title="MLR"
              />
              <KPIMetricCard
                value={kpiMetrics.memberMonths}
                percentage={kpiMetrics.memberMonthsPercentageChange}
                title="MEMBER MONTHS"
              />
            </div>
            <div className="mb-0 mt-4 ms-3">
              <h4>Insights</h4>
            </div>
            <div className="row d-flex mx-auto mb-3">
              <Card
                label={"Summary"}
                cardUrl={"/summaryPage"}
                imageUrl={localpolice}
                description={
                  "Overview of key Healthcare Expense Metrics and Trends."
                }
              />
              <Card
                label={"Care Category"}
                cardUrl={"/careCategory"}
                imageUrl={careCategory}
                description={
                  "Breakdown of Expenses by distinct Care Categories."
                }
              />
              <Card
                label={"Service Region"}
                cardUrl={"/serviceRegion"}
                imageUrl={serviceregion}
                description={
                  "Geographical Analysis of Healthcare Costs across Regions."
                }
              />
              <Card
                label={"Care Provider"}
                cardUrl={"/careProvider"}
                imageUrl={careprovider}
                description={
                  "Cost analysis for Individual Healthcare Providers."
                }
              />
            </div>
            <div className="row d-flex mx-auto mb-3">
              <Card
                label={"Provider Specialty"}
                cardUrl={"/providerSpecialty"}
                imageUrl={providerspeciality}
                description={
                  "Cost variations among different Provider Specialties."
                }
              />
              <Card
                label={"PCP Group"}
                cardUrl={"/pcpGroup"}
                imageUrl={pcpGroup}
                description={
                  "Expense tracking for Primary Care Provider (PCP) groups."
                }
              />
              <Card
                label={"Forecast"}
                cardUrl={"/forecast"}
                imageUrl={forecast}
                description={
                  "Predictive Analysis to Forecast future Healthcare Expenses"
                }
              />
              <div className="col mx-2 mt-3"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
