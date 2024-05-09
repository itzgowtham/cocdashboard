import React from "react";
import { useNavigate } from "react-router-dom";
const Card = ({ label, imageUrl, description, cardUrl }) => {
  const navigate=useNavigate()

  const handleClickEvent=()=>{
    navigate(cardUrl)
  }
  return (
    <div className="col mx-2 mt-3 coc-cursor" onClick={handleClickEvent}>
        <div className="card">
          <div
            className="m-2 rounded p-2  coc-cardImg"
          >
            <img
              src={imageUrl}
              className="card-img-top"
              alt="Card Image"
            />
          </div>
          <div className="card-body">
            <h5 className="card-title">{label}</h5>
            <p className="card-text">{description}</p>
          </div>
        </div>
      
    </div>
  );
};

export default Card;
