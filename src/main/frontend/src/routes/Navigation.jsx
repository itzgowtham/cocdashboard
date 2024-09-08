import { Route, Routes } from "react-router-dom";
import LandingPage from "../pages/LandingPage";
import SummaryPage from "../pages/SummaryPage/SummaryPage";
import CareCategory from "../pages/CareCategory/CareCategory";
import ProviderSpecialty from "../pages/ProviderSpecialty/ProviderSpecialty";
import CareProvider from "../pages/CareProvider/CareProvider";
import PcpGroup from "../pages/PcpGroup/PcpGroup";
import ServiceRegion from "../pages/ServiceRegion/ServiceRegion";
import Forecast from "../pages/Forecast/Forecast";

// import { Callback } from "@healthaisso/health-ai-sso";

/* redirectURL to be changed to actual domain url after hosting and 
registered with Deloitte as valid redirect url for sso */
const redirectURL = "https://deloitte.coc.com";

const Navigation = () => {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      {/* <Route
        path="/callback"
        element={
          <Callback redirectURL={redirectURL} homeNavigateObject="/"></Callback>
        }
      /> */}
      <Route path="/summaryPage" element={<SummaryPage />} />
      <Route path="/careCategory" element={<CareCategory />} />
      <Route path="/serviceRegion" element={<ServiceRegion />} />
      <Route path="/providerSpecialty" element={<ProviderSpecialty />} />
      <Route path="/careProvider" element={<CareProvider />} />
      <Route path="/pcpGroup" element={<PcpGroup />} />
      <Route path="/forecast" element={<Forecast />} />
    </Routes>
  );
};
export default Navigation;
