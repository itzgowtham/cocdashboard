import React, { createContext, useState, useEffect } from "react";
import { FilterOptions } from "../services/ApiDataService";
import { formatOptions } from "../utilities/FormatUtilities";
import { useLocation } from "react-router-dom";

export const DataContext = createContext();

export const DataProvider = ({ children }) => {
  const [filterOptions, setFilterOptions] = useState({});
  const [initialInputValues, setInitialInputValues] = useState({});
  const location = useLocation();
  console.log("Current Path Name", location.pathname);
  const currentPath = location.pathname;
  const fetchData = async () => {
    try {
      const response1 = await FilterOptions();
      if (response1.status === 200) {
        const { lob, state, months } = response1.data;
        const formattedlob = formatOptions(lob);
        const updatedlob = [{ label: "All", value: "" }, ...formattedlob];
        const formattedState = formatOptions(state);
        const updatedState = [{ label: "All", value: "" }, ...formattedState];
        const newOptions = {
          lob: updatedlob,
          state: updatedState,
          product: [{ label: "All", value: "" }],
          amountType: formatOptions(["Paid"]),
          endMonth: formatOptions(months),
          graphType: [
            { label: "Target vs Actual", value: "Target vs Actual" },
            { label: "Current vs Prior", value: "Current vs Prior" },
          ],
        };
        const newInputValues = {
          lob: newOptions.lob[0].value,
          state: newOptions.state[0].value,
          product: newOptions.product[0].value,
          amountType: newOptions.amountType[0].value,
          endMonth: newOptions.endMonth[0].value,
          graphType: newOptions.graphType[0].value,
          startMonth: "",
        };
        setFilterOptions(newOptions);
        setInitialInputValues(newInputValues);
      } else {
        console.log(
          "FilterOptions failed with status code " + response1.status
        );
      }
    } catch (error) {
      console.log("Could not fetch data: " + error);
    }
  };
  useEffect(() => {
    fetchData();
  }, []);

  return (
    <DataContext.Provider value={{ initialInputValues, filterOptions }}>
      {children}
    </DataContext.Provider>
  );
};
