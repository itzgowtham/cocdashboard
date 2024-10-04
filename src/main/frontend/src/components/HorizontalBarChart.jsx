import React from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";
import { formatNumberforChart } from "../utilities/FormatUtilities";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

function HorizontalBarChart(props) {
  const {
    actualvalue,
    label,
    targetvalue,
    type,
    graphtype,
    aspectRatio,
    graphLength,
    height,
    maxValue,
  } = props;
  const options = {
    indexAxis: "y",
    elements: {
      bar: {
        borderWidth: 0,
        borderRadius: 3,
      },
    },

    responsive: true,
    interaction: {
      intersect: false,
      mode: "index",
    },
    plugins: {
      tooltip: {
        mode: "index",
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
              if (type === "number") {
                label += formatNumberforChart(value);
              } else {
                if (value !== null) {
                  label += new Intl.NumberFormat("en-US", {
                    style: "currency",
                    currency: "USD",
                  }).format(value);
                }
              }
            }

            return label;
          },
        },
      },
      legend: {
        display: false,
      },
      title: {
        display: label,
        text: label,
        align: "start",
      },
    },
    scales: {
      x: {
        categoryPercentage: 0.1,
        categorySpacing: 0.1,
        beginAtZero: true,
        max: maxValue,
        min: 0,
        display: false,
        grid: {
          display: false,
        },
        axis: {
          lineWidth: 10,
        },
      },
      y: {
        categorySpacing: 0.1,
        display: true,
        beginAtZero: true,
        grid: {
          display: false,
        },
      },
    },
    aspectRatio: aspectRatio,
  };
  const labels = [""];
  const data = {
    labels,
    datasets:
      graphLength === 2
        ? [
            {
              label: graphtype.type1,
              data: [targetvalue],
              borderColor: ["#004F59", "#6FC2B4"],
              backgroundColor: ["#004F59", "#6FC2B4"],
              barPercentage: 0.7,
              categoryPercentage: 1,
            },
            {
              label: graphtype.type2,
              data: [actualvalue],
              borderColor: ["#6FC2B4"],
              backgroundColor: ["#6FC2B4"],
              barPercentage: 0.7,
              categoryPercentage: 1,
            },
          ]
        : [
            {
              data: [actualvalue],
              borderColor: ["#6FC2B4"],
              backgroundColor: ["#6FC2B4"],
              barPercentage: 0.7,
              categoryPercentage: 1,
            },
          ],
  };

  return (
    <div style={{ height: height }}>
      <Bar options={options} data={data} />
    </div>
  );
}

export default HorizontalBarChart;
