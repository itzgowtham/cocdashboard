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
import { formatNumber } from "../utilities/FormatUtilities";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

function HorizontalBarChart(props) {
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
              if (props.type === "number") {
                label += formatNumber(value);
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
        display: props.label,
        text: props.label,
        align: "start",
      },
    },
    scales: {
      x: {
        categoryPercentage: 0.1,
        categorySpacing: 0.1,
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
        grid: {
          display: false,
        },
      },
    },
    aspectRatio: props.aspectRatio,
  };
  const graphLength = props.graphLength;
  const labels = [""];
  const data = {
    labels,
    datasets:
      graphLength === 2
        ? [
            {
              label: props.graphtype.type1,
              data: [props.targetvalue],
              borderColor: ["#004F59", "#6FC2B4"],
              backgroundColor: ["#004F59", "#6FC2B4"],
              barPercentage: 0.7,
              categoryPercentage: 1,
            },
            {
              label: props.graphtype.type2,
              data: [props.actualvalue],
              borderColor: ["#6FC2B4"],
              backgroundColor: ["#6FC2B4"],
              barPercentage: 0.7,
              categoryPercentage: 1,
            },
          ]
        : [
            {
              data: [props.actualvalue],
              borderColor: ["#6FC2B4"],
              backgroundColor: ["#6FC2B4"],
              barPercentage: 0.7,
              categoryPercentage: 1,
            },
          ],
  };

  return (
    <div style={{ height: props.height }}>
      <Bar options={options} data={data} />
    </div>
  );
}

export default HorizontalBarChart;
