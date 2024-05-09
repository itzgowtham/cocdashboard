import React, { useState } from "react";
import { rightSVG, leftSVG } from "../../assets/images/svg/SVGIcons";
import logo from "../../assets/images/Deloitte_Logo 1.png";
import icon from "../../assets/images/DeloitteIcon.png";
import { NavbarSVG } from "../../assets/images/svg/SVGIcons";
import { navLinks } from "../../constants/FileConstants";
import NavbarLinks from "./NavbarLinks";
import NavbarIcons from "./NavbarIcons";
import Button from "../Button";
import "./Navbar.css";

const Navbar = () => {
  const initialCollapseState =
    localStorage.getItem("navbarcollapsed") === "true" ? true : false;
  const [collapsed, setCollapsed] = useState(initialCollapseState);
  const toggleCollapse = () => {
    setCollapsed((prevState) => {
      const newValue = !prevState;
      localStorage.setItem("navbarcollapsed", String(newValue));
      return newValue;
    });
  };
  return (
    <div
      className={`column vh-100 border first-column ${
        collapsed ? "collapsed" : ""
      }`}
    >
      <div className="col-12 vh-100 m-0">
        {collapsed && (
          <div className="col vh-100 align-items-center">
            <div className="row mt-2">
              <div className="d-flex mb-2">
                <div className="align-items-center">
                  <img src={icon} className="coc-img" />
                </div>
                <div>
                  <Button
                    buttonClass="coc-navbarbg border-0 ms-0"
                    buttonClickHandler={toggleCollapse}
                  >
                    {rightSVG}
                  </Button>
                </div>
              </div>

              <h4 className="ms-0" style={{ fontWeight: "300" }}>
                CA
              </h4>
              <div className="col-12 p-0">
                {NavbarSVG.map((icon, index) => (
                  <div key={index}>
                    <NavbarIcons
                      icon={icon}
                      url={navLinks[index].url}
                      title={navLinks[index].title}
                    />
                  </div>
                ))}
              </div>
            </div>{" "}
          </div>
        )}
        {!collapsed && (
          <div className="col vh-100 position-relative">
            <div className="row mt-3">
              <div className="d-flex">
                <div className="row mb-2">
                  <img src={logo} />
                </div>
                <Button
                  buttonClickHandler={toggleCollapse}
                  buttonClass="coc-navbarbg border-0 ms-7 mt-0"
                >
                  {leftSVG}
                </Button>
              </div>
              <div className="row">
                <p className="coc-font ms-1">Cost Analytics</p>
              </div>
              {navLinks.map((link, index) => (
                <NavbarLinks
                  key={index}
                  title={link.title}
                  icon={NavbarSVG[index]}
                  url={link.url}
                />
              ))}
            </div>
            <div className="d-flex coc-username mb-4 p-2 rounded-2">
              <span className="d-flex ">
                <p className="m-0 px-1 coc-teal7BackGround text-white">AC</p>
                <p className="ms-1 m-0">Arnold Chase</p>
              </span>
              <p className="m-0">
                <strong>...</strong>
              </p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
export default Navbar;
