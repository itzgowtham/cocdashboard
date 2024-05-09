//Author : D M Gireesh(dmgireesh@deloitte.com)

import React from "react";

const TooltipComponent = (props) => {
  const { value, length, children } = props;
  const isValueLong = value.length > length;
  //   const slicedValue = value.slice(0, length) + "...";

  return (
    <>
      <span title={isValueLong ? value : null}>{children}</span>
    </>
  );
};

export default TooltipComponent;
