import React, { useState, useRef, useEffect } from "react";
import Modal from "react-modal";
import "./Components.css";
import chatRobot from "../assets/images/Chat Button 1.png";
import messageIcon from "../assets/images/MessageIcon.png";
import Draggable from "react-draggable";
import { chatBotDataFetch } from "../services/ApiDataService";

function Chatbot() {
  const [isMaximized, setIsMaximized] = useState(false);
  const [isChatOpen, setChatOpen] = useState(false);
  const [inputText, setInputText] = useState("");
  const [conversationHistory, setConversationHistory] = useState([]);
  const date = new Date();
  const iconRef = useRef(null);
  const [modalPosition, setModalPosition] = useState({});

  /*To update position and modal open state*/
  const openModal = () => {
    if (iconRef.current) {
      const rect = iconRef.current.getBoundingClientRect();
      const modalWidth = isMaximized ? 1000 : 400;
      const modalHeight = 533;
      setModalPosition({
        top: rect.top + window.scrollY - modalHeight + 25,
        left: rect.left + window.scrollX - modalWidth + 25,
      });
    }
    setChatOpen(true);
  };

  /*To update position and modal*/
  const updateModalPosition = () => {
    if (iconRef.current) {
      const rect = iconRef.current.getBoundingClientRect();
      const modalWidth = isMaximized ? 1000 : 400;
      const modalHeight = 533;
      setModalPosition({
        top: rect.top + window.scrollY - modalHeight + 25,
        left: rect.left + window.scrollX - modalWidth + 25,
      });
    }
  };

  /*To update position when chat modal maximized or minimized*/
  useEffect(() => {
    if (isChatOpen) {
      updateModalPosition();
    }
  }, [isMaximized]);

  /*To toggle between maximize and minimize */
  const toggleMaximize = () => {
    setIsMaximized(!isMaximized);
  };

  const customStyles = {
    overlay: {
      background: "transparent",
    },
    content: {
      position: "absolute",
      top: `${modalPosition.top}px`,
      left: `${modalPosition.left}px`,
      zIndex: "1001",
      width: isMaximized ? "1000px" : "400px",
      height: isMaximized ? "533px" : "533px",
      background: "#F5F5F8",
      padding: "5px",
      overflow: "hidden",
    },
  };

  /**To update the position when window resize happens */
  useEffect(() => {
    const handleResize = () => {
      updateModalPosition();
    };
    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, [isMaximized]);

  const handleInputChange = (event) => {
    setInputText(event.target.value);
  };

  const sendMessage = async () => {
    console.log("fghjkl");
    const newHistoryItem = {
      user: inputText,
      timestamp: new Date().toLocaleString(),
    };
    setConversationHistory((prevHistory) => [...prevHistory, newHistoryItem]);
    const payload = {
      input: {
        input: inputText,
      },
      config: {
        configurable: {
          session_id: "abc123",
        },
      },
      kwargs: {},
    };
    setInputText("");

    try {
      const response = await chatBotDataFetch(payload);
      if (response.status === 200) {
        console.log("chatbot response", response);
        const answer = response?.data?.output?.answer;
        const updatedHistoryItem = { ...newHistoryItem, answer };
        setConversationHistory((prevHistory) => {
          const updatedHistory = [...prevHistory];
          updatedHistory[updatedHistory.length - 1] = updatedHistoryItem;
          return updatedHistory;
        });
      }
    } catch (error) {
      console.error("error fetching the response...");
    }
  };

  return (
    <Draggable>
      <div>
        <div className="coc-chatbot">
          <button
            ref={iconRef}
            onClick={openModal}
            className="border-0 m-1"
            style={{ background: "none" }}
          >
            <img src={chatRobot} className="coc-chatbot-icon"></img>
          </button>
        </div>
        <div className="chat-container">
          <Modal
            isOpen={isChatOpen}
            onRequestClose={() => setChatOpen(false)}
            style={customStyles}
            contentLabel="Chat Modal"
          >
            <div className="d-flex justify-content-between">
              <h6></h6>
              <span className="d-flex">
                <button onClick={toggleMaximize} className="border-0 m-1">
                  {isMaximized ? (
                    <i class="fa fa-window-minimize" aria-hidden="true"></i>
                  ) : (
                    <i class="fa fa-window-maximize" aria-hidden="true"></i>
                  )}
                </button>
                <button
                  className="border-0 m-1"
                  onClick={() => setChatOpen(false)}
                >
                  <i class="fa fa-times" aria-hidden="true"></i>
                </button>
              </span>
            </div>
            <div className="chat">
              {conversationHistory.length === 0 ? (
                <span className="chatbot-welcome-message">
                  <img
                    src={messageIcon}
                    alt="Welcome Icon"
                    height={80}
                    width={100}
                  />
                  <h6 className="mt-3">
                    Welcome to our chatbot! How can we assist you today?
                  </h6>
                </span>
              ) : (
                conversationHistory.map((item, index) => (
                  <div key={index}>
                    {item.user && (
                      <>
                        <span className="msg-user-info">
                          Arnold {item.timestamp}
                        </span>
                        <div className="message-right">{item.user}</div>
                      </>
                    )}
                    {item.answer && (
                      <>
                        <span className="msg-admin-info">
                          Admin {date.toLocaleString()}
                        </span>
                        <div className="message-left">{item.answer} </div>
                      </>
                    )}
                  </div>
                ))
              )}
            </div>
            <div className="chat-input-field mt-0 p-1">
              <div className="d-flex justify-content-between">
                <input
                  type="text"
                  placeholder="Type in your message"
                  className="message-input"
                  value={inputText}
                  onChange={handleInputChange}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      sendMessage();
                      e.preventDefault();
                    }
                  }}
                />

                <button
                  onClick={sendMessage}
                  className="m-0 border-0"
                  style={{ zIndex: "1000" }}
                >
                  <i
                    className="fa far fa-paper-plane searchColor coc-cursor"
                    aria-hidden="true"
                  />
                </button>
              </div>
            </div>
          </Modal>
        </div>
      </div>
    </Draggable>
  );
}

export default Chatbot;
