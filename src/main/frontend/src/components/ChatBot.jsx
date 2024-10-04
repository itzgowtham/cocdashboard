import React, { useState, useRef } from "react";
import Modal from "react-modal";
import "./Components.css";
import { crossIcon } from "../assets/images/svg/SVGIcons";
import chatRobot from "../assets/images/Chat Button 1.png";
import Draggable from "react-draggable";
import axios from "axios";

// import io from "socket.io-client";
// const socket = io("localhost:8000/qa_chat/invoke");

function Chatbot() {
  const [isChatOpen, setChatOpen] = useState(false);
  const [inputText, setInputText] = useState("");
  const [response, setResponse] = useState({});
  const [chatData, setChatData] = useState([]);
  const chatButtonRef = useRef(null);
  const date = new Date();
  const user = "hjkgnfbgn";
  // const chatData = [];
  let top = 0,
    left = 0;
  console.log("chatData", chatData);

  if (chatButtonRef.current) {
    const rect = chatButtonRef.current.getBoundingClientRect();
    top = rect.top;
    left = rect.left;
  }

  // useEffect(() => {
  //   socket.on("response", (data) => {
  //     console.log("response", data);
  //     setResponse(data);
  //   });

  //   return () => {
  //     socket.off("response");
  //   };
  // }, []);

  const customStyles = {
    overlay: {
      background: "transparent",
    },
    content: {
      position: "fixed",
      top: `${top - 330}px`,
      left: `${left - 330}px`,
      zIndex: "-100",
      width: "350px",
      height: "350px",
      background: "#F5F5F8",
      padding: "5px",
    },
  };

  const handleInputChange = (event) => {
    setInputText(event.target.value);
  };

  // const sendMessage = () => {
  //   const date = new Date();
  //   const payload = [
  //     ...chatData,
  //     {
  //       input: {
  //         input: inputText,
  //       },
  //       config: {
  //         configurable: {
  //           session_id: Math.random(),
  //         },
  //       },
  //       kwargs: {},
  //     },
  //   ];
  //   setChatData(payload);
  //   setInputText("");
  //   console.log("paylaod", payload);

  //   // socket.emit("send payload", payload);
  // };

  const sendMessage = async () => {
    const date = new Date();
    const payload = [
      // ...chatData,
      {
        input: {
          input: inputText,
        },
        config: {
          configurable: {
            session_id: Math.random(),
          },
        },
        kwargs: {},
      },
    ];
    setChatData(payload);
    setInputText("");
    console.log("paylaod", payload);

    try {
      const response = await axios.post(
        "http://localhost:8080/qa_chat/invoke",
        payload
      );
      console.log("api response", response);
      setResponse(response);
      console.log("chatbot response", response);
    } catch (error) {
      console.error("error fetching the response...");
    }
  };

  return (
    <div>
      <Draggable>
        <div className="coc-chatbot">
          <button
            className="border-0"
            ref={chatButtonRef}
            onClick={() => setChatOpen(true)}
            style={{ background: "none" }}
          >
            <img src={chatRobot} className="coc-chatbot-icon"></img>
          </button>
        </div>
      </Draggable>
      <Modal
        isOpen={isChatOpen}
        onRequestClose={() => setChatOpen(false)}
        style={customStyles}
        contentLabel="Chat Modal"
      >
        <div className="d-flex justify-content-between">
          <p></p>
          <button
            className="border-0 p-0 m-0"
            onClick={() => setChatOpen(false)}
          >
            {crossIcon}
          </button>
        </div>
        <div className="chat">
          {chatData?.map((item) => (
            <>
              {user && (
                <>
                  <span className="msg-user-info">
                    Arnold {date.toLocaleString()}
                  </span>
                  <div className="message-right">
                    <span>{item.input.input}</span>
                  </div>
                </>
              )}
              {user !== item.messageOwner && (
                <>
                  <span className="msg-admin-info">
                    Admin {date.toLocaleString()}
                  </span>
                  <div className="message-left">
                    <span>response...</span>
                  </div>
                </>
              )}
            </>
          ))}
          {/* {response?.map((item) => (
            <>
              {user !== item.messageOwner && (
                <>
                  <span className="msg-admin-info">
                    Admin {date.toLocaleString()}
                  </span>
                  <div className="message-left">
                    <span>{item.output.answer}</span>
                  </div>
                </>
              )}
            </>
          ))} */}
        </div>
        <div className="chat-input-field mt-0 p-1">
          <div className="d-flex justify-content-between">
            <input
              type="text"
              placeholder="Type in your message"
              className="message-input"
              value={inputText}
              onChange={handleInputChange}
            />
            <span className="send-icon" onClick={sendMessage}>
              <i
                className="fa far fa-paper-plane searchColor"
                aria-hidden="true"
              ></i>
            </span>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default Chatbot;
