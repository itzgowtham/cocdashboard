import { trendingUpSvg, trendingdownSvg } from "../assets/images/svg/SVGIcons";

export const checkPercentage = (value) => {
  const absValue = Math.abs(value);
  const colorClass = value >= 0 ? "text-success" : "text-danger";
  return (
    <span className={colorClass}>
      {value >= 0 ? <>&#x25B2;</> : <>&#x25BC;</>} {absValue}%{" "}
    </span>
  );
};

export const formatNumberforChart = (value) => {
    return `${(value).toFixed(2)}`;
  
};


export const formatNumber = (value) => {
  value = Number(value);
  if (value >= 1000000000) {
    return `${(value / 1000000000).toFixed(0)}B`;
  } else if (value >= 1000000) {
    return `${(value / 1000000).toFixed(0)}M`;
  } else if (value >= 1000) {
    return `${(value / 1000).toFixed(0)}K`;
  } else {
    // return value;
    return `${(value).toFixed(3)}`;
  }
};

export const formatNumberColour = (value) => {
  var val = "";
  if (value >= 1000000000) {
    val = `${(value / 1000000000).toFixed(0)}B`;
  } else if (value >= 1000000) {
    val = `${(value / 1000000).toFixed(0)}M`;
  } else if (value >= 1000) {
    val = `${(value / 1000).toFixed(0)}K`;
  } else if (value < 1) {
    val = `${value.toFixed(3)}`;
  } else {
    val = value;
  }
  return <b style={{ color: "#004F59" }}>${value}</b>;
};

export const formatOptions = (optionsArray) => {
  return optionsArray.map((option) => ({ label: option, value: option }));
};

export const handleValueinDollar = (params) => {
  return "$" + params.value;
};

export const handleDifferenceinPercentage = (params) => {
  const value = params.value;
  const absValue = Math.abs(value);
  return (
    <span>
      {value >= 0 ? trendingUpSvg : trendingdownSvg}&nbsp;{absValue}
    </span>
  );
};

export const handleValue = (params) => {
  return (
    <p>
      {params.value} - {formatNumberColour(234.56)}
    </p>
  );
};
