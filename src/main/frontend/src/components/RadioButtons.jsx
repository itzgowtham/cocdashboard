//Author : D M Gireesh(dmgireesh@deloitte.com)

import React from "react";

const RadioButtons = (props) => {
  const { options, selectedOption, onChange } = props;
  return (
    <div>
      {options.map((option) => (
        <label key={option} style={{ marginRight: "10px" }}>
          <input
            type="radio"
            value={option}
            checked={selectedOption === option}
            onChange={() => onChange(option)}
            className="radioButtonInput"
          />
          &nbsp;{option}
        </label>
      ))}
    </div>
  );
};
export default RadioButtons;
