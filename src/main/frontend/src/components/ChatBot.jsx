import React from "react";
import { useState } from "react";
import "./Components.css";
function ChatBot() {
  const [showModal, setShowModal] = useState(false);
  const handleToggleModal = () => {
    setShowModal(!showModal);
  };
  return (
    <div style={{ position: "fixed", bottom: "20px", right: "20px" }}>
      <button onClick={handleToggleModal}>ChatBot</button>
      {showModal && (
        <div className="modal">
          <div className="modal-content">
            <span className="close" onClick={handleToggleModal}>
              &times;
            </span>
            <h2>Chatbot</h2>
            <p>Chatbot Content</p>
            <button onClick={handleToggleModal}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default ChatBot;
