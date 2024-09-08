import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
import LineChart from "../../components/LineChart";

const ForecastData = (props) => {
  const { rowData, showGraph, selectedOption } = props;

  const colDefs = [
    {
      field: "months",
      headerName: "Period",
      flex: 1,
      unSortIcon:true,
    },
    {
      field: `${
        selectedOption === "Cost"
          ? "pmpm_forecast"
          : "activemembership_forecast"
      }`,
      headerName: `Forecasted Value (${
        selectedOption === "Cost" ? "PMPM" : "Members"
      })`,
      flex: 1,
      unSortIcon:true,
      //   hide: showGraph,
      cellRenderer: (params) => {
        return selectedOption === "Cost"
          ? `$ ${params.data.pmpm_forecast}`
          : params.data.activemembership_forecast;
      },
    },
    {
      field: "confidenceInterval",
      headerName: `Confidence Interval (${
        selectedOption === "Cost" ? "PMPM" : "Members"
      })`,
      flex: 1,
      unSortIcon:true,
      //   hide: showGraph,
      cellRenderer: (params) => {
        return selectedOption === "Cost"
          ? `+/- $ ${params.data.confidenceInterval?.toFixed(2)}`
          : `+/- ${params.data.confidenceInterval}`;
      },
    },
  ];

  const data = {
    labels: rowData.map((data) => data.months?.substring(0, 3)),
    datasets: [
      {
        type: "scatter",
        label: "Observed Data Points",
        data:
          selectedOption === "Cost"
            ? rowData
                .filter((data) => data.pmpm > 0)
                .map((data) => ({
                  x: data.months?.substring(0, 3),
                  y: data.pmpm,
                }))
            : rowData
                .filter((data) => data.activemembership > 0)
                .map((data) => ({
                  x: data.months?.substring(0, 3),
                  y: data.activemembership,
                })),

        backgroundColor: "#004F59",
      },
      {
        type: "scatter",
        label: "Test Data Points",
        data:
          selectedOption === "Cost"
            ? rowData
                .filter((data) => data.pmpm === 0 && data.pmpm_forecast > 0)
                .map((data) => ({
                  x: data.months?.substring(0, 3),
                  y: data.pmpm_forecast,
                }))
            : rowData
                .filter(
                  (data) =>
                    data.activemembership === 0 &&
                    data.activemembership_forecast > 0
                )
                .map((data) => ({
                  x: data.months?.substring(0, 3),
                  y: data.activemembership_forecast,
                })),

        backgroundColor: "#F39000",
      },
      {
        label: "Forecast",
        data: rowData.map((data) =>
          selectedOption === "Cost"
            ? data.pmpm_forecast
            : data.activemembership_forecast
        ),
        fill: false,
        pointRadius: 0,
        borderWidth: 2,
        tension: 0.2,
        borderColor: "#00ABAB",
      },

      {
        label: "Uncertainty Interval",
        backgroundColor: "rgba(0, 171, 171, 0.2)",
        borderColor: "transparent",
        pointRadius: 0,
        tension: 0.2,
        fill: false, //no fill here
        data: rowData.map((data) =>
          selectedOption === "Cost"
            ? data.pmpm_forecast_lower
            : data.activemembership_forecast_lower
        ),
      },
      {
        label: "Uncertainty Interval",
        backgroundColor: "rgba(0, 171, 171, 0.2)",
        borderColor: "transparent",
        pointRadius: 0,
        tension: 0.2,
        fill: "-1", //fill until previous dataset
        data: rowData.map((data) =>
          selectedOption === "Cost"
            ? data.pmpm_forecast_upper
            : data.activemembership_forecast_upper
        ),
      },
    ],
  };

  // const graphType = data.datasets.map((items) => items.label);
  return (
    <>
      {showGraph ? (
        <>
          <LineChart
            labels={data.labels}
            data={data.datasets}
            selectedOption={selectedOption}
          />
        </>
      ) : (
        <div
          className={"ag-theme-quartz"}
          style={{
            height: "500px",
          }}
        >
          {showGraph ? (
            <h3>Graphical View in Progress</h3>
          ) : (
            <AgGridReact
              rowData={rowData}
              columnDefs={colDefs}
              rowHeight={46}
              pagination={true}
              paginationPageSize={20}
            />
          )}
        </div>
      )}
    </>
  );
};

export default ForecastData;
