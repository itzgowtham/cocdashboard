import { React, useState } from "react";
import { filterSVG, leftSVG, rightSVG } from "../assets/images/svg/SVGIcons";
import InputSelectField from "./InputSelectFiled";
import "./Components.css";
import Button from "./Button";
function Filters({
  FormSelectOptions,
  onChange,
  handleReset,
  handleSubmit,
  RadioButtonOptions,
  RadioSelectedOption,
  isProviderDisabled,
  isSpecialityDisabled,
  isTypeDisabled,
}) {
  const initialCollapseState =
    localStorage.getItem("filterscollapsed") === "true" ? true : false;
  const [collapsed, setCollapsed] = useState(initialCollapseState);
  const toggleCollapse = () => {
    setCollapsed((prevState) => {
      const newValue = !prevState;
      localStorage.setItem("filterscollapsed", String(newValue));
      return newValue;
    });
  };
  return (
    <div
      className={`column border third-column vh-100 ${
        collapsed ? "collapsed" : ""
      }`}
    >
      <div>
        {collapsed && (
          <div>
            <div className="row">
              <div className="col-12">
                <div className="d-flex">
                  <Button
                    buttonClass="border-0 m-0 p-0 me-2 coc-filterbg"
                    buttonClickHandler={toggleCollapse}
                  >
                    <div className="mt-2">{leftSVG}</div>
                  </Button>
                  <div className="mt-0">{filterSVG}</div>
                </div>
              </div>
            </div>
          </div>
        )}
        {!collapsed && (
          <div className="col-12 vh-100">
            <div className="row">
              <div className="d-flex">
                <Button
                  buttonClickHandler={toggleCollapse}
                  buttonClass="mt-1 me-2 border-0 coc-filterbg"
                >
                  {rightSVG}
                </Button>
                {filterSVG}
                <h6 className="ms-2 mt-3">Filters</h6>
              </div>
            </div>
            <div className="row">
              <form onSubmit={handleSubmit} action="submit">
                <div className="mb-2">
                  <InputSelectField
                    label="Line of Business"
                    options={FormSelectOptions.lob}
                    onChange={onChange}
                    name={"lob"}
                  />
                </div>
                <div className="mb-2">
                  <InputSelectField
                    label="Service Area State"
                    options={FormSelectOptions.state}
                    onChange={onChange}
                    name={"state"}
                  />
                </div>
                <div className="mb-2">
                  <InputSelectField
                    label="Product"
                    options={FormSelectOptions.product}
                    onChange={onChange}
                    name={"product"}
                    isDisabled={true}
                  />
                </div>
                <div className="mb-2">
                  <InputSelectField
                    label="Speciality"
                    options={FormSelectOptions.product}
                    onChange={onChange}
                    name={"speciality"}
                    isDisabled={true}
                  />
                </div>
                <div className="mb-2">
                  <InputSelectField
                    label="Provider"
                    options={FormSelectOptions.product}
                    onChange={onChange}
                    name={"provider"}
                    isDisabled={true}
                  />
                </div>

                <div className="mb-2">
                  <InputSelectField
                    label="Amount Type"
                    options={FormSelectOptions.amountType}
                    onChange={onChange}
                    name="amountType"
                    isDisabled={true}
                  />
                </div>
                <div className="mb-2">
                  <InputSelectField
                    label="Incurred Ending Month"
                    options={FormSelectOptions.endMonth}
                    onChange={onChange}
                    name="endMonth"
                  />
                </div>
                <div
                  className="btn-group text-black p-0 mb-2 ms-1"
                  role="group"
                  aria-label="Radio button group"
                >
                  {RadioButtonOptions.map((option) => (
                    <label
                      key={option.label}
                      className={`coc-btn btn btn-md text-black px-2 ${
                        RadioSelectedOption === option.label
                          ? "coc-active"
                          : "border border-dark"
                      }`}
                    >
                      <input
                        name="startMonth"
                        type="radio"
                        value={option.label}
                        checked={RadioSelectedOption === option.label}
                        onChange={(e) => onChange("startMonth", e.target.value)}
                        className="visually-hidden"
                      />
                      {option.label}
                    </label>
                  ))}
                </div>

                <div className="mb-2">
                  <InputSelectField
                    label="Type"
                    options={FormSelectOptions.graphType}
                    onChange={onChange}
                    name="graphType"
                    isDisabled={isTypeDisabled?true:false}
                  />
                </div>
                <div className="d-flex mx-auto">
                  <Button
                    type="submit"
                    buttonClass="btn btn-light btm-sm bg-white"
                  >
                    Submit
                  </Button>
                  <Button
                    type="reset"
                    buttonClass="btn btn-light btm-sm bg-white"
                    buttonClickHandler={handleReset}
                  >
                    Reset
                  </Button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
export default Filters;
