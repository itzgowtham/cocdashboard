import React, { useState } from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import "bootstrap/dist/css/bootstrap.css";
import "font-awesome/css/font-awesome.min.css";
import { BrowserRouter } from "react-router-dom";
// import { HealthAISSO } from "@healthaisso/health-ai-sso";

/* redirectURL to be changed to actual domain url after hosting and 
registered with Deloitte as valid redirect url for sso */

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <BrowserRouter>
  <App />
    {/* <HealthAISSO
      AppComponent={
        <div className="app-root">
          <App />
        </div>
      }
      redirectURL="https://deloitte.coc.com/callback"
    /> */}
  </BrowserRouter>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
