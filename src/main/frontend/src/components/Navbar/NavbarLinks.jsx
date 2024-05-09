import React from "react";
import { useNavigate } from "react-router-dom";
import "./Navbar.css";

function NavbarLinks(props) {
  const navigate = useNavigate();
  const isSelected = window.location.pathname === props.url;
  const onclickEvent = () => {
    navigate(props.url);
  };
  return (
    <div
      className={`coc-container coc-cursor d-inline-flex p-2 mt-0 ps-3 align-items-center ${
        isSelected ? "selected" : ""
      }`}
      onClick={onclickEvent}
    >
      {props.icon}
      <p className="mt-0 ms-2 mb-0 ml-2">{props.title}</p>
    </div>
  );
}

export default NavbarLinks;
