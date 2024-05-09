import { useNavigate } from "react-router-dom";
import Button from "./Button";

const Dashboards = ({name, classname, hasClick}) => {
  const navigate = useNavigate();

  const OnSubmitHandle = () => {
    navigate("/executiveDashBoard");
  };
    return (
        <div className="col">
          <div className="row">
            <i className={classname} aria-hidden="true"></i>
          </div>
          <div className="row">
            <strong>{name}</strong>
          </div>
          <div className="row">
            <p>Short description</p>
          </div>
          <div className="row d-block">
            <Button
              type="button"
              buttonClickHandler={(hasClick)?OnSubmitHandle:null}
              buttonClass="btn btn-info btn-sm w-25"
            >
              View
            </Button>
          </div>
        </div>
    );
}

export default Dashboards;