import React from "react";

const Label = (props) => {
  const { htmlFor, text, parentClasses, className } = props;
  return (
    <label
      htmlFor={htmlFor}
      className={className}
      parentClasses={parentClasses}
    >
      {text}
    </label>
  );
};

export default Label;
