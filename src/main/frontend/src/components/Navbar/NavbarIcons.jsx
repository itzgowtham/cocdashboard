import React from "react";
import { useNavigate } from "react-router-dom";
import TooltipComponent from "../TooltipComponent";
import "./Navbar.css";

function NavbarIcons(props) {
  const { icon, title } = props;
  const navigate = useNavigate();
  const isSelected = window.location.pathname === props.url;
  const onclickEvent = () => {
    navigate(props.url);
  };

  return (
    <TooltipComponent value={title} length={0}>
      <div
        className={`coc-container coc-cursor ps-3 ${
          isSelected ? "selected" : ""
        }`}
        onClick={onclickEvent}
      >
        <div className="coc-container align-items-center pb-2 pt-2 ps-1">
          {icon}
        </div>
      </div>
    </TooltipComponent>
  );
}

export default NavbarIcons;
