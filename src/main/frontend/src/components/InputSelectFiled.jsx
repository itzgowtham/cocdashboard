import React from "react";
import "./Components.css";

const InputSelectField = (props) => {
  const { label, options, value, onChange, name } = props;
  const optionsArray = Array.isArray(options) ? options : [];

  const handleChange = (event) => {
    const { name, value } = event.target;
    onChange(name, value);
  };

  return (
    <div>
      {label && (
        <label
          className={`form-label my-0 fs-6 ${
            props.isDisabled ? "text-secondary" : ""
          }`}
        >
          {label}
        </label>
      )}
      <select
        value={value}
        onChange={handleChange}
        name={name}
        className="form-select form-select-sm mb-2 rounded"
        style={{ maxHeight: "50px", overflowY: "auto" }}
        disabled={props.isDisabled}
      >
        {optionsArray.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
    </div>
  );
};

export default InputSelectField;
