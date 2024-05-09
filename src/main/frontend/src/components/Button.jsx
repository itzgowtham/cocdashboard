import React from "react";

const Button = (props) => {
  return (
    <button
      type={props.type}
      className={props.buttonClass}
      disabled={props.disabled}
      onClick={props.buttonClickHandler}
      style={props.style}
    >
      {props.children}
    </button>
  );
};

export default Button;
