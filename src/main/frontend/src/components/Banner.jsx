import logo from "../assets/images/Deloitte_Logo 1.png";
import "../App.css";

const Banner = (props) => {
  return (
    <div className="container-fluid">
      <div className="d-flex justify-content-between align-items-center p-2">
        <div className="d-flex align-items-center">
            <img src={logo} alt="Deloitte Logo" className="px-2"/>
            <h4 className="mt-1 ms-3">|</h4>
            <h3 className="ms-4 mt-2">Cost Analytics</h3>
        </div>
        <div className="d-flex align-items-center coc-tealbg">
          <p
            className="text-white px-2 mb-0"
          >
            AC
          </p>
        </div>
      </div>
    </div>
  );
};

export default Banner;
