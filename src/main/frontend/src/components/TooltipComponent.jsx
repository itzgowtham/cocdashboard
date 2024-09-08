//Author : D M Gireesh(dmgireesh@deloitte.com)

import React from "react";

const TooltipComponent = (props) => {
  const { value, length, children } = props;
  const isValueLong = length == 0 ? true : value.length > length;
  const slicedValue = value.slice(0, length) + "...";

  return (
    <>
      <span title={isValueLong ? value : null}>
        {length == 0 ? children : isValueLong ? slicedValue : children}
      </span>
    </>
  );
};

export default TooltipComponent;
