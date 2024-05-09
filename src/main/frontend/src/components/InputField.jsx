import React from "react";

const InputField = (props) => {
  return (
    <input
      id={props.name}
      type={props.type}
      value={props.content}
      name={props.name}
      rows={props.rows}
      className={props.parentClasses}
      onChange={(event) => props.changeHandler(event)}
      placeholder={props.placeholder}
      ref={props.ref}
    />
  );
};

export default InputField;
