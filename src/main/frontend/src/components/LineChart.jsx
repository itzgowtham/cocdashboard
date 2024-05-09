import React from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Filler,
  Legend,
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Filler,
  Tooltip,
  Legend
);
const LineChart = (props) => {
  const data = {
    labels: props.labels,
    datasets: props.data,
  };

  const options = {
    responsive: true,
    scales: {
      x: {
        grid: {
          display: false,
        },
      },
      y: {
        grid: {
          display: true,
        },
        ticks: {
          //   maxTicksLimit: 3,
          callback: function (value, index, values) {
            console.log(value);
            return props.selectedOption === "Cost" ? "$ " + value : value;
          },
        },
      },
    },
    plugins: {
      tooltip: {
        mode: "nearest",
        intersect: false,
        axis: "xy",
        callbacks: {
          label: function (context) {
            var label = context.dataset.label || "";
            if (label) {
              label += ": ";
            }
            var value = context.dataset.data[context.dataIndex];
            console.log("Tooltip value", value, typeof value);
            if (value !== null) {
              label += new Intl.NumberFormat("en-US", {
                style: "currency",
                currency: "USD",
              }).format(value);
            }
            return label;
          },
        },
      },

      legend: {
        display: false,
      },
      datalabels: {
        display: false,
      },
      filler: {
        propagate: true,
      },
    },
    // aspectRatio:7,
    maintainAspectRatio: false,
  };
  return (
    <div className="line-chart-container card p-1" style={{ height: "450px" }}>
      <div className="d-flex justify-content-end">
        <span>
          <i
            className="fa fa-circle me-1 small"
            style={{ color: "#004F59" }}
            aria-hidden="true"
          ></i>
          <span className="w-300 " style={{ fontSize: "11px" }}>
            Observed Data Points &nbsp;
          </span>
          <i
            className="fa fa-circle me-1 small"
            style={{ color: "#00ABAB" }}
            aria-hidden="true"
          ></i>
          <span className="w-300 " style={{ fontSize: "11px" }}>
            Forecast &nbsp;
          </span>
          <i
            className="fa fa-circle me-1 small"
            style={{ color: "rgba(0, 171, 171, 0.2)" }}
            aria-hidden="true"
          ></i>
          <span className="w-300 " style={{ fontSize: "11px" }}>
            Uncertainty Interval &nbsp;
          </span>
          <i
            className="fa fa-circle ms-2 me-1 small"
            style={{ color: "#F39000" }}
            aria-hidden="true"
          ></i>
          <span className="w-300 " style={{ fontSize: "11px" }}>
            Test Data Points &nbsp;
          </span>
        </span>
      </div>
      <Line data={data} options={options} />
    </div>
  );
};
export default LineChart;
