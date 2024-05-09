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

const AreaChart = (props) => {
  const data = {
    labels: props.labels,
    datasets: [
      {
        label: props.graphtype.type1,
        data: props.data.data1,
        fill: true,
        backgroundColor: "rgba(0, 79, 89, 0.2)",
        borderColor: "rgba(0, 79, 89)",
        borderWidth: 1,
        pointRadius: 0,
        borderCapStyle: "butt",
      },
      {
        label: props.graphtype.type2,
        data: props.data.data2,
        fill: true,
        backgroundColor: "rgba(0, 171, 171, 0.2)",
        borderColor: "rgba(0, 171, 171)",
        borderWidth: 1,
        pointRadius: 0,
        borderCapStyle: "butt",
      },
    ],
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
          maxTicksLimit: 3,
          callback: function (value) {
            return "$ " + value;
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
    aspectRatio: 3,
    maintainAspectRatio: false,
  };
  return (
    <div className="chart-container">
      <Line data={data} options={options} />
    </div>
  );
};
export default AreaChart;
